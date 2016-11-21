package com.github.nathannr.antilaby.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.command.AntiLabyCommand;
import com.github.nathannr.antilaby.command.TabComplete;
import com.github.nathannr.antilaby.main.events.PlayerJoin;
import com.github.nathannr.antilaby.metrics.Metrics;
import com.github.nathannr.antilaby.update.UpdateDownload;
import com.github.nathannr.antilaby.versions.v1_10_R1;
import com.github.nathannr.antilaby.versions.v1_11_R1;
import com.github.nathannr.antilaby.versions.v1_8_R1;
import com.github.nathannr.antilaby.versions.v1_8_R2;
import com.github.nathannr.antilaby.versions.v1_8_R3;
import com.github.nathannr.antilaby.versions.v1_9_R1;
import com.github.nathannr.antilaby.versions.v1_9_R2;


public class AntiLaby extends JavaPlugin {

	//Spigot resource id
	public int resource = 21347;
	//Prefix
	public String prefix = "§8[§e§lAntiLaby§8] §r";
	//Console prefix
	public String cprefixinfo = "[AntiLaby/INFO] ";
	public String cprefixerr = "[AntiLaby/ERROR] ";
	//NMS-version
	public String nmsver;
	//Compatible?
	public boolean compatible;
	
	public static AntiLaby instance;
	public static AntiLaby getInstance(){
	  return instance;
	}
	
	@Override
	public void onLoad() {
		instance = this;
		if(this.getConfig().getBoolean("AntiLaby.Update.AutoUpdate")) {
			//Check and install updates async
			UpdateDownload ud = new UpdateDownload();
			ud.start();
		} else {
			System.out.println(cprefixinfo + "You have disabled auto-update in the config file. You can get newer versions of AntiLaby manually from here: https://www.spigotmc.org/resources/" + resource + "/!");
		}
	}
	
	@Override
	public void onEnable() {
		System.out.println("[AntiLaby/INFO] Enabled AntiLaby by Nathan_N version " + this.getDescription().getVersion() + " sucsessfully!");
		//Init files, commands and events
		initConfig();
		initLanguage();
		initCmds();
		initEvents();
		//Register plugin channel
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LABYMOD");
		//Get NMS-version
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		//Start plugin metrics for MCStats.org
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		com.github.nathannr.antilaby.metrics.BStats bstats = new com.github.nathannr.antilaby.metrics.BStats(this);
		//Resend AntiLaby packages on reload
		for(Player p : Bukkit.getOnlinePlayers()) {
			sendPackages(p);
		}
		System.out.println(cprefixinfo + "Your NMS-version: " + nmsver);
		//Check if the server is compatible with AntiLaby
		if(nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_8_R2") || nmsver.equalsIgnoreCase("v1_8_R3") || nmsver.equalsIgnoreCase("v1_9_R1") || nmsver.equalsIgnoreCase("v1_9_R2") || nmsver.equalsIgnoreCase("v1_10_R1") || nmsver.equalsIgnoreCase("v1_11_R1")) {
			//TODO: Dont't forget to update this after adding a new NMS-version!
			compatible = true;
			System.out.println(cprefixinfo + "Your server is compatible with AntiLaby!");
			try {
				FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by Nathan_N, version " + this.getDescription().getVersion() + "");
				bw.newLine();
				bw.write("Link: https://www.spigotmc.org/resources/" + resource + "/");
				bw.newLine();
				bw.write("Date / time: " + new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz").format(new Date()));
				bw.newLine();
				bw.write("NMS-version: " + nmsver);
				bw.newLine();
				bw.newLine();
				bw.write("Compatibility: This version of AntiLaby is compatibly with your NMS-version :)");
				bw.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			compatible = false;
			System.err.println(cprefixerr + "Your server is not compatible with this version of AntiLaby! Your NMS-version: '" + nmsver + "', your AntiLaby version: '" + this.getDescription().getVersion() + "'. Look into the file '" + "plugins/status.txt" + "' for more information!");
			try {
				FileWriter fw = new FileWriter("plugins/AntiLaby/info.txt");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("AntiLaby plugin by Nathan_N, version " + this.getDescription().getVersion() + "");
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
	}
	
	@Override
	public void onDisable() {
		System.out.println("[AntiLaby/INFO] Disabled AntiLaby by Nathan_N version " + this.getDescription().getVersion() + " sucsessfully!");
	}
	
	public void disableIfNotCompatible() {
		if(!compatible) {
			this.onDisable();
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	public void initEvents() {
		//Register PlayerJoinEvent to send AntiLaby packages on join
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerJoin(this), this);
	}
	
	private void initConfig() {
		//Init config
		this.reloadConfig();
		this.getConfig().options().header("AntiLaby plugin by Nathan_N, https://www.spigotmc.org/resources/" + resource + "/");
		this.getConfig().addDefault("AntiLaby.EnableBypassWithPermission", false);
		this.getConfig().addDefault("AntiLaby.disable.FOOD", true);
		this.getConfig().addDefault("AntiLaby.disable.GUI", true);
		this.getConfig().addDefault("AntiLaby.disable.NICK", true);
		this.getConfig().addDefault("AntiLaby.disable.BLOCKBUILD", true);
		this.getConfig().addDefault("AntiLaby.disable.CHAT", true);
		this.getConfig().addDefault("AntiLaby.disable.EXTRAS", true);
		this.getConfig().addDefault("AntiLaby.disable.ANIMATIONS", true);
		this.getConfig().addDefault("AntiLaby.disable.ANIMATIONS", true);
		this.getConfig().addDefault("AntiLaby.disable.POTIONS", true);
		this.getConfig().addDefault("AntiLaby.disable.ARMOR", true);
		this.getConfig().addDefault("AntiLaby.disable.DAMAGEINDICATOR", true);
		this.getConfig().addDefault("AntiLaby.disable.MINIMAP_RADAR", true);
		this.getConfig().addDefault("AntiLaby.Update.AutoUpdate", true);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	public void initLanguage() {
		
		//Delete the old language file frome older versions of this plugin
		File oldLang = new File("plugins/AntiLaby/language.yml");
		if(oldLang.exists()) {
			oldLang.delete();
		}
		
		//Create lang files in diffrent languages
		String en_US = "en_US";
		File en_USfile = new File("plugins/AntiLaby/language/" + en_US + ".yml");
		FileConfiguration en_UScfg = YamlConfiguration.loadConfiguration(en_USfile);
		en_UScfg.options().header("Language file of AntiLaby by Nathan_N, https://www.spigotmc.org/resources/" + resource + "/");
		en_UScfg.addDefault("AntiLaby.Language.NoPermission", "&cYou do not have permission to use this command&r");
		en_UScfg.options().copyDefaults(true);
		
		String de_DE = "de_DE";
		File de_DEfile = new File("plugins/AntiLaby/language/" + de_DE + ".yml");
		FileConfiguration de_DEcfg = YamlConfiguration.loadConfiguration(de_DEfile);
		de_DEcfg.options().header("Language file of AntiLaby by Nathan_N, https://www.spigotmc.org/resources/" + resource + "/");
		de_DEcfg.addDefault("AntiLaby.Language.NoPermission", "&cDu hast nicht die benötigte Berechtigung, diesen Befehl auszuführen&r");
		de_DEcfg.options().copyDefaults(true);
		
		String en_GB = "en_GB";
		File en_GBfile = new File("plugins/AntiLaby/language/" + en_GB + ".yml");
		FileConfiguration en_GBcfg = YamlConfiguration.loadConfiguration(en_GBfile);
		en_GBcfg.options().header("Language file of AntiLaby by Nathan_N, https://www.spigotmc.org/resources/" + resource + "/");
		en_GBcfg.addDefault("AntiLaby.Language.NoPermission", "&cYou do not have permission to use this command&r");
		en_GBcfg.options().copyDefaults(true);
		
		String fr_FR = "fr_FR";
		File fr_FRfile = new File("plugins/AntiLaby/language/" + fr_FR + ".yml");
		FileConfiguration fr_FRcfg = YamlConfiguration.loadConfiguration(fr_FRfile);
		fr_FRcfg.options().header("Language file of AntiLaby by Nathan_N, https://www.spigotmc.org/resources/" + resource + "/");
		fr_FRcfg.addDefault("AntiLaby.Language.NoPermission", "&cVous n'avez pas la permission d'utiliser cette commande&r");
		fr_FRcfg.options().copyDefaults(true);
		
		try {
			//Save language files
			en_UScfg.save(en_USfile);
			de_DEcfg.save(de_DEfile);
			en_GBcfg.save(en_GBfile);
			fr_FRcfg.save(fr_FRfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initCmds() {
		//Init /antilaby command
		getCommand("antilaby").setExecutor(new AntiLabyCommand(this));
		getCommand("antilaby").setTabCompleter(new TabComplete());
	}
	
	public void sendMultiLanguageMessage(Player p, String path, Boolean translateAlternateColorCodes) {
		//Send a message in player's language
		initLanguage();
		File file = new File("plugins/AntiLaby/language/" + p.spigot().getLocale() + ".yml");
		File fallbackFile = new File("plugins/AntiLaby/language/" + "en_US" + ".yml");
		FileConfiguration fallbackCfg = YamlConfiguration.loadConfiguration(fallbackFile);
		if(path.isEmpty() || path == null) {
			p.sendMessage("§cInternal error§r");
			System.err.println(cprefixerr + "MultiLanguageMessage error: Plugin tried to send a MultiLanguageMessage with an empty or null path. Please report this bug here: " + "https://github.com/NathanNr/AntiLaby/issues");
			return;
		}
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getString(path) != null) {
				if(translateAlternateColorCodes == true) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', cfg.getString(path)));
				} else {
					p.sendMessage(cfg.getString(path));
				}
			} else {
				if(fallbackCfg.getString(path) != null) {
					if(translateAlternateColorCodes == true) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path)));
					} else {
						p.sendMessage(fallbackCfg.getString(path));
					}
				} else {
					p.sendMessage("§cInternal error§r");
					System.err.println(cprefixerr + "MultiLanguageMessage error: Path '" + path + "' does not exists in the fallback language file. Please report this bug here: " + "https://github.com/NathanNr/AntiLaby/issues");
				}
			}
		} else {
			if(fallbackCfg.getString(path) != null) {
				if(translateAlternateColorCodes == true) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path)));
				} else {
					p.sendMessage(fallbackCfg.getString(path));
				}
			} else {
				p.sendMessage("§cInternal error§r");
				System.err.println(cprefixerr + "MultiLanguageMessage error: Path '" + path + "' does not exists in the fallback language file. Please report this bug here: " + "https://github.com/NathanNr/AntiLaby/issues");
			}
		}
	}
	
	public void reloadPlugin(CommandSender sender) {
		//Reload this plugin
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("antilaby.reload")) {
				sendMultiLanguageMessage(p, "AntiLaby.Language.NoPermission", true);
				System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(prefix + "§aReloading AntiLaby...§r");
			System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + "): Reloading AntiLaby...");
		} else {
			sender.sendMessage(cprefixinfo + "Reloading AntiLaby...");
		}
		initConfig();
		for(Player p : Bukkit.getOnlinePlayers()) {
			sendPackages(p);
		}
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(prefix + "§aReload complete!§r");
			System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + "): Reload complete!");
		} else {
			sender.sendMessage(cprefixinfo + "Reload complete!");
		}
	}
	
	public void sendInfo(CommandSender sender) {
		//Send information about this plugin to a command sender (console / player)
		sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + this.getDescription().getVersion() + " by Nathan_N" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/" + resource + "/" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/antilaby reload' to reload the plugin." + ChatColor.RESET);
		sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}
	
	public void sendPackages(Player p) {
		//Ignore players with the bypass permission if enabled in the config file
		if(!p.hasPermission("antilaby.bypass")) {
			//Send AntiLaby packages to a player
			HashMap<EnumLabyModFeature, Boolean> list = new HashMap<EnumLabyModFeature, Boolean>();
			if(this.getConfig().getBoolean("AntiLaby.disable.FOOD")) {
				list.put(EnumLabyModFeature.FOOD, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.GUI")) {
				list.put(EnumLabyModFeature.GUI, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.NICK")) {
				list.put(EnumLabyModFeature.NICK, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.BLOCKBUILD")) {
				list.put(EnumLabyModFeature.BLOCKBUILD, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.CHAT")) {
				list.put(EnumLabyModFeature.CHAT, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.EXTRAS")) {
				list.put(EnumLabyModFeature.EXTRAS, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.ANIMATIONS")) {
				list.put(EnumLabyModFeature.ANIMATIONS, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.POTIONS")) {
				list.put(EnumLabyModFeature.POTIONS, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.ARMOR")) {
				list.put(EnumLabyModFeature.ARMOR, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.DAMAGEINDICATOR")) {
				list.put(EnumLabyModFeature.DAMAGEINDICATOR, false);
			}
			if(this.getConfig().getBoolean("AntiLaby.disable.MINIMAP_RADAR")) {
				list.put(EnumLabyModFeature.MINIMAP_RADAR, false);
			}
			//TODO: Dont't forget to update the info file after adding a new NMS-version!
			if(nmsver.equalsIgnoreCase("v1_8_R1")) {
				v1_8_R1.setLabyModFeature(p, list);
			} else if(nmsver.equalsIgnoreCase("v1_8_R2")) {
				v1_8_R2.setLabyModFeature(p, list);
			} else if(nmsver.equalsIgnoreCase("v1_8_R3")) {
				v1_8_R3.setLabyModFeature(p, list);
			} else if(nmsver.equalsIgnoreCase("v1_9_R1")) {
				v1_9_R1.setLabyModFeature(p, list);
			} else if(nmsver.equalsIgnoreCase("v1_9_R2")) {
				v1_9_R2.setLabyModFeature(p, list);
			} else if(nmsver.equalsIgnoreCase("v1_10_R1")) {
				v1_10_R1.setLabyModFeature(p, list);
			} else if(nmsver.equalsIgnoreCase("v1_11_R1")) {
				v1_11_R1.setLabyModFeature(p, list);
			} else {
				System.err.println("[AntiLaby/ERROR] " + "Your server version is not compatible with this plugin!");
				if(p.isOp()) {
					p.sendMessage(prefix + "§4AntiLaby is not compatible with your server version! A newer version of AntiLaby is maybe compatible with your server, check for updates here: https://www.spigotmc.org/resources/" + resource + "/§r");
				}
			}
		} else {
			//Ignore players with the bypass permission if enabled in the config file
			if(!this.getConfig().getBoolean("AntiLaby.EnableBypassWithPermission")) {
				HashMap<EnumLabyModFeature, Boolean> list = new HashMap<EnumLabyModFeature, Boolean>();
				if(this.getConfig().getBoolean("AntiLaby.disable.FOOD")) {
					list.put(EnumLabyModFeature.FOOD, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.GUI")) {
					list.put(EnumLabyModFeature.GUI, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.NICK")) {
					list.put(EnumLabyModFeature.NICK, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.BLOCKBUILD")) {
					list.put(EnumLabyModFeature.BLOCKBUILD, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.CHAT")) {
					list.put(EnumLabyModFeature.CHAT, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.EXTRAS")) {
					list.put(EnumLabyModFeature.EXTRAS, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.ANIMATIONS")) {
					list.put(EnumLabyModFeature.ANIMATIONS, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.POTIONS")) {
					list.put(EnumLabyModFeature.POTIONS, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.ARMOR")) {
					list.put(EnumLabyModFeature.ARMOR, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.DAMAGEINDICATOR")) {
					list.put(EnumLabyModFeature.DAMAGEINDICATOR, false);
				}
				if(this.getConfig().getBoolean("AntiLaby.disable.MINIMAP_RADAR")) {
					list.put(EnumLabyModFeature.MINIMAP_RADAR, false);
				}
				//TODO: Dont't forget to update the info file after adding a new NMS-version!
				if(nmsver.equalsIgnoreCase("v1_8_R1")) {
					v1_8_R1.setLabyModFeature(p, list);
				} else if(nmsver.equalsIgnoreCase("v1_8_R2")) {
					v1_8_R2.setLabyModFeature(p, list);
				} else if(nmsver.equalsIgnoreCase("v1_8_R3")) {
					v1_8_R3.setLabyModFeature(p, list);
					System.out.println("Version v1_8_R3");
				} else if(nmsver.equalsIgnoreCase("v1_9_R1")) {
					v1_9_R1.setLabyModFeature(p, list);
				} else if(nmsver.equalsIgnoreCase("v1_9_R2")) {
					v1_9_R2.setLabyModFeature(p, list);
				} else if(nmsver.equalsIgnoreCase("v1_10_R1")) {
					v1_10_R1.setLabyModFeature(p, list);
				} else if(nmsver.equalsIgnoreCase("v1_11_R1")) {
					v1_11_R1.setLabyModFeature(p, list);
				} else {
					System.err.println("[AntiLaby/ERROR] " + "Your server version is not compatible with this plugin!");
					if(p.isOp()) {
						p.sendMessage(prefix + "§4AntiLaby is not compatible with your server version! A newer version of AntiLaby is maybe compatible with your server, check for updates here: https://www.spigotmc.org/resources/" + resource + "/§r");
					}
				}
			} else {
				System.out.println("[AntiLaby/INFO] Player " + p.getName() + " (" + p.getUniqueId() + ") has the permission 'antilaby.bypass': no LabyMod functions disabled.");
			}
		}
	}
	
	public enum EnumLabyModFeature {
		//The LabyMod functions
		FOOD, GUI, NICK, BLOCKBUILD, CHAT, EXTRAS, ANIMATIONS, POTIONS, ARMOR, DAMAGEINDICATOR, MINIMAP_RADAR;
	}

}