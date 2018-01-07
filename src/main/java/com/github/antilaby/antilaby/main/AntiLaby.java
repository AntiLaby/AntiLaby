package com.github.antilaby.antilaby.main;

import com.github.antilaby.antilaby.api.BooleanIntConversion;
import com.github.antilaby.antilaby.api.LabyModJoinCommands;
import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.api.spigot.BungeeChecker;
import com.github.antilaby.antilaby.api.updater.VersionType;
import com.github.antilaby.antilaby.command.AntiLabyCommand;
import com.github.antilaby.antilaby.command.AntiLabyTabComplete;
import com.github.antilaby.antilaby.compat.PluginFeature;
import com.github.antilaby.antilaby.compat.ProtocolLibSupport;
import com.github.antilaby.antilaby.config.Config;
import com.github.antilaby.antilaby.config.ConfigFile;
import com.github.antilaby.antilaby.config.InitConfig;
import com.github.antilaby.antilaby.events.EventsPost18;
import com.github.antilaby.antilaby.events.PlayerJoin;
import com.github.antilaby.antilaby.features.labyinfo.DataManager;
import com.github.antilaby.antilaby.features.labyinfo.LabyInfoCommand;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.metrics.BStats;
import com.github.antilaby.antilaby.metrics.Metrics;
import com.github.antilaby.antilaby.util.NmsUtils;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.antilaby.antilaby.update.UpdateDownloader;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class of AntiLaby Spigot plugin
 *
 * @author NathanNr
 */
public class AntiLaby extends JavaPlugin {

	public static final Logger LOG = new Logger("Main");
	private static AntiLaby instance;

	public static AntiLaby getInstance() {
		return instance;
	}

	private final List<PluginFeature> loadedFeatures = new ArrayList<>(PluginFeature.values().length);
	// Compatible?
	private boolean compatible;
	// MCStats.org Metrics
	private Metrics metrics;
	// Is this a beta version?
	private VersionType versionType;
	// Is the server part of a BungeeCord network?
	private BungeeChecker bungeeChecker;

	private UpdateDownloader ud;

	private boolean before19 = false;

	public boolean isPrior19() {
		return before19;
	}

	/**
	 * Disables the desired {@link PluginFeature}
	 *
	 * @param feature
	 * 		The PluginFeature to disable
	 */
	public void disableFeature(PluginFeature feature) {
		loadedFeatures.remove(feature);
	}

	/**
	 * Enables the desired {@link PluginFeature}
	 *
	 * @param feature
	 * 		The PluginFeature to enable
	 */
	public void enableFeature(PluginFeature feature) {
		if(!loadedFeatures.contains(feature)) loadedFeatures.add(feature);
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
	 * Returns whether a given {@link PluginFeature} is enabled
	 *
	 * @param feature
	 * 		The requested feature
	 *
	 * @return whether the requested feature is enabled
	 */
	public boolean isSupportEnabled(PluginFeature feature) {
		return loadedFeatures.contains(feature);
	}

	@Override
	public void onDisable() {
		// Kill update task if it's running
		if(ud != null) ud.interrupt();
		// Save Data
		DataManager.saveData();
		LOG.info("Disabled AntiLaby by the AntiLaby Team version " + getDescription().getVersion() + " successfully!");
	}

	@Override
	public void onEnable() {
		// Delete datamanager file on exit
		Runtime.getRuntime().addShutdownHook(new Thread(DataManager::cleanup, "AntiLabyCleanup"));
		// Check if the server is compatible with AntiLaby
		final String nmsver = NmsUtils.getVersion();
		int version = 0;
		try {
			version = Integer.parseInt(nmsver.split("_")[1]);
		} catch(final NumberFormatException e) {
			LOG.fatal("Unknown NMS version (" + nmsver + ')');
			compatible = false;
			disableIfNotCompatible();
		}
		if(version >= 8) {
			// Ensure the DataFolder exists
			getDataFolder().mkdir();
			compatible = true;
			LOG.debug("Your server (NMS version " + nmsver + ") is compatible with AntiLaby!");
		} else {
			compatible = false;
			LOG.error("Your server is not compatible with this version of AntiLaby! Your NMS-version is \"" + nmsver + "\", which was released before the first LabyMod version.");
			disableIfNotCompatible();
		}
		// Try to update AntiLaby
		update();
		// Init files, commands and events
		initConfig();
		// Register plug-in channels
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, Constants.LABYMOD_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, Constants.LABYMOD_CHANNEL, new IncomingPluginChannel());
		// Init ProtocolLib support
		if(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) ProtocolLibSupport.init();
		else if(version > 8)
			LOG.debug("ProtocolLib is not installed, falling back to possibly inaccurate legacy implementation.");
		else {
			LOG.debug("ProtocolLib is not installed and version is < 1.9, using reflection to get locale...");
			before19 = true;
		}
		initCmds();
		initEvents();
		// Load data
		DataManager.loadData();
		// Is the server part of a BungeeCord network?
		bungeeChecker = new BungeeChecker();
		bungeeChecker.init();
		// Start plug-in metrics for MCStats.org
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch(final IOException e) {
			LOG.error(e.getMessage());
		}
		// Start plug-in metrics for bStats.org
		initBMetrics();
		// Init LanguageManager
		final LanguageManager lang = LanguageManager.INSTANCE;
		if(!lang.isInit()) lang.init();
		// Resend AntiLaby packages (on reload)
		for(final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		LOG.info("Enabled AntiLaby by the AntiLaby Team version " + getDescription().getVersion() + " sucsessfully!");
	}

	/**
	 * Disables the plug-in if not compatible
	 */
	public void disableIfNotCompatible() {
		if(!compatible) getPluginLoader().disablePlugin(this);
	}

	private void update() {
		if(versionType.equals(VersionType.RELEASE)) {
			if(getConfig().getBoolean("AntiLaby.Update.AutoUpdate")) {
				// Check and install updates async
				ud = new UpdateDownloader();
				ud.start();
			} else
				LOG.info("You have disabled auto-update in the config file. You can get newer versions of AntiLaby " + "manually" + " from here: https://www.spigotmc.org/resources/" + Constants.RESOURCE_ID + "/!");
		} else {
			LOG.info("You are running a " + versionType.toString() + " version! Auto-update is not available. You can update manually: " + Constants.RESOURCE_LINK);
			disableIfNotCompatible();
		}
	}

	private void initConfig() {
		new InitConfig(this).init();
	}

	/**
	 * Initializes and registers the AntiLaby commands
	 */
	private void initCmds() {
		getCommand("antilaby").setExecutor(new AntiLabyCommand(this));
		getCommand("antilaby").setTabCompleter(new AntiLabyTabComplete());
		getCommand("labyinfo").setExecutor(new LabyInfoCommand());
	}

	/**
	 * Initializes and registers the EventListeners
	 */
	private void initEvents() {
		final PluginManager pm = Bukkit.getPluginManager();
		if(!before19) pm.registerEvents(new EventsPost18(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new IncomingPluginChannel(), this);
	}

	/**
	 * Initializes the <a href="https://bstats.org/plugin/bukkit/AntiLaby">BStats</a> Metrics
	 */
	private void initBMetrics() {
		// Start plug-in metrics for bStats.org
		final BStats bstats = new BStats(this);
		bstats.addCustomChart(new BStats.SimplePie("autoupdate_enabled", () -> String.valueOf(Config.isAutoUpdateEnabled())));
		bstats.addCustomChart(new BStats.SimplePie("is_bungee_server", () -> String.valueOf(bungeeChecker.isBungeecord())));
		bstats.addCustomChart(new BStats.SimplePie("bypass_enabled", () -> String.valueOf(Config.getEnableBypassWithPermission())));
		bstats.addCustomChart(new BStats.SimplePie("kick_enabled", () -> String.valueOf(Config.getLabyModPlayerKickEnable())));
		bstats.addCustomChart(new BStats.SimpleBarChart("disabled_functions", () -> {
			final Map<String, Integer> valueMap = new HashMap<>();
			final int food = BooleanIntConversion.convert(Config.getFOOD());
			final int gui = BooleanIntConversion.convert(Config.getGUI());
			final int nick = BooleanIntConversion.convert(Config.getNICK());
			final int blockBuild = BooleanIntConversion.convert(Config.getBLOCKBUILD());
			final int chat = BooleanIntConversion.convert(Config.getCHAT());
			final int extras = BooleanIntConversion.convert(Config.getEXTRAS());
			final int animations = BooleanIntConversion.convert(Config.getANIMATIONS());
			final int potions = BooleanIntConversion.convert(Config.getPOTIONS());
			final int armor = BooleanIntConversion.convert(Config.getARMOR());
			final int damageIndicator = BooleanIntConversion.convert(Config.getDAMAGEINDICATOR());
			final int minimapRadar = BooleanIntConversion.convert(Config.getMINIMAP_RADAR());
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
			valueMap.put("players_no_lm", Bukkit.getOnlinePlayers().size() - IncomingPluginChannel.getLabyModPlayers().size());
			return valueMap;
		}));
		bstats.addCustomChart(new BStats.SingleLineChart("players_with_labymod_count_single", () -> IncomingPluginChannel.getLabyModPlayers().size()));
		final LabyModJoinCommands lmjc = new LabyModJoinCommands();
		bstats.addCustomChart(new BStats.SimplePie("lmjoincmd_enabled", () -> {
			if(lmjc.getLabyModJoinCommands(false).isEmpty()) return "false";
			else return "true";
		}));
		bstats.addCustomChart(new BStats.SingleLineChart("lmjoincmd_count", () -> lmjc.getLabyModJoinCommands(false).size()));
	}

	@Override
	public void onLoad() {
		instance = this;
		versionType = VersionType.fromName(getDescription().getVersion().toLowerCase());
	}

	public void reloadPlugin(CommandSender sender) {
		// Reload the plug-in
		if(sender instanceof Player) {
			final Player player = (Player) sender;
			if(!player.hasPermission("antilaby.reload")) {
				player.sendMessage(LanguageManager.INSTANCE.translate("antilaby.command.noPermission", player));
				LOG.info("Player " + player.getName() + " (" + player.getUniqueId() + ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if(sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Constants.PREFIX + "§aReloading AntiLaby...§r");
			LOG.info(player.getName() + " (" + player.getUniqueId() + "): Reloading AntiLaby...");
		} else LOG.info("Reloading AntiLaby...");
		ConfigFile.reloadFile();
		for(final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		if(sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Constants.PREFIX + "§aReload complete!§r");
			LOG.info(player.getName() + " (" + player.getUniqueId() + "): Reload complete!");
		} else LOG.info("Reload complete!");
	}

	public void sendInfo(CommandSender sender) {
		// Send information about this plug-in to a command sender (console /
		// player)
		sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + getDescription().getVersion() + " by the AntiLaby Team" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "More information about the plugin: " + Constants.RESOURCE_LINK + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/antilaby reload' to reload the plugin." + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/labyinfo <player>' to check if a player uses LabyMod." + ChatColor.RESET);
		sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}

}