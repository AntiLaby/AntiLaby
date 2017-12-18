package com.github.antilaby.antilaby.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.antilaby.antilaby.api.BooleanIntConversion;
import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.command.AntiLabyCommand;
import com.github.antilaby.antilaby.command.AntiLabyTabComplete;
import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.compat.ProtocolLibSupport;
import com.github.antilaby.antilaby.config.Config;
import com.github.antilaby.antilaby.config.ConfigFile;
import com.github.antilaby.antilaby.config.InitConfig;
import com.github.antilaby.antilaby.events.PlayerJoin;
import com.github.antilaby.antilaby.features.labyinfo.DataManager;
import com.github.antilaby.antilaby.features.labyinfo.LabyInfoCommand;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.metrics.BStats;
import com.github.antilaby.antilaby.metrics.Metrics;
import com.github.antilaby.antilaby.nms.NmsTools;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.antilaby.antilaby.update.UpdateDownloader;
import com.github.antilaby.antilaby.update.VersionType;
import com.github.antilaby.antilaby.util.Constants;

/**
 * Main class of AntiLaby Spigot plugin
 *
 * @author NathanNr
 */
public class AntiLaby extends JavaPlugin {
	
	private static AntiLaby instance;
	public static final Logger LOG = new Logger("Main");
	
	public static AntiLaby getInstance() {
		return instance;
	}
	
	// Compatible?
	private boolean compatible;
	// MCStats.org Metrics
	private Metrics metrics;
	
	private final List<PluginFeature> loadedFeatures = new ArrayList<>(PluginFeature.values().length);

	// Is this a beta version?
	private VersionType versionType;
	
	private UpdateDownloader ud;
	
	/**
	 * Disables the desired {@link PluginFeature}
	 *
	 * @param feature
	 *            The PluginFeature to disable
	 * @author heisluft
	 */
	public void disableFeature(PluginFeature feature) {
		loadedFeatures.remove(feature);
	}

	/**
	 * Disables the plugin if not compatible
	 */
	public void disableIfNotCompatible() {
		if (!compatible) getPluginLoader().disablePlugin(this);
	}
	
	/**
	 * Enables the desired {@link PluginFeature}
	 *
	 * @param feature
	 *            The PluginFeature to enable
	 * @author heisluft
	 */
	public void enableFeature(PluginFeature feature) {
		if (!loadedFeatures.contains(feature)) loadedFeatures.add(feature);
	}
	
	@Override
	public File getFile() {
		return super.getFile();
	}

	/**
	 * Returns the <a href="http://mcstats.org/plugin/Antilaby">MCStats</a> Metrics
	 */
	public Metrics getMetrics() {
		return metrics;
	}
	
	public VersionType getVersionType() {
		return versionType;
	}

	/**
	 * Initializes the
	 * <a href="https://bstats.org/plugin/bukkit/AntiLaby">BStats</a> Metrics
	 */
	public void initBMetrics() {
		// Start plugin metrics for bStats.org
		final BStats bstats = new BStats(this);
		bstats.addCustomChart(new BStats.SimplePie("autoupdate_enabled", () -> {
			return String.valueOf(Config.isAutoUpdateEnabled());
		}));
		bstats.addCustomChart(new BStats.SimplePie("bypass_enabled", () -> {
			return String.valueOf(Config.getEnableBypassWithPermission());
		}));
		bstats.addCustomChart(new BStats.SimplePie("kick_enabled", () -> {
			return String.valueOf(Config.getLabyModPlayerKickEnable());
		}));
		bstats.addCustomChart(new BStats.SimpleBarChart("disabled_functions", () -> {
			final Map<String, Integer> valueMap = new HashMap<>();
			final int FOOD = BooleanIntConversion.convert(Config.getFOOD());
			final int GUI = BooleanIntConversion.convert(Config.getGUI());
			final int NICK = BooleanIntConversion.convert(Config.getNICK());
			final int BLOCKBUILD = BooleanIntConversion.convert(Config.getBLOCKBUILD());
			final int CHAT = BooleanIntConversion.convert(Config.getCHAT());
			final int EXTRAS = BooleanIntConversion.convert(Config.getEXTRAS());
			final int ANIMATIONS = BooleanIntConversion.convert(Config.getANIMATIONS());
			final int POTIONS = BooleanIntConversion.convert(Config.getPOTIONS());
			final int ARMOR = BooleanIntConversion.convert(Config.getARMOR());
			final int DAMAGEINDICATOR = BooleanIntConversion.convert(Config.getDAMAGEINDICATOR());
			final int MINIMAP_RADAR = BooleanIntConversion.convert(Config.getMINIMAP_RADAR());
			valueMap.put("FOOD", FOOD);
			valueMap.put("GUI", GUI);
			valueMap.put("NICK", NICK);
			valueMap.put("BLOCKBUILD", BLOCKBUILD);
			valueMap.put("CHAT", CHAT);
			valueMap.put("EXTRAS", EXTRAS);
			valueMap.put("ANIMATIONS", ANIMATIONS);
			valueMap.put("POTIONS", POTIONS);
			valueMap.put("ARMOR", ARMOR);
			valueMap.put("DAMAGEINDICATOR", DAMAGEINDICATOR);
			valueMap.put("MINIMAP_RADAR", MINIMAP_RADAR);
			return valueMap;
		}));

		bstats.addCustomChart(new BStats.MultiLineChart("players_with_labymod", () -> {
			final Map<String, Integer> valueMap = new HashMap<>();
			valueMap.put("players_lm", IncomingPluginChannel.getLabyModPlayers().size());
			valueMap.put("players_no_lm",
					Bukkit.getOnlinePlayers().size() - IncomingPluginChannel.getLabyModPlayers().size());
			return valueMap;
		}));
	}
	
	/**
	 * Initializes and registers the AntiLaby commands
	 */
	private void initCmds() {
		getCommand("antilaby").setExecutor(new AntiLabyCommand(this));
		getCommand("antilaby").setTabCompleter(new AntiLabyTabComplete());
		getCommand("labyinfo").setExecutor(new LabyInfoCommand());
	}
	
	private void initConfig() {
		new InitConfig(getInstance()).init();
	}
	
	/**
	 * Initializes and registers the EventListeners
	 */
	private void initEvents() {
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new IncomingPluginChannel(), this);
	}
	
	/**
	 * Returns whether a given {@link PluginFeature} is enabled
	 *
	 * @param feature
	 *            The requested feature
	 * @return whether the requested feature is enabled
	 * @author heisluft
	 */
	public boolean isSupportEnabled(PluginFeature feature) {
		return loadedFeatures.contains(feature);
	}
	
	@Override
	public void onDisable() {
		// Kill update task if it's running
		if (ud != null) ud.interrupt();
		// Save Data
		DataManager.saveData();
		LOG.info("Disabled AntiLaby by the AntiLaby Team version " + getDescription().getVersion() + " sucsessfully!");
	}
	
	@Override
	public void onEnable() {
		// Delete datamanager file on exit
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			DataManager.cleanup();
		}, "AntiLabyCleanup"));
		// Check if the server is compatible with AntiLaby
		final String nmsver = NmsTools.getVersion();
		int version = 0;
		try {
			version = Integer.parseInt(nmsver.split("_")[1]);
		} catch (final NumberFormatException e) {
			LOG.fatal("Unknown NMS version (" + nmsver + ")");
			compatible = false;
			disableIfNotCompatible();
		}
		if (version >= 8) {
			// Ensure the DataFolder exists
			getDataFolder().mkdir();
			compatible = true;
			LOG.info("Your server (NMS version " + nmsver + ") is compatible with AntiLaby!");
		} else {
			compatible = false;
			LOG.error("Your server is not compatible with this version of AntiLaby! Your NMS-version is \"" + nmsver
					+ "\", which was released before the first LabyMod version.");
			disableIfNotCompatible();
		}
		// Try to update AntiLaby
		update();
		// Init files, commands and events
		initConfig();
		// Register plugin channels
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, Constants.LABYMOD_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, Constants.LABYMOD_CHANNEL,
				new IncomingPluginChannel());
		// Init ProtocolLib support
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) ProtocolLibSupport.init();
		else LOG.info("ProtocolLib is not installed, falling back to possibly inaccurate legacy implementation.");
		initCmds();
		initEvents();
		// Load data
		DataManager.loadData();
		// Start plugin metrics for MCStats.org
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch (final IOException e) {}
		// Start plugin metrics for bStats.org
		initBMetrics();
		// Init LanguageManager
		final LanguageManager lang = LanguageManager.INSTANCE;
		if (!lang.isInit()) lang.init();
		// Resend AntiLaby packages (on reload)
		for (final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		LOG.info("Enabled AntiLaby by the AntiLaby Team version " + getDescription().getVersion() + " sucsessfully!");
	}
	
	@Override
	public void onLoad() {
		instance = this;
		versionType = VersionType.fromName(getDescription().getVersion().toLowerCase());
	}
	
	public void reloadPlugin(CommandSender sender) {
		// Reload the plugin
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("antilaby.reload")) {
				player.sendMessage(LanguageManager.INSTANCE.translate("antilaby.command.noPermission", player));
				LOG.info("Player " + player.getName() + " (" + player.getUniqueId()
						+ ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Constants.PREFIX + "§aReloading AntiLaby...§r");
			LOG.info(player.getName() + " (" + player.getUniqueId() + "): Reloading AntiLaby...");
		} else LOG.info("Reloading AntiLaby...");
		ConfigFile.reloadFile();
		for (final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Constants.PREFIX + "§aReload complete!§r");
			LOG.info(player.getName() + " (" + player.getUniqueId() + "): Reload complete!");
		} else LOG.info("Reload complete!");
	}
	
	public void sendInfo(CommandSender sender) {
		// Send information about this plugin to a command sender (console /
		// player)
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + getDescription().getVersion()
				+ " by the AntiLaby Team" + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "More information about the plugin: " + Constants.RESOURCE_LINK + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/antilaby reload' to reload the plugin." + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "Use '/labyinfo <player>' to check if a player uses LabyMod." + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}
	
	private void update() {
		if (getVersionType().equals(VersionType.RELEASE)) {
			if (getConfig().getBoolean("AntiLaby.Update.AutoUpdate")) {
				// Check and install updates async
				ud = new UpdateDownloader();
				ud.start();
			} else LOG.info(
					"You have disabled auto-update in the config file. You can get newer versions of AntiLaby manually from here: https://www.spigotmc.org/resources/"
							+ Constants.RESOURCE_ID + "/!");
		} else {
			LOG.info("You are running a " + getVersionType().toString()
					+ " version! Auto-update is not available. You can update manually: " + Constants.RESOURCE_LINK);
			disableIfNotCompatible();
		}
	}
	
}