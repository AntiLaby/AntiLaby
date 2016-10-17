package com.github.nathannr.antilaby.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

import com.github.nathannr.antilaby.features.Join;
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
	public static String updateaviable = "Update checking was disabled in the config file";
	
	public void onEnable() {
		System.out.println("[AntiLaby/INFO] Enabled AntiLaby by Nathan_N version " + this.getDescription().getVersion() + " sucsessfully!");
		initConfig();
		initCmds();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LABYMOD");
		Bukkit.getPluginManager().registerEvents(new Join(this), this);
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
		if(this.getConfig().getBoolean("AntiLaby.Update.UpdateCheckOnStart")) {
			checkUpdate();
		} else {
			System.out.println("[AntiLaby] Update checking on start is disabled in the config file! There may be a newer version: https://www.spigotmc.org/resources/" + resource + "/");
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
				bw.newLine();
				bw.write(updateaviable);
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
				bw.newLine();
				
				
				try {
		            HttpURLConnection con = (HttpURLConnection) new URL(
		                    "http://www.spigotmc.org/api/general.php").openConnection();
		            con.setDoOutput(true);
		            con.setRequestMethod("POST");
		            con.getOutputStream()
		                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
		                            .getBytes("UTF-8"));
		            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
		            if (!version.equalsIgnoreCase(this.getDescription().getVersion())) {
		                bw.write("Update status: a new update is aviable! Possibly this version is compatible with your server! Download it here: " + "https://www.spigotmc.org/resources/" + resource + "/");
		            } else {
		            	bw.write("Update status: There is no update aviable :(");
		            }
		        } catch (Exception ex) {
		           bw.write("Update status: Failed to check for updates. Look here for updates: https://www.spigotmc.org/resources/" + resource + "/");
		        }
				
				
				
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			this.getPluginLoader().disablePlugin(this);
		}
	}
	
	@Override
	public void onDisable() {
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
		this.getConfig().addDefault("AntiLaby.Update.UpdateNotification", true);
		this.getConfig().addDefault("AntiLaby.Update.UpdateCheckOnStart", true);
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
			System.err.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
			System.err.println("Your error ID: 'EAL101'");
			System.err.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
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
			} else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				reloadPlugin(sender);
			} else {
				sendInfo(sender);
			}
		}
		return true;
	}
	
	
	public void reloadPlugin(CommandSender sender) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(!p.hasPermission("antilaby.reload")) {
				File file = new File("plugins/AntiLaby/language.yml");
				FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				System.out.println(cprefixinfo + p.getName() + " (" + p.getUniqueId() + ") tried to reload AntiLaby: Permission 'healthindicator.reload' is missing!");
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
		playerCheckUpdate(sender);
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
	
	public void checkUpdate() {
		try {
			System.out.println(cprefixinfo + "Checking for updates...");
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
                            .getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(this.getDescription().getVersion())) {
                System.err.println(cprefixinfo + "A new update is aviable: version " + version + ", download it here: " + "https://www.spigotmc.org/resources/" + resource + "/");
                updateaviable = "Update status: A new update is aviable: version " + version + ", download it here: " + "https://www.spigotmc.org/resources/" + resource + "/";
            } else {
            	updateaviable = "Update status: This server is running the newest plugin version!";
            	System.out.println(cprefixinfo + "You're running the newest plugin version!");
            }
        } catch (Exception ex) {
           System.err.println(cprefixerr + "Failed to check for updates on spigotmc.org");
           updateaviable = "Update status: Failed to check for updates!";
        }
	}
	
	public void playerCheckUpdate(CommandSender sender) {
		sender.sendMessage(ChatColor.BLUE + "Checking for updates..." + ChatColor.RESET);
		try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
                            .getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(this.getDescription().getVersion())) {
                sender.sendMessage("§6A new update is aviable: version §e" + version + "§6, download it here: §n" + "https://www.spigotmc.org/resources/" + resource + "/" + "§r");
            } else {
            	sender.sendMessage("§aYou're running the newest plugin version!§r");
            }
        } catch (Exception ex) {
           sender.sendMessage("§cFailed to check for updates on spigotmc.org§r");
        }
	}
	
	public void playerJoinCheckUpdate(Player p) {
		try {
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
                            .getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(this.getDescription().getVersion())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "§6Version §e" + version + "§6 of AntiLaby is aviable, download it here: §n" + "https://www.spigotmc.org/resources/" + resource + "/" + "§r" + "\n§7You can disable this notification in the config file§r"));
            }
        } catch (Exception ex) {}
	}

}