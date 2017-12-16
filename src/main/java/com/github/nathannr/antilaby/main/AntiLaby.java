package com.github.nathannr.antilaby.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.api.BooleanIntConversion;
import com.github.nathannr.antilaby.api.LabyModFeature;
import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.nathannr.antilaby.command.AntiLabyCommand;
import com.github.nathannr.antilaby.command.AntiLabyTabComplete;
import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.config.ConfigFile;
import com.github.nathannr.antilaby.config.InitConfig;
import com.github.nathannr.antilaby.events.PlayerJoin;
import com.github.nathannr.antilaby.features.labyinfo.DataManager;
import com.github.nathannr.antilaby.features.labyinfo.LabyInfoCommand;
import com.github.nathannr.antilaby.features.labyinfo.Players;
import com.github.nathannr.antilaby.messagemanager.MultiLanguage;
import com.github.nathannr.antilaby.metrics.BStats;
import com.github.nathannr.antilaby.metrics.Metrics;
import com.github.nathannr.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.nathannr.antilaby.update.UpdateDownloader;
import com.github.nathannr.antilaby.update.VersionType;
import com.github.nathannr.antilaby.util.PluginChannel;
import com.github.nathannr.antilaby.util.Prefix;
import com.github.nathannr.antilaby.util.Resource;

import de.heisluft.antilaby.compat.ProtocolLibSupport;
import de.heisluft.antilaby.lang.impl.LanguageManager;
import de.heisluft.antilaby.log.Logger;
import de.heisluft.antilaby.util.Strings;

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

	// NMS-version
	private String nmsver;
	// Compatible?
	private boolean compatible;
	// MCStats.org Metrics
	private Metrics metrics;

	private final List<PluginFeature> loadedFeatures = new ArrayList<>(1);
	
	// Is this a beta version?
	private VersionType versionType;

	// MultiLanguage
	private MultiLanguage multiLanguage;

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
	
	// Disable the plugin if not compatible
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

	public Metrics getMetrics() {
		return metrics;
	}

	public MultiLanguage getMultiLanguage() {
		return multiLanguage;
	}
	
	public String getNmsver() {
		return nmsver;
	}

	public VersionType getVersionType() {
		return versionType;
	}

	public void initBMetrics() {
		// Start plugin metrics for bStats.org
		final BStats bstats = new BStats(this);
		bstats.addCustomChart(new BStats.SimplePie("autoupdate_enabled", new Callable<String>() {
			// Is AutoUpdate enabled?
			@Override
			public String call() {
				final String r = AntiLaby.getInstance().getConfig().getString("AntiLaby.Update.AutoUpdate");
				return Strings.equalsFallback(r, "true", true, "false").toLowerCase();
			}
		}));
		bstats.addCustomChart(new BStats.SimplePie("bypass_enabled", new Callable<String>() {
			// Bypass with permission enabled?
			@Override
			public String call() {
				final String r = AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission");
				return Strings.equalsFallback(r, "true", true, "false").toLowerCase();
			}
		}));
		bstats.addCustomChart(new BStats.SimplePie("kick_enabled", new Callable<String>() { 
			// LabyMod player kick enabled?
			@Override
			public String call() {
				final boolean b = Config.getLabyModPlayerKickEnable();
				if (!String.valueOf(b).equals("null")) return String.valueOf(b);
				else return "false";
			}
		}));
		bstats.addCustomChart(new BStats.MultiLineChart("players_with_labymod", new Callable<Map<String, Integer>>() {
			// TODO: Add it to bStats.org if it's available
			// Players with and without LabyMod in a MultiLineChart
	        @Override
	        public Map<String, Integer> call() throws Exception {
	            Map<String, Integer> valueMap = new HashMap<>();
	            valueMap.put("players_lm", Players.getLabyModPlayerCount());
	            valueMap.put("players_no_lm", Players.getNoLabyModPlayerCount());
	            int playersUnknown = Bukkit.getOnlinePlayers().size() - (Players.getLabyModPlayerCount() + Players.getNoLabyModPlayerCount());
	            valueMap.put("players_unknown", playersUnknown);
	            return valueMap;
	        }
	    }));
		bstats.addCustomChart(new BStats.SimpleBarChart("disabled_functions", new Callable<Map<String, Integer>>() {
			// TODO: Add it to bStats.org if it's available
			// TODO: Add all new functions
			// Disabled functions
			@Override
			public Map<String, Integer> call() {
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
			}
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
		DataManager.saveData();
		LOG.log(Level.INFO, "Disabled AntiLaby by NathanNr and heisluft version " + getDescription().getVersion()
				+ " sucsessfully!");
	}

	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) ProtocolLibSupport.init();
		else LOG.log(Level.INFO,
				"ProtocolLib is not installed, falling back to possibly inaccurate legacy implementation.");
		// Get NMS-version
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		LOG.log(Level.INFO, "Your NMS-version: " + nmsver);
		// Check if the server is compatible with AntiLaby
		int version = 0;
		try {
			version = Integer.parseInt(nmsver.split("_")[1]);
		} catch (final NumberFormatException e) {
			LOG.log(Level.WARN, "Unknown NMS version");
			compatible = false;
			disableIfNotCompatible();
		}
		if (version >= 8) {
			// TODO: Dont't forget to update this after adding a new
			// NMS-version!
			compatible = true;
			LOG.log(Level.INFO, "Your server is compatible with AntiLaby!");
			try {
				final FileWriter fw = new FileWriter(getDataFolder() + "/info.txt");
				final BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by NathanNr, version " + getDescription().getVersion() + "");
				bw.newLine();
				bw.write("Disable LabyMod functions! Bukkit, Spigot + BungeeCord support");
				bw.newLine();
				bw.newLine();
				bw.write("Date / time: " + new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date()));
				bw.newLine();
				bw.write("NMS-version: " + nmsver);
				bw.newLine();
				bw.newLine();
				bw.write("Commands:");
				bw.newLine();
				bw.write(
						" - /antilaby <info|reload>: Information about the plugin or reload the plugin, required permission to reload 'antilaby.reload'");
				bw.newLine();
				bw.write(
						" - /labyinfo <player>: Check if a player uses LabyMod, required permission: 'antilaby.labyinfo'");
				bw.newLine();
				bw.newLine();
				bw.write("Other permission:");
				bw.newLine();
				bw.write(" - antilaby.bypasscommands: Allows you to bypass the join commands if you are using LabyMod");
				bw.newLine();
				bw.newLine();
				bw.write("Find more information about AntiLaby and how to use it: " + Resource.RESOURCE_LINK);
				bw.close();
			} catch (final IOException e) {}
		} else {
			compatible = false;
			LOG.log(Level.ERROR, "Your server is not compatible with this version of AntiLaby! Your NMS-version is \""
					+ nmsver + "\", which was released before the first LabyMod version.");
			disableIfNotCompatible();
		}
		update();
		// Init files, commands and events
		initConfig();
		// Register plugin channels
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, PluginChannel.LABYMOD_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, PluginChannel.LABYMOD_CHANNEL,
				new IncomingPluginChannel());
		initCmds();
		initEvents();
		DataManager.loadData();
		// Start plugin metrics for MCStats.org
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch (final IOException e) {}
		// Start plugin metrics for bStats.org
		initBMetrics();
		// Init MultiLanguage system
		multiLanguage = new MultiLanguage(getInstance(), Prefix.CPREFIXINFO);
		// Resend AntiLaby packages (on reload)
		final LanguageManager lang = LanguageManager.INSTANCE;
		if (!lang.isInit()) lang.init();
		for (final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		LOG.log(Level.INFO, "Enabled AntiLaby by NathanNr and heisluft version " + getDescription().getVersion()
				+ " sucsessfully!");
	}

	@Override
	public void onLoad() {
		instance = this;
		versionType = VersionType.fromName(getDescription().getVersion().toLowerCase());
		LabyInfoCommand.setCommandAvailability();
	}

	public void reloadPlugin(CommandSender sender) {
		// Reload the plugin
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("antilaby.reload")) {
				player.sendMessage(multiLanguage.getMultiLanguageMessage(player, "NoPermission", true));
				LOG.log(Level.INFO, "Player " + player.getName() + " (" + player.getUniqueId()
						+ ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Prefix.PREFIX + "§aReloading AntiLaby...§r");
			LOG.log(Level.INFO, player.getName() + " (" + player.getUniqueId() + "): Reloading AntiLaby...");
		} else sender.sendMessage(Prefix.CPREFIXINFO + "Reloading AntiLaby...");
		ConfigFile.reloadFile();
		for (final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		LabyInfoCommand.setCommandAvailability();
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Prefix.PREFIX + "§aReload complete!§r");
			LOG.log(Level.INFO, player.getName() + " (" + player.getUniqueId() + "): Reload complete!");
		} else LOG.log(Level.INFO, "Reload complete!");
	}

	public void sendInfo(CommandSender sender) {
		// Send information about this plugin to a command sender (console /
		// player)
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + getDescription().getVersion()
				+ " by NathanNr and heisluft" + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "More information about the plugin: " + Resource.RESOURCE_LINK + ChatColor.RESET);
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
			} else LOG.log(Level.INFO,
					"You have disabled auto-update in the config file. You can get newer versions of AntiLaby manually from here: https://www.spigotmc.org/resources/"
							+ Resource.RESOURCE_ID + "/!");
		} else {
			LOG.log(Level.INFO, "You are running a " + getVersionType().toString().toLowerCase()
					+ " version! Auto-update is not available. You can update manually: " + Resource.RESOURCE_LINK);
			disableIfNotCompatible();
		}
	}

}