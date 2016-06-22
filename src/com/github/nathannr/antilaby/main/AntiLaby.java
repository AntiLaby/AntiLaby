package com.github.nathannr.antilaby.main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.nathannr.antilaby.listener.Join;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R2.PacketDataSerializer;
import net.minecraft.server.v1_8_R2.PacketPlayOutCustomPayload;

public class AntiLaby extends JavaPlugin implements Listener {

	public static int resource = 21347;
	public static String cprefix = "[AntiLaby/INFO] ";
	
	public void onEnable() {
		System.out.println("[AntiLaby/INFO] Enabled AntiLaby by Nathan_N version " + this.getDescription().getVersion() + " sucsessfully!");
		initConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LABYMOD");
		Bukkit.getPluginManager().registerEvents(new Join(this), this);
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(this.getConfig().getBoolean("AntiLaby.Update.UpdateCheckOnStart")) {
			checkUpdate();
		} else {
			System.out.println("[AntiLaby] Update checking on start is disabled in the config file! There may be a newer version: https://www.spigotmc.org/resources/" + resource + "/");
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
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		if(cmd.getName().equalsIgnoreCase("antilaby")) {
			sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
			sender.sendMessage(ChatColor.BLUE + "AntiLaby plugin version " + this.getDescription().getVersion() + " by Nathan_N" + ChatColor.RESET);
			sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/" + resource + "/" + ChatColor.RESET);
			playerCheckUpdate(sender);
			sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
		}
		return true;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
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
			setLabyModFeature(e.getPlayer(), list);
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
				setLabyModFeature(e.getPlayer(), list);
			} else {
				System.out.println("[AntiLaby/INFO] Player " + p.getName() + " (" + p.getUniqueId() + ") has the permission 'antilaby.bypass': no LabyMod functions disabled.");
			}
		}
	}
	
	public void setLabyModFeature(Player p, HashMap<EnumLabyModFeature, Boolean> list) {
		try {
			HashMap<String, Boolean> nList = new HashMap<String, Boolean>();
			for(EnumLabyModFeature f : list.keySet()) {
				nList.put(f.name(), list.get(f));
			}
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(nList);
			ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
			PacketDataSerializer b = new PacketDataSerializer(a);
			PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("LABYMOD", b);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			System.out.print("[AntiLaby/INFO] Disable some LabyMod functions for " + p.getName() + " (" + p.getUniqueId() + ")");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("[AntiLaby/ERROR] An unknown error has occurred: can't send AntiLaby packages to " + p.getName() + " (" + p.getUniqueId() + ")!");
		}
	}

	public enum EnumLabyModFeature {
		FOOD, GUI, NICK, BLOCKBUILD, CHAT, EXTRAS, ANIMATIONS, POTIONS, ARMOR, DAMAGEINDICATOR, MINIMAP_RADAR;
	}
	
	public void checkUpdate() {
		try {
			System.out.println("[AntiLaby] " + "Checking for updates...");
            HttpURLConnection con = (HttpURLConnection) new URL(
                    "http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream()
                    .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
                            .getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(this.getDescription().getVersion())) {
                System.err.println(cprefix + "A new update is aviable: version " + version + ", download it here: " + "https://www.spigotmc.org/resources/" + resource + "/");
            } else {
            	System.out.println(cprefix + "You're running the newest plugin version!");
            }
        } catch (Exception ex) {
           System.err.println(cprefix + "Failed to check for updates on spigotmc.org");
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
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "§8[§4AntiLaby§8] §6Version §e" + version + "§6 of AntiLaby is aviable, download it here: §n" + "https://www.spigotmc.org/resources/" + resource + "/" + "§r"));
            }
        } catch (Exception ex) {}
	}

}