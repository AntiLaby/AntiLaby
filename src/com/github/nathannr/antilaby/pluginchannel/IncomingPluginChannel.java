package com.github.nathannr.antilaby.pluginchannel;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.github.nathannr.antilaby.api.util.Permission;
import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.main.AntiLaby;

public class IncomingPluginChannel implements PluginMessageListener {

	ArrayList<Player> labyModPlayers = new ArrayList<>();
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(channel.equals("LABYMOD")) {
			if(!labyModPlayers.contains(player)) {
				System.out.println(AntiLaby.getInstance().getCprefixinfo() + "Player " + player.getName() + " (" + player.getUniqueId().toString() + ") uses LabyMod.");
				labyModPlayers.add(player);
			}
			if(Config.getLabyModPlayersKickEnable()) {
				if(AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission").equals("true")) {
					if(!player.hasPermission(Permission.BYPASS_PERMISSION)) {
						kickPlayer(player);
					}
				} else {
					kickPlayer(player);
				}
			}
		}
	}
	
	private void kickPlayer(Player player) {
		player.kickPlayer(ChatColor.translateAlternateColorCodes('&', Config.getLabyModPlayersKickMessage()));
		System.out.println(AntiLaby.getInstance().getCprefixinfo() + "Player " + player.getName() + " (" + player.getUniqueId().toString() + ") is not allowed to use LabyMod and has been kicked.");
	}
	
}
