package com.github.nathannr.antilaby.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.api.Boolean2Int;
import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.nathannr.antilaby.api.util.PluginChannel;
import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.api.util.Resource;
import com.github.nathannr.antilaby.command.AntiLabyCommand;
import com.github.nathannr.antilaby.command.AntiLabyTabComplete;
import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.events.PlayerJoin;
import com.github.nathannr.antilaby.messagemanager.MultiLanguage;
import com.github.nathannr.antilaby.metrics.BStats;
import com.github.nathannr.antilaby.metrics.Metrics;
import com.github.nathannr.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.nathannr.antilaby.update.UpdateDownloader;
import com.github.nathannr.antilaby.update.VersionType;

public class AntiLaby extends JavaPlugin {

	/**
	 * Main class of AntiLaby Spigot plugin
	 * 
	 * @author NathanNr
	 */

	// NMS-version
	private String nmsver;
	// Compatible?
	private boolean compatible;
	// MCStats.org Metrics
	private Metrics metrics;
	// Is this a beta version?
	private VersionType versionType;
	// MultiLanguage
	private MultiLanguage multiLanguage;

	private static AntiLaby instance;

	public static AntiLaby getInstance() {
		return instance;
	}

	public VersionType getVersionType() {
		return this.versionType;
	}

	@Deprecated
	public String getPrefix() {
		return Prefix.PREFIX;
	}

	@Deprecated
	public void setPrefix(String prefix) {
	}

	@Deprecated
	public int getResource() {
		return Resource.RESOURCE_ID;
	}

	@Deprecated
	public String getCprefixinfo() {
		return Prefix.CPREFIXINFO;
	}

	@Deprecated
	public String getCprefixerr() {
		return Prefix.CPREFIXERROR;
	}

	public String getNmsver() {
		return this.nmsver;
	}

	public Metrics getMetrics() {
		return this.metrics;
	}

	public MultiLanguage getMultiLanguage() {
		return this.multiLanguage;
	}

	@Override
	public void onLoad() {
		instance = this;
		if (this.getDescription().getVersion().toLowerCase().contains("pre")) {
			this.versionType = VersionType.PRE_RELEASE;
		} else if (this.getDescription().getVersion().toLowerCase().contains("beta")) {
			this.versionType = VersionType.BETA;
		} else if (this.getDescription().getVersion().toLowerCase().contains("alpha")) {
			this.versionType = VersionType.ALPHA;
		} else if (this.getDescription().getVersion().toLowerCase().contains("dev")) {
			this.versionType = VersionType.DEV;
		} else if (this.getDescription().getVersion().toLowerCase().contains("custom")) {
			this.versionType = VersionType.CUSTOM;
		} else {
			this.versionType = VersionType.RELEASE;
		}
	}

	@Override
	public void onEnable() {
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
			this.compatible = true;
			System.out.println(Prefix.CPREFIXINFO + "Your server is compatible with AntiLaby!");
			try {
				FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by NathanNr, version " + this.getDescription().getVersion() + "");
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
				bw.write(
						"Other permission:");
				bw.newLine();
				bw.write(
						" - antilaby.bypasscommands: Allows you to bypass the join commands if you are using LabyMod");
				bw.newLine();
				bw.newLine();
				bw.write("Find more information about AntiLaby and how to use it: " + Resource.RESOURCE_LINK);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.compatible = false;
			System.err.println(Prefix.CPREFIXERROR
					+ "Your server is not compatible with this version of AntiLaby! Your NMS-version: '" + nmsver
					+ "', your AntiLaby version: '" + this.getDescription().getVersion() + "'. Look into the file '"
					+ "plugins/status.txt" + "' for more information!");
			try {
				FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by NathanNr, version " + this.getDescription().getVersion() + "");
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.update();
		// Init files, commands and events
		this.initConfig();
		// Register plugin channels
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, PluginChannel.LABYMOD_CHANNEL);
		Bukkit.getMessenger().registerIncomingPluginChannel(this, PluginChannel.LABYMOD_CHANNEL,
				new IncomingPluginChannel());
		this.initCmds();
		this.initEvents();
		// Start plugin metrics for MCStats.org
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
		}
		// Start plugin metrics for bStats.org
		initBMetrics();
		// Init MultiLanguage system
		multiLanguage = new MultiLanguage(getInstance(), this.getCprefixinfo(), this.getResource());
		// Resend AntiLaby packages (on reload)
		for (Player all : Bukkit.getOnlinePlayers()) {
			AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		System.out.println("[AntiLaby/INFO] Enabled AntiLaby by NathanNr version " + this.getDescription().getVersion()
				+ " sucsessfully!");
	}

	@Override
	public void onDisable() {
		System.out.println("[AntiLaby/INFO] Disabled AntiLaby by NathanNr version " + this.getDescription().getVersion()
				+ " sucsessfully!");
	}

	private void update() {
		if (this.getVersionType().equals(VersionType.RELEASE)) {
			if (this.getConfig().getBoolean("AntiLaby.Update.AutoUpdate")) {
				// Check and install updates async
				UpdateDownloader ud = new UpdateDownloader();
				ud.start();
			} else {
				System.out.println(this.getCprefixinfo()
						+ "You have disabled auto-update in the config file. You can get newer versions of AntiLaby manually from here: https://www.spigotmc.org/resources/"
						+ Resource.RESOURCE_ID + "/!");
			}
		} else {
			System.out.println(
					this.getCprefixinfo() + "You are running a " + this.getVersionType().toString().toLowerCase()
							+ " version! Auto-update is not available. You can update manually: https://www.spigotmc.org/resources/"
							+ this.getResource() + "/");
			this.disableIfNotCompatible();
		}
	}

	public void initBMetrics() {
		// Start plugin metrics for bStats.org
		com.github.nathannr.antilaby.metrics.BStats bstats = new com.github.nathannr.antilaby.metrics.BStats(this);
		bstats.addCustomChart(new BStats.SimplePie("autoupdate_enabled") {
			// Auto-update enabled?
			@Override
			public String getValue() {
				String r = AntiLaby.getInstance().getConfig().getString("AntiLaby.Update.AutoUpdate");
				if (r.equalsIgnoreCase("true") || r.equalsIgnoreCase("false")) {
					return r.toLowerCase();
				} else {
					return "false";
				}
			}
		});
		bstats.addCustomChart(new BStats.SimplePie("bypass_enabled") {
			// Bypass with permission enabled?
			@Override
			public String getValue() {
				String r = AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission");
				if (r.equalsIgnoreCase("true") || r.equalsIgnoreCase("false")) {
					return r.toLowerCase();
				} else {
					return "false";
				}
			}
		});
		bstats.addCustomChart(new BStats.SimplePie("kick_enabled") {
			// LabyMod player kick enabled?
			@Override
			public String getValue() {
				boolean b = Config.getLabyModPlayerKickEnable();
				if (!String.valueOf(b).equals("null")) {
					return String.valueOf(b);
				} else {
					return "false";
				}
			}
		});
		// TODO: Change to bar charts
		bstats.addCustomChart(new BStats.AdvancedPie("disabled_functions") {
			// Disabled functions
			@Override
			public HashMap<String, Integer> getValues(HashMap<String, Integer> valueMap) {
				int FOOD = (AntiLaby.getInstance().getConfig().getBoolean("") ? 1 : 0);
				int GUI = Boolean2Int.convert(Config.getFOOD());
				int NICK = Boolean2Int.convert(Config.getNICK());
				int BLOCKBUILD = Boolean2Int.convert(Config.getBLOCKBUILD());
				int CHAT = Boolean2Int.convert(Config.getCHAT());
				int EXTRAS = Boolean2Int.convert(Config.getEXTRAS());
				int ANIMATIONS = Boolean2Int.convert(Config.getANIMATIONS());
				int POTIONS = Boolean2Int.convert(Config.getPOTIONS());
				int ARMOR = Boolean2Int.convert(Config.getARMOR());
				int DAMAGEINDICATOR = Boolean2Int.convert(Config.getDAMAGEINDICATOR());
				int MINIMAP_RADAR = Boolean2Int.convert(Config.getMINIMAP_RADAR());
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
		});
	}

	// Disable the plugin if not compatible
	public void disableIfNotCompatible() {
		if (!this.compatible) {
			this.getPluginLoader().disablePlugin(this);
		}
	}

	public void initEvents() {
		// Register PlayerJoinEvent to send AntiLaby packages on join
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoin(), this);
	}

	private void initConfig() {
		// Init config
		this.reloadConfig();
		this.getConfig().options()
				.header("AntiLaby plugin by NathanNr, https://www.spigotmc.org/resources/" + this.getResource() + "/");
		this.getConfig().addDefault("AntiLaby.EnableBypassWithPermission", false);
		this.getConfig().addDefault("AntiLaby.LabyModPlayerKick.Enable", false);
		this.getConfig().addDefault("AntiLaby.disable.FOOD", true);
		this.getConfig().addDefault("AntiLaby.disable.GUI", true);
		this.getConfig().addDefault("AntiLaby.disable.NICK", true);
		this.getConfig().addDefault("AntiLaby.disable.BLOCKBUILD", true);
		this.getConfig().addDefault("AntiLaby.disable.CHAT", true);
		this.getConfig().addDefault("AntiLaby.disable.EXTRAS", true);
		this.getConfig().addDefault("AntiLaby.disable.ANIMATIONS", true);
		this.getConfig().addDefault("AntiLaby.disable.POTIONS", true);
		this.getConfig().addDefault("AntiLaby.disable.ARMOR", true);
		this.getConfig().addDefault("AntiLaby.disable.DAMAGEINDICATOR", true);
		this.getConfig().addDefault("AntiLaby.disable.MINIMAP_RADAR", true);
		List<String> labyModPlayerCommands = getConfig().getStringList("AntiLaby.LabyModPlayerCommands");
		labyModPlayerCommands.add("#These commands will be executed once if a player with LabyMod joins the server.");
		labyModPlayerCommands
				.add("#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
		labyModPlayerCommands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
		labyModPlayerCommands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
		if (this.getConfig().getList("AntiLaby.LabyModPlayerCommands") == null) {
			this.getConfig().set("AntiLaby.LabyModPlayerCommands", labyModPlayerCommands);
		}
		if (this.getVersionType().equals(VersionType.RELEASE)) {
			this.getConfig().addDefault("AntiLaby.Update.AutoUpdate", true);
		} else {
			this.getConfig().set("AntiLaby.Update.AutoUpdate",
					"Auto-update is not available in " + this.getVersionType().toString().toLowerCase()
							+ " versions! You can update manually: https://www.spigotmc.org/resources/"
							+ this.getResource() + "/");
		}
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		if (!getConfig().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("true")) {
			if (!getConfig().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("false")) {
				if (this.getVersionType().equals(VersionType.RELEASE)) {
					this.getConfig().set("AntiLaby.Update.AutoUpdate", true);
					this.saveConfig();
				}
			}
		}
	}

	public void initCmds() {
		// Init /antilaby command
		getCommand("antilaby").setExecutor(new AntiLabyCommand(this));
		getCommand("antilaby").setTabCompleter(new AntiLabyTabComplete());
	}

	public void reloadPlugin(CommandSender sender) {
		// Reload this plugin
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (!player.hasPermission("antilaby.reload")) {
				player.sendMessage(this.multiLanguage.getMultiLanguageMessage(player, "NoPermission", true));
				System.out.println(Prefix.CPREFIXINFO + player.getName() + " (" + player.getUniqueId()
						+ ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(Prefix.PREFIX + "§aReloading AntiLaby...§r");
			System.out.println(
					Prefix.CPREFIXINFO + player.getName() + " (" + player.getUniqueId() + "): Reloading AntiLaby...");
		} else {
			sender.sendMessage(Prefix.CPREFIXINFO + "Reloading AntiLaby...");
		}
		this.initConfig();
		for (Player all : Bukkit.getOnlinePlayers()) {
			AntiLabyPackager pack = new AntiLabyPackager(all);
			pack.sendPackages();
		}
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(Prefix.PREFIX + "§aReload complete!§r");
			System.out.println(
					Prefix.CPREFIXINFO + player.getName() + " (" + player.getUniqueId() + "): Reload complete!");
		} else {
			sender.sendMessage(Prefix.CPREFIXINFO + "Reload complete!");
		}
	}

	public void sendInfo(CommandSender sender) {
		// Send information about this plugin to a command sender (console /
		// player)
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + this.getDescription().getVersion()
				+ " by NathanNr" + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "More information about the plugin: " + Resource.RESOURCE_LINK + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/antilaby reload' to reload the plugin." + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.BLUE + "Use '/labyinfo <player>' to check if a player uses LabyMod." + ChatColor.RESET);
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}

	public File getPluginFile() {
		if (!(this instanceof JavaPlugin)) {
			return null;
		}
		try {
			Method method = JavaPlugin.class.getDeclaredMethod("getFile");
			method.setAccessible(true);
			return (File) method.invoke(this);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException("Could not get plugin file", e);
		}
	}

}