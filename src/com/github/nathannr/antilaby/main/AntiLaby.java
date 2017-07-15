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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.api.Boolean2Int;
import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.nathannr.antilaby.command.AntiLabyCommand;
import com.github.nathannr.antilaby.command.AntiLabyTabComplete;
import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.main.events.PlayerJoin;
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

	// Spigot resource id
	private final int resource = 21347;
	// Prefix
	private String prefix = "§8[§e§lAntiLaby§8] §r";
	// Console prefix
	private final String cprefixinfo = "[AntiLaby/INFO] ";
	private final String cprefixerr = "[AntiLaby/ERROR] ";
	// NMS-version
	private String nmsver;
	// Compatible?
	private boolean compatible;
	// Metrics
	private Metrics metrics;
	// Is this a beta version?
	private VersionType versionType;

	private static AntiLaby instance;

	public static AntiLaby getInstance() {
		return instance;
	}

	public VersionType getVersionType() {
		return this.versionType;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public int getResource() {
		return resource;
	}

	public String getCprefixinfo() {
		return cprefixinfo;
	}

	public String getCprefixerr() {
		return cprefixerr;
	}

	public String getNmsver() {
		return nmsver;
	}

	public Metrics getMetrics() {
		return metrics;
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
		System.out.println(cprefixinfo + "Your NMS-version: " + nmsver);
		// Check if the server is compatible with AntiLaby
		if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_8_R2")
				|| nmsver.equalsIgnoreCase("v1_8_R3") || nmsver.equalsIgnoreCase("v1_9_R1")
				|| nmsver.equalsIgnoreCase("v1_9_R2") || nmsver.equalsIgnoreCase("v1_10_R1")
				|| nmsver.equalsIgnoreCase("v1_11_R1") || nmsver.equalsIgnoreCase("v1_12_R1")) {
			// TODO: Dont't forget to update this after adding a new
			// NMS-version!
			this.compatible = true;
			System.out.println(cprefixinfo + "Your server is compatible with AntiLaby!");
			try {
				FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by NathanNr, version " + this.getDescription().getVersion() + "");
				bw.newLine();
				bw.write("Link: https://www.spigotmc.org/resources/" + resource + "/");
				bw.newLine();
				bw.write("Date / time: " + new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date())
						+ " (" + System.currentTimeMillis() + ")");
				bw.newLine();
				bw.write("NMS-version: " + nmsver);
				bw.newLine();
				bw.newLine();
				bw.write("Compatibility: This version of AntiLaby is compatibly with your NMS-version :)");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			this.compatible = false;
			System.err.println(
					cprefixerr + "Your server is not compatible with this version of AntiLaby! Your NMS-version: '"
							+ nmsver + "', your AntiLaby version: '" + this.getDescription().getVersion()
							+ "'. Look into the file '" + "plugins/status.txt" + "' for more information!");
			try {
				FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by NathanNr, version " + this.getDescription().getVersion() + "");
				bw.newLine();
				bw.write("Link: https://www.spigotmc.org/resources/" + resource + "/");
				bw.newLine();
				bw.write("Date / time: " + new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date()));
				bw.newLine();
				bw.write("NMS-version: " + nmsver);
				bw.newLine();
				bw.newLine();
				bw.write("Compatibility: This version of AntiLaby is NOT compatibly with your NMS-version!");
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.update();
		// Init files, commands and events
		this.initConfig();
		this.initLanguage();
		// Register plugin channels
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LABYMOD");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "LABYMOD", new IncomingPluginChannel());
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
		// Resend AntiLaby packages (on reload)
		for (Player p : Bukkit.getOnlinePlayers()) {
			AntiLabyPackager pack = new AntiLabyPackager(p);
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
						+ resource + "/!");
			}
		} else {
			System.out.println(this.getCprefixinfo()
					+ "You are running a " + this.getVersionType().toString().toLowerCase() + " version! Auto-update is not available. You can update manually: https://www.spigotmc.org/resources/"
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
				boolean b = Config.getLabyModPlayersKickEnable();
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
		this.getConfig().addDefault("AntiLaby.LabyModPlayersKick.Enable", false);
		this.getConfig().addDefault("AntiLaby.LabyModPlayersKick.KickMessage", "&cYou are not allowed to use LabyMod!&r");
		if (this.getVersionType().equals(VersionType.RELEASE)) {
			this.getConfig().addDefault("AntiLaby.Update.AutoUpdate", true);
		} else {
			this.getConfig().set("AntiLaby.Update.AutoUpdate",
					"Auto-update is not available in " + this.getVersionType().toString().toLowerCase() + " versions! You can update manually: https://www.spigotmc.org/resources/"
							+ this.getResource() + "/");
		}
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		if(!getConfig().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("true")) {
			if(!getConfig().getString("AntiLaby.Update.AutoUpdate").equalsIgnoreCase("false")) {
				if(this.getVersionType().equals(VersionType.RELEASE)) {
					this.getConfig().set("AntiLaby.Update.AutoUpdate", true);
					this.saveConfig();
				}
			}
		}
	}

	public void initLanguage() {
		// Delete the old language file from older versions of this plugin
		File oldLang = new File("plugins/AntiLaby/language.yml");
		if (oldLang.exists()) {
			oldLang.delete();
		}

		// Create lang files in different languages
		String en_US = "en_US";
		File en_USfile = new File("plugins/AntiLaby/language/" + en_US + ".yml");
		FileConfiguration en_UScfg = YamlConfiguration.loadConfiguration(en_USfile);
		en_UScfg.options()
				.header("Language file of AntiLaby by NathanNr, https://www.spigotmc.org/resources/" + resource + "/");
		en_UScfg.addDefault("AntiLaby.Language.NoPermission", "&cYou do not have permission to use this command&r");
		en_UScfg.options().copyDefaults(true);

		String de_DE = "de_DE";
		File de_DEfile = new File("plugins/AntiLaby/language/" + de_DE + ".yml");
		FileConfiguration de_DEcfg = YamlConfiguration.loadConfiguration(de_DEfile);
		de_DEcfg.options()
				.header("Language file of AntiLaby by NathanNr, https://www.spigotmc.org/resources/" + resource + "/");
		de_DEcfg.addDefault("AntiLaby.Language.NoPermission",
				"&cDu hast nicht die benötigte Berechtigung, diesen Befehl auszuführen&r");
		de_DEcfg.options().copyDefaults(true);

		String en_GB = "en_GB";
		File en_GBfile = new File("plugins/AntiLaby/language/" + en_GB + ".yml");
		FileConfiguration en_GBcfg = YamlConfiguration.loadConfiguration(en_GBfile);
		en_GBcfg.options()
				.header("Language file of AntiLaby by NathanNr, https://www.spigotmc.org/resources/" + resource + "/");
		en_GBcfg.addDefault("AntiLaby.Language.NoPermission", "&cYou do not have permission to use this command&r");
		en_GBcfg.options().copyDefaults(true);

		String fr_FR = "fr_FR";
		File fr_FRfile = new File("plugins/AntiLaby/language/" + fr_FR + ".yml");
		FileConfiguration fr_FRcfg = YamlConfiguration.loadConfiguration(fr_FRfile);
		fr_FRcfg.options()
				.header("Language file of AntiLaby by NathanNr, https://www.spigotmc.org/resources/" + resource + "/");
		fr_FRcfg.addDefault("AntiLaby.Language.NoPermission",
				"&cVous n'avez pas la permission d'utiliser cette commande&r");
		fr_FRcfg.options().copyDefaults(true);

		try {
			// Save language files
			en_UScfg.save(en_USfile);
			de_DEcfg.save(de_DEfile);
			en_GBcfg.save(en_GBfile);
			fr_FRcfg.save(fr_FRfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initCmds() {
		// Init /antilaby command
		getCommand("antilaby").setExecutor(new AntiLabyCommand(this));
		getCommand("antilaby").setTabCompleter(new AntiLabyTabComplete());
	}

	public void sendMultiLanguageMessage(Player p, String path, Boolean translateAlternateColorCodes) {
		// Send a message in player's language
		initLanguage();
		File file = new File("plugins/AntiLaby/language/" + p.spigot().getLocale() + ".yml");
		File fallbackFile = new File("plugins/AntiLaby/language/" + "en_US" + ".yml");
		FileConfiguration fallbackCfg = YamlConfiguration.loadConfiguration(fallbackFile);
		if (path.isEmpty() || path == null) {
			p.sendMessage("§cInternal error§r");
			System.err.println(cprefixerr
					+ "MultiLanguageMessage error: Plugin tried to send a MultiLanguageMessage with an empty or null path. Please report this bug here: "
					+ "https://github.com/NathanNr/AntiLaby/issues");
			return;
		}
		if (file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if (cfg.getString(path) != null) {
				if (translateAlternateColorCodes == true) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString(path)));
				} else {
					p.sendMessage(cfg.getString(path));
				}
			} else {
				if (fallbackCfg.getString(path) != null) {
					if (translateAlternateColorCodes == true) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path)));
					} else {
						p.sendMessage(fallbackCfg.getString(path));
					}
				} else {
					p.sendMessage("§cInternal error§r");
					System.err.println(cprefixerr + "MultiLanguageMessage error: Path '" + path
							+ "' does not exists in the fallback language file. Please report this bug here: "
							+ "https://github.com/NathanNr/AntiLaby/issues");
				}
			}
		} else {
			if (fallbackCfg.getString(path) != null) {
				if (translateAlternateColorCodes == true) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path)));
				} else {
					p.sendMessage(fallbackCfg.getString(path));
				}
			} else {
				p.sendMessage("§cInternal error§r");
				System.err.println(cprefixerr + "MultiLanguageMessage error: Path '" + path
						+ "' does not exists in the fallback language file. Please report this bug here: "
						+ "https://github.com/NathanNr/AntiLaby/issues");
			}
		}
	}

	public void reloadPlugin(CommandSender sender) {
		// Reload this plugin
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (!p.hasPermission("antilaby.reload")) {
				sendMultiLanguageMessage(p, "AntiLaby.Language.NoPermission", true);
				System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId()
						+ ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(prefix + "§aReloading AntiLaby...§r");
			System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + "): Reloading AntiLaby...");
		} else {
			sender.sendMessage(cprefixinfo + "Reloading AntiLaby...");
		}
		this.initConfig();
		for (Player p : Bukkit.getOnlinePlayers()) {
			AntiLabyPackager pack = new AntiLabyPackager(p);
			pack.sendPackages();
		}
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(prefix + "§aReload complete!§r");
			System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + "): Reload complete!");
		} else {
			sender.sendMessage(cprefixinfo + "Reload complete!");
		}
	}

	public void sendInfo(CommandSender sender) {
		// Send information about this plugin to a command sender (console /
		// player)
		sender.sendMessage(
				ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + this.getDescription().getVersion()
				+ " by NathanNr" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/"
				+ resource + "/" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/antilaby reload' to reload the plugin." + ChatColor.RESET);
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