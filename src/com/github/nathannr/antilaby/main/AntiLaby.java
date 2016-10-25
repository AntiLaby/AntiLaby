package com.github.nathannr.antilaby.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.features.TabComplete;
import com.github.nathannr.antilaby.versions.v1_10_R1;
import com.github.nathannr.antilaby.versions.v1_8_R1;
import com.github.nathannr.antilaby.versions.v1_8_R2;
import com.github.nathannr.antilaby.versions.v1_8_R3;
import com.github.nathannr.antilaby.versions.v1_9_R1;
import com.github.nathannr.antilaby.versions.v1_9_R2;


public class AntiLaby extends JavaPlugin implements Listener {

	public static int resource = 21347;
	public static String prefix = "§8[§e§lAntiLaby§8] §r";
	public static String cprefixinfo = "[AntiLaby/INFO] ";
	public static String cprefixerr = "[AntiLaby/ERROR] ";
	public static String nmsver;
	
	public void onEnable() {
		System.out.println("[AntiLaby/INFO] Enabled AntiLaby by Nathan_N version " + this.getDescription().getVersion() + " sucsessfully!");
		initConfig();
		initCmds();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LABYMOD");
		nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
	//		e.printStackTrace();
		}
		for(Player p : Bukkit.getOnlinePlayers()) {
			sendPackages(p);
		}
		
		System.out.println(cprefixinfo + "Your NMS-version: " + nmsver);
		if(nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.equalsIgnoreCase("v1_8_R2") || nmsver.equalsIgnoreCase("v1_8_R3") || nmsver.equalsIgnoreCase("v1_9_R1") || nmsver.equalsIgnoreCase("v1_9_R2") || nmsver.equalsIgnoreCase("v1_10_R1")) {
			//Dont't forget to update this after adding a new NMS-version!
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
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
		if(this.getConfig().getBoolean("AntiLaby.Update.AutoUpdate")) {
			if(updateAviable()) {
				downloadUpdate();
			}
		} else {
			System.out.println(cprefixinfo + "You have disabled auto-update in the config file. You can get newer versions of AntiLaby manually from here: https://www.spigotmc.org/resources/" + resource + "/!");
		}
		System.out.println("[AntiLaby/INFO] Disabled AntiLaby by Nathan_N version " + this.getDescription().getVersion() + " sucsessfully!");
	}
	
	private void initConfig() {
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
		
		
		
		File file = new File("plugins/AntiLaby/language.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.options().header("Language file of AntiLaby by Nathan_N, https://www.spigotmc.org/resources/" + resource + "/");
		cfg.addDefault("AntiLaby.Language.NoPermission", "&cSorry, but you don't have permission to execute this command!&r");
		cfg.options().copyDefaults(true);
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void initCmds() {
		getCommand("antilaby").setTabCompleter(new TabComplete());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		if(cmd.getName().equalsIgnoreCase("antilaby")) {
			if(args.length != 1) {
				sendInfo(sender);
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					reloadPlugin(sender);
				} else if(args[0].equalsIgnoreCase("info")) {
					sendInfo(sender);
				} else {
					sendUsage(sender);
				}
			} else {
				sendUsage(sender);
			}
		}
		return true;
	}
	
	public void sendUsage(CommandSender sender) {
		if(sender instanceof Player) {
			Player p = (Player) sender;				
			p.sendMessage(prefix + "§cUsage: /antilaby <info|reload>§r");
		} else {
			sender.sendMessage(cprefixinfo + "Usage: /antilaby <info|reload>");
		}
	}
	
	public void reloadPlugin(CommandSender sender) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("antilaby.reload")) {
				File file = new File("plugins/AntiLaby/language.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + ") tried to reload AntiLaby: Permission 'antilaby.reload' is missing!");
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + cfg.getString("AntiLaby.Language.NoPermission")));
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
		sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + this.getDescription().getVersion() + " by Nathan_N" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/" + resource + "/" + ChatColor.RESET);
		sender.sendMessage(ChatColor.BLUE + "Use '/antilaby reload' to reload the plugin." + ChatColor.RESET);
		sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
	}
	
	public void sendPackages(Player p) {
		if(!p.hasPermission("antilaby.bypass")) {
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
			//Dont't forget to update the info file after adding a new NMS-version!
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
			} else {
				System.err.println("[AntiLaby/ERROR] " + "Your server version is not compatible with this plugin!");
				if(p.isOp()) {
					p.sendMessage(prefix + "§cAntiLaby is not compatible with your server version! A newer version of AntiLaby is maybe compatible with your server, check for updates here: https://www.spigotmc.org/resources/" + resource + "/§r");
				}
			}
		} else {
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
				//Dont't forget to update the info file after adding a new NMS-version!
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
				} else {
					System.err.println("[AntiLaby/ERROR] " + "Your server version is not compatible with this plugin!");
					if(p.isOp()) {
						p.sendMessage(prefix + "§cAntiLaby is not compatible with your server version! A newer version of AntiLaby is maybe compatible with your server, check for updates here: https://www.spigotmc.org/resources/" + resource + "/§r");
					}
				}
			} else {
				System.out.println("[AntiLaby/INFO] Player " + p.getName() + " (" + p.getUniqueId() + ") has the permission 'antilaby.bypass': no LabyMod functions disabled.");
			}
		}
	}
	
	public enum EnumLabyModFeature {
		FOOD, GUI, NICK, BLOCKBUILD, CHAT, EXTRAS, ANIMATIONS, POTIONS, ARMOR, DAMAGEINDICATOR, MINIMAP_RADAR;
	}
	
	public boolean downloadUpdate() {
		File aljar = new File("plugins/AntiLaby.jar");
		if(!aljar.exists()) {
			System.err.println(cprefixerr + "Auto-update failed!");
			int i = 5;
			while(i >= 0) {
				System.err.println(cprefixerr + "PLEASE RENAME THE ANTILABY PLUGIN BACK TO 'AntiLaby.jar'! OTHERWISE AUTO-UPDATE WON'T WORK! IF YOU DON'T WANT TO USE AUTO-UPDATE DISABLE IT IN THE CONFIG FILE!");
				i--;
			}
			return false;
		}
		System.out.println(cprefixinfo + "Downloading update...");
		URL url;
		try {
			
			url = new URL("http://adf.ly/1f1ZEn");
			final URLConnection conn = url.openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			if(is != null) {
				final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
				byte[] chunk = new byte[1024];
				int chunkSize;
				while ((chunkSize = is.read(chunk)) != -1) {
					os.write(chunk, 0, chunkSize);
				}
				os.close();
				File newfile = new File("plugins/AntiLaby.tmp");
				long newlength = newfile.length();
				if(newlength <= 10000) {
					newfile.delete();
					System.err.println(cprefixerr + "Auto-update failed! Update server down? You still have version " + this.getDescription().getVersion() + ". Please install the newest version manually from https://www.spigotmc.org/resources/" + resource + "/!");
				} else {
					System.out.println(cprefixinfo + "Installing update...");
					
					final FileInputStream is2 = new FileInputStream(new File("plugins/AntiLaby.tmp"));
					
					final OutputStream os2 = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.jar"));
					byte[] chunk2 = new byte[1024];
					int chunkSize2;
					while ((chunkSize2 = is2.read(chunk2)) != -1) {
						os2.write(chunk2, 0, chunkSize2);
					}
					is2.close();
					os2.close();
					
					File tmp = new File("plugins/AntiLaby.tmp");
					tmp.delete();
					
					System.out.println(cprefixinfo + "Auto-update complete!");
					return true;
				}
			}
			
		} catch (IOException e) {
			System.err.println(cprefixerr + "Auto-update failed! You still have version " + this.getDescription().getVersion() + ". Please install the newest version manually from https://www.spigotmc.org/resources/" + resource + "/!");
		}
		File tmp = new File("plugins/AntiLaby.tmp");
		if(tmp.exists()) {
			tmp.delete();
		}
		return false;
	}
	
	public boolean updateAviable() {
		System.out.println(cprefixinfo + "Checking for updates on spigotmc.org...");
		try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(this.getDescription().getVersion())) {
            	System.out.println(cprefixinfo + "Update found! Version " + version + " is aviable.");
            	return true;
            } else {
            	System.out.println(cprefixinfo + "No update found. You are running the newest version.");
            	return false;
            }
        } catch (Exception ex) {}
    	System.err.println(cprefixerr + "Failed to check for updates on spigotmc.org!");
		return false;
	}

}