package com.github.antilaby.antilaby.main;

import com.github.antilaby.antilaby.api.config.ConfigReader;
import com.github.antilaby.antilaby.compat.HLSCompat;
import com.github.antilaby.antilaby.updater.UpdateManager;
import com.github.antilaby.antilaby.util.Miscellaneous;
import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.util.ServerHelper;
import com.github.antilaby.antilaby.api.updater.VersionType;
import com.github.antilaby.antilaby.command.AntiLabyCommand;
import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.compat.ProtocolLibSupport;
import com.github.antilaby.antilaby.config.Config;
import com.github.antilaby.antilaby.config.InitConfig;
import com.github.antilaby.antilaby.events.EventsPost18;
import com.github.antilaby.antilaby.events.PlayerJoin;
import com.github.antilaby.antilaby.util.DataManager;
import com.github.antilaby.antilaby.command.LabyInfoCommand;
import com.github.antilaby.antilaby.lang.LanguageManager;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.metrics.BStats;
import com.github.antilaby.antilaby.metrics.Metrics;
import com.github.antilaby.antilaby.util.NmsUtils;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Main class of AntiLaby Spigot plugin
 *
 * @author NathanNr
 */
public class AntiLaby extends JavaPlugin {

	/**
	 * The main logger
	 */
	public static final Logger LOG = new Logger("Main");
	// The singleton instance
	private static AntiLaby instance;

	/**
	 * Gets the singleton instance
	 *
	 * @return the singleton instance
	 */
	public static AntiLaby getInstance() {
		return instance;
	}

	// Read from the configuration file
	private ConfigReader configReader = new ConfigReader();

	/**
	 * All loaded Features
	 */
	private final Set<PluginFeature> loadedFeatures = new HashSet<>(PluginFeature.values().length);
	// Compatible?
	private boolean compatible;
	// Is this a beta version?
	private VersionType versionType;
	// The cleanup Thread deletes the saved LabyPlayer data
	private final Thread cleanup = new Thread(DataManager::cleanup, "AntiLabyCleanup");
	// is this pre 1.9?
	private boolean before19 = false;

	/**
	 * This should be used instead of accessing {@link #loadedFeatures} directly
	 *
	 * @return an unmodifiable set of all enabled plugin features
	 */
	public Set<PluginFeature> getLoadedFeatures() {
		return Collections.unmodifiableSet(loadedFeatures);
	}

	/**
	 * Gets whether the mc version is pre 1.9, used by the {@link LanguageManager} system
	 *
	 * @return whether this is before 1.9
	 */
	public boolean isPrior19() {
		return before19;
	}

	@Override
	public File getFile() {
		return super.getFile();
	}

	public Path getPath() {
		return getFile().toPath();
	}

	public Path getDataPath() {
		return getDataFolder().toPath();
	}

	/**
	 * Gets the {@link VersionType} of the plugin
	 *
	 * @return the version type
	 */
	public VersionType getVersionType() {
		return versionType;
	}

	@Override
	public void onDisable() {
		if (ServerHelper.getImplementation() == ServerHelper.ImplementationType.GLOWSTONE) return;
		// Save Data if compatible
		if (compatible) DataManager.saveData();
			// If not, we need to remove the cleanup thread
		else Runtime.getRuntime().removeShutdownHook(cleanup);
		LOG.info("Disabled AntiLaby by the AntiLaby Team version " + getDescription().getVersion() + " successfully!");
	}

	@Override
	public void onEnable() {
		// Glowstone is not supported yet
		if (ServerHelper.getImplementation() == ServerHelper.ImplementationType.GLOWSTONE) return;
		// Delete datamanager file on exit
		Runtime.getRuntime().addShutdownHook(cleanup);
		// Check if the server is compatible with AntiLaby
		final String nmsver = NmsUtils.getVersion();
		int version = 0;
		try {
			version = Integer.parseInt(nmsver.split("_")[1]);
		} catch (final NumberFormatException e) {
			LOG.fatal("Unknown NMS version (" + nmsver + ')');
			compatible = false;
			disableIfNotCompatible();
		}
		if (version >= 8) {
			// Ensure the DataFolder exists
			Path dFP = getDataPath();
			if (!Files.isDirectory(dFP)) try {
				Files.createDirectory(dFP);
			} catch (IOException e) {
				e.printStackTrace();
			}
			compatible = true;
			LOG.debug("Your server (NMS version " + nmsver + ") is compatible with AntiLaby!");
		} else {
			compatible = false;
			LOG.error("Your server is not compatible with AntiLaby!");
			disableIfNotCompatible();
		}
		// Try to update AntiLaby
		new UpdateManager().run();
		// Init files, commands and events
		initConfig();
		// Register plug-in channels
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, Constants.LABYMOD_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, Constants.LABYMOD_CHANNEL, new IncomingPluginChannel());
		// If HLS is installed, use HLS
		if (Bukkit.getPluginManager().isPluginEnabled("HeisluftsLanguageSystem")) {
			HLSCompat.init();
			loadedFeatures.add(PluginFeature.HEISLUFTS_LANGUAGE_SYSTEM);
		} else {
			// Init ProtocolLib support
			if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
				ProtocolLibSupport.init();
				loadedFeatures.add(PluginFeature.PROTOCOL_LIB);
			} else if (version > 8)
				LOG.debug("ProtocolLib is not installed, falling back to possibly inaccurate legacy implementation.");
			else {
				LOG.debug("ProtocolLib is not installed and version is < 1.9, using reflection to get locale...");
				before19 = true;
			}
		}
		initCmds();
		initEvents();
		// Load data
		DataManager.loadData();
		// Start plug-in metrics for MCStats.org
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (final IOException e) {
			LOG.error(e.getMessage());
		}
		// Init LanguageManager
		try {
			LanguageManager.INSTANCE.initAL();
		} catch (IOException e) {
			e.printStackTrace();
			LOG.error("Could not convert language folder! ");
		}
		// Start plug-in metrics for bStats.org
		initBMetrics();
		// Resend AntiLaby packages (on reload)
		for (final Player all : Bukkit.getOnlinePlayers()) {
			new AntiLabyPackager(all).sendPackages();
		}
		LOG.info("Enabled AntiLaby by the AntiLaby Team version " + getDescription().getVersion() + " sucsessfully!");
	}

	/**
	 * Disables the plug-in if not compatible
	 */
	public void disableIfNotCompatible() {
		if (!compatible) getPluginLoader().disablePlugin(this);
	}

	private void initConfig() {
		new InitConfig(this).init();
	}

	/**
	 * Initializes and registers the AntiLaby commands
	 */
	private void initCmds() {
		new AntiLabyCommand();
		new LabyInfoCommand();
	}

	/**
	 * Initializes and registers the EventListeners
	 */
	private void initEvents() {
		final PluginManager pm = Bukkit.getPluginManager();
		if (!before19) pm.registerEvents(new EventsPost18(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new IncomingPluginChannel(), this);
	}

	/**
	 * Initializes the <a href="https://bstats.org/plugin/bukkit/AntiLaby">BStats</a> Metrics
	 */
	private void initBMetrics() {
		// Start plug-in metrics for bStats.org
		final BStats bstats = new BStats(this);
		bstats.addCustomChart(
				new BStats.SimplePie("autoupdate_enabled", () -> String.valueOf(Config.isAutoUpdateEnabled())));
		bstats.addCustomChart(
				new BStats.SimplePie("is_bungee_server", () -> String.valueOf(ServerHelper.isBungeeCord())));
		bstats.addCustomChart(
				new BStats.SimplePie("bypass_enabled", () -> String.valueOf(Config.getEnableBypassWithPermission())));
		bstats.addCustomChart(
				new BStats.SimplePie("kick_enabled", () -> String.valueOf(Config.getLabyModPlayerKickEnable())));
		bstats.addCustomChart(new BStats.SimpleBarChart("disabled_functions", () -> {
			final Map<String, Integer> valueMap = new HashMap<>();
			final int food = Miscellaneous.boolToInt(Config.getFOOD());
			final int gui = Miscellaneous.boolToInt(Config.getGUI());
			final int nick = Miscellaneous.boolToInt(Config.getNICK());
			final int blockBuild = Miscellaneous.boolToInt(Config.getBLOCKBUILD());
			final int chat = Miscellaneous.boolToInt(Config.getCHAT());
			final int extras = Miscellaneous.boolToInt(Config.getEXTRAS());
			final int animations = Miscellaneous.boolToInt(Config.getANIMATIONS());
			final int potions = Miscellaneous.boolToInt(Config.getPOTIONS());
			final int armor = Miscellaneous.boolToInt(Config.getARMOR());
			final int damageIndicator = Miscellaneous.boolToInt(Config.getDAMAGEINDICATOR());
			final int minimapRadar = Miscellaneous.boolToInt(Config.getMINIMAP_RADAR());
			valueMap.put("FOOD", food);
			valueMap.put("GUI", gui);
			valueMap.put("NICK", nick);
			valueMap.put("BLOCKBUILD", blockBuild);
			valueMap.put("CHAT", chat);
			valueMap.put("EXTRAS", extras);
			valueMap.put("ANIMATIONS", animations);
			valueMap.put("POTIONS", potions);
			valueMap.put("ARMOR", armor);
			valueMap.put("DAMAGEINDICATOR", damageIndicator);
			valueMap.put("MINIMAP_RADAR", minimapRadar);
			return valueMap;
		}));
		bstats.addCustomChart(new BStats.MultiLineChart("players_with_labymod_count", () -> {
			final Map<String, Integer> valueMap = new HashMap<>();
			valueMap.put("players_lm", IncomingPluginChannel.getLabyModPlayers().size());
			valueMap.put("players_no_lm",
					Bukkit.getOnlinePlayers().size() - IncomingPluginChannel.getLabyModPlayers().size());
			return valueMap;
		}));
		bstats.addCustomChart(new BStats.SingleLineChart("players_with_labymod_count_single",
				() -> IncomingPluginChannel.getLabyModPlayers().size()));
		final List<String> labyModJoinCommands = configReader.getLabyModPlayerAction().getJoinCommands(false);
		bstats.addCustomChart(new BStats.SimplePie("lmjoincmd_enabled", () -> {
			if (labyModJoinCommands.isEmpty()) return "false";
			else return "true";
		}));
		bstats.addCustomChart(
				new BStats.SingleLineChart("lmjoincmd_count", () -> labyModJoinCommands.size()));
	}

	@Override
	public void onLoad() {
		if (ServerHelper.getImplementation() == ServerHelper.ImplementationType.GLOWSTONE) {
			LOG.error("Glowstone is not yet supported");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		instance = this;
		versionType = VersionType.fromName(getDescription().getVersion().toLowerCase());
	}
}