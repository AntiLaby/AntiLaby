package net.nathann.mc.antilaby;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_9_R1.PacketDataSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutCustomPayload;

public class AntiLaby extends JavaPlugin implements Listener {

	String version = " 1.1.1 ";
	
	public void onEnable() {
		System.out.println("[AntiLaby1.9/INFO] Enabled AntiLaby by Nathan_N version" + version + "sucsessfully!");
		initConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "LABYMOD");
	}
	
	@Override
	public void onDisable() {
		System.out.println("[AntiLaby1.9/INFO] Disabled AntiLaby by Nathan_N" + version + "sucsessfully!");
	}
	
	public void initConfig() {
		this.reloadConfig();
		this.getConfig().options().header("AntiLaby plugin for Minecraft 1.9 by Nathan_N, https://www.spigotmc.org/resources/21345/");
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
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	
		if(cmd.getName().equalsIgnoreCase("antilaby")) {
			sender.sendMessage(ChatColor.DARK_BLUE + "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-" + ChatColor.RESET);
			sender.sendMessage(ChatColor.BLUE + "AntiLaby-1.9 plugin version" + version + "by Nathan_N" + ChatColor.RESET);
			sender.sendMessage(ChatColor.BLUE + "More information about the plugin: https://www.spigotmc.org/resources/21345/" + ChatColor.RESET);
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
				System.out.println("[AntiLaby1.9/INFO] Player " + p.getName() + " (" + p.getUniqueId() + ") has the permission 'antilaby.bypass': no LabyMod functions disabled.");
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
			System.out.print("[AntiLaby1.9/INFO] Disable some LabyMod functions for " + p.getName() + " (" + p.getUniqueId() + ")");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("[AntiLaby1.9/WARNING] An unknown error has occurred: can't send AntiLaby packages to " + p.getName() + " (" + p.getUniqueId() + ")!");
		}
	}

	public enum EnumLabyModFeature {
		FOOD, GUI, NICK, BLOCKBUILD, CHAT, EXTRAS, ANIMATIONS, POTIONS, ARMOR, DAMAGEINDICATOR, MINIMAP_RADAR;
	}

}