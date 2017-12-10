package com.github.nathannr.antilaby.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.nathannr.antilaby.command.AntiLabyCommand;
import com.github.nathannr.antilaby.command.AntiLabyTabComplete;
import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.config.ConfigFile;
import com.github.nathannr.antilaby.config.InitConfig;
import com.github.nathannr.antilaby.events.PlayerJoin;
import com.github.nathannr.antilaby.features.labyinfo.DataManager;
import com.github.nathannr.antilaby.features.labyinfo.LabyInfoCommand;
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

/**
 * Main class of AntiLaby Spigot plugin
 *
 * @author NathanNr
 */
public class AntiLaby extends JavaPlugin {
	
	private static AntiLaby instance;
	
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
	
	public void disableFeature(PluginFeature feature) {
		loadedFeatures.remove(feature);
	}

	// Disable the plugin if not compatible
	public void disableIfNotCompatible() {
		if (!compatible) getPluginLoader().disablePlugin(this);
	}

	public void enableFeature(PluginFeature feature) {
		if (!loadedFeatures.contains(feature)) loadedFeatures.add(feature);
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
	
	public File getPluginFile() {
		if (!(this instanceof JavaPlugin)) return null;
		try {
			final Method method = JavaPlugin.class.getDeclaredMethod("getFile");
			method.setAccessible(true);
			return (File) method.invoke(this);
		} catch (final ReflectiveOperationException e) {
			throw new RuntimeException("Could not get plugin file", e);
		}
	}
	
	public VersionType getVersionType() {
		return versionType;
	}
	
	public void initBMetrics() {
		// Start plugin metrics for bStats.org
		final BStats bstats = new BStats(this);
		bstats.addCustomChart(new BStats.SimplePie("autoupdate_enabled") {
			
			@Override
			public String getValue() {
				final String r = AntiLaby.getInstance().getConfig().getString("AntiLaby.Update.AutoUpdate");
				if (r.equalsIgnoreCase("true") || r.equalsIgnoreCase("false")) return r.toLowerCase();
				else return "false";
			}
		});
		bstats.addCustomChart(new BStats.SimplePie("bypass_enabled") {
			
			// Bypass with permission enabled?
			@Override
			public String getValue() {
				final String r = AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission");
				if (r.equalsIgnoreCase("true") || r.equalsIgnoreCase("false")) return r.toLowerCase();
				else return "false";
			}
		});
		bstats.addCustomChart(new BStats.SimplePie("kick_enabled") {
			
			// LabyMod player kick enabled?
			@Override
			public String getValue() {
				final boolean b = Config.getLabyModPlayerKickEnable();
				if (!String.valueOf(b).equals("null")) return String.valueOf(b);
				else return "false";
			}
		});
		// TODO: Change to bar charts
		/*
		 * bstats.addCustomChart(new BStats.AdvancedPie("disabled_functions") { //
		 * Disabled functions
		 *
		 * @Override public HashMap<String, Integer> getValues(HashMap<String, Integer>
		 * valueMap) { int FOOD = Boolean2Int.convert(Config.getFOOD()); int GUI =
		 * Boolean2Int.convert(Config.getGUI()); int NICK =
		 * Boolean2Int.convert(Config.getNICK()); int BLOCKBUILD =
		 * Boolean2Int.convert(Config.getBLOCKBUILD()); int CHAT =
		 * Boolean2Int.convert(Config.getCHAT()); int EXTRAS =
		 * Boolean2Int.convert(Config.getEXTRAS()); int ANIMATIONS =
		 * Boolean2Int.convert(Config.getANIMATIONS()); int POTIONS =
		 * Boolean2Int.convert(Config.getPOTIONS()); int ARMOR =
		 * Boolean2Int.convert(Config.getARMOR()); int DAMAGEINDICATOR =
		 * Boolean2Int.convert(Config.getDAMAGEINDICATOR()); int MINIMAP_RADAR =
		 * Boolean2Int.convert(Config.getMINIMAP_RADAR()); valueMap.put("FOOD", FOOD);
		 * valueMap.put("GUI", GUI); valueMap.put("NICK", NICK);
		 * valueMap.put("BLOCKBUILD", BLOCKBUILD); valueMap.put("CHAT", CHAT);
		 * valueMap.put("EXTRAS", EXTRAS); valueMap.put("ANIMATIONS", ANIMATIONS);
		 * valueMap.put("POTIONS", POTIONS); valueMap.put("ARMOR", ARMOR);
		 * valueMap.put("DAMAGEINDICATOR", DAMAGEINDICATOR);
		 * valueMap.put("MINIMAP_RADAR", MINIMAP_RADAR); return valueMap; } });
		 */
	}
	
	public void initCmds() {
		// Init /antilaby command
		getCommand("antilaby").setExecutor(new AntiLabyCommand(this));
		getCommand("antilaby").setTabCompleter(new AntiLabyTabComplete());
		getCommand("labyinfo").setExecutor(new LabyInfoCommand());
	}
	
	private void initConfig() {
		new InitConfig(getInstance()).init();
	}

	public void initEvents() {
		// Register PlayerJoinEvent to send AntiLaby packages on join
		final PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new IncomingPluginChannel(), this);
	}
	
	public boolean isSupportEnabled(PluginFeature feature) {
		return loadedFeatures.contains(feature);
	}
	
	@Override
	public void onDisable() {
		// Kill update task if it's running
		if (ud != null) ud.interrupt();
		DataManager.saveData();
		System.out.println("[AntiLaby/INFO] Disabled AntiLaby by NathanNr version " + getDescription().getVersion()
				+ " sucsessfully!");
	}
	
	@Override
	public void onEnable() {
		LanguageManager.initAll();
		while (!LanguageManager.isInit());
		if (Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) ProtocolLibSupport.init();
		
		// Get NMS-version
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		System.out.println(Prefix.CPREFIXINFO + "Your NMS-version: " + nmsver);
		// Check if the server is compatible with AntiLaby
		if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_8_R2")
				|| nmsver.equalsIgnoreCase("v1_8_R3") || nmsver.equalsIgnoreCase("v1_9_R1")
				|| nmsver.equalsIgnoreCase("v1_9_R2") || nmsver.equalsIgnoreCase("v1_10_R1")
				|| nmsver.equalsIgnoreCase("v1_11_R1") || nmsver.equalsIgnoreCase("v1_12_R1")) {
			// TODO: Dont't forget to update this after adding a new
			// NMS-version!
			compatible = true;
			System.out.println(Prefix.CPREFIXINFO + "Your server is compatible with AntiLaby!");
			try {
				final FileWriter fw = new FileWriter(getDataFolder() + "/info.txt");
				final BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by NathanNr, version " + getDescription().getVersion() + "");
				bw.newLine();
				bw.write("Disable LabyMod functions! Bukkit, Spigot + BungeeCord support");
				bw.newLine();
				bw.newLine();
				bw.write("Date / time: " + new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date())
						+ " (" + System.currentTimeMillis() + ")");
				bw.newLine();
				bw.write("NMS-version: " + nmsver);
				bw.newLine();
				bw.write("Compatibility: This version of AntiLaby is compatibly with your Minecraft version :)");
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
			System.err.println(Prefix.CPREFIXERROR
					+ "Your server is not compatible with this version of AntiLaby! Your NMS-version: '" + nmsver
					+ "', your AntiLaby version: '" + getDescription().getVersion() + "'. Look into the file '"
					+ "plugins/status.txt" + "' for more information!");
			try {
				final FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
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
				bw.write("Compatibility: This version of AntiLaby is NOT compatibly with your Minecraft version!");
				bw.newLine();
				bw.newLine();
				bw.write("Find more information about AntiLaby and how to use it: " + Resource.RESOURCE_LINK);
				bw.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
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
		multiLanguage = new MultiLanguage(getInstance(), Prefix.CPREFIXINFO, Resource.RESOURCE_ID);
		// Resend AntiLaby packages (on reload)
		for (final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		System.out.println("[AntiLaby/INFO] Enabled AntiLaby by NathanNr version " + getDescription().getVersion()
				+ " sucsessfully!");
	}
	
	@Override
	public void onLoad() {
		instance = this;
		if (getDescription().getVersion().toLowerCase().contains("pre")) versionType = VersionType.PRE_RELEASE;
		else if (getDescription().getVersion().toLowerCase().contains("beta")) versionType = VersionType.BETA;
		else if (getDescription().getVersion().toLowerCase().contains("alpha")) versionType = VersionType.ALPHA;
		else if (getDescription().getVersion().toLowerCase().contains("dev")) versionType = VersionType.DEV;
		else if (getDescription().getVersion().toLowerCase().contains("custom")) versionType = VersionType.CUSTOM;
		else versionType = VersionType.RELEASE;
		LabyInfoCommand.setCommandAvailability();
	}
	
	public void reloadPlugin(CommandSender sender) {
		// Reload the plugin
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (!player.hasPermission("antilaby.reload")) {
				player.sendMessage(multiLanguage.getMultiLanguageMessage(player, "NoPermission", true));
				System.out.println(Prefix.CPREFIXINFO + "Player " + player.getName() + " (" + player.getUniqueId()
						+ ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Prefix.PREFIX + "§aReloading AntiLaby...§r");
			System.out.println(
					Prefix.CPREFIXINFO + player.getName() + " (" + player.getUniqueId() + "): Reloading AntiLaby...");
		} else sender.sendMessage(Prefix.CPREFIXINFO + "Reloading AntiLaby...");
		ConfigFile.reloadFile();
		for (final Player all : Bukkit.getOnlinePlayers()) {
			final AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Prefix.PREFIX + "§aReload complete!§r");
			System.out.println(
					Prefix.CPREFIXINFO + player.getName() + " (" + player.getUniqueId() + "): Reload complete!");
		} else sender.sendMessage(Prefix.CPREFIXINFO + "Reload complete!");
	}
	
	public void sendInfo(CommandSender sender) {
		// Send information about this plugin to a command sender (console /
		// player)
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + getDescription().getVersion() + " by NathanNr"
				+ ChatColor.RESET);
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
			} else System.out.println(Prefix.CPREFIXINFO
					+ "You have disabled auto-update in the config file. You can get newer versions of AntiLaby manually from here: https://www.spigotmc.org/resources/"
					+ Resource.RESOURCE_ID + "/!");
		} else {
			System.out.println(Prefix.CPREFIXINFO + "You are running a " + getVersionType().toString().toLowerCase()
					+ " version! Auto-update is not available. You can update manually: " + Resource.RESOURCE_LINK);
			disableIfNotCompatible();
		}
	}
	
}