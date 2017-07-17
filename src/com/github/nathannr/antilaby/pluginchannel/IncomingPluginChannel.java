package com.github.nathannr.antilaby.pluginchannel;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.github.nathannr.antilaby.api.util.Permission;
import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.messagemanager.MessageManager;

public class IncomingPluginChannel implements PluginMessageListener {

	private HashMap<Player, Boolean> labyModPlayers = new HashMap<>();
	
	public HashMap<Player, Boolean> getLabyModPlayers() {
		return this.labyModPlayers;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(channel.equals("LABYMOD")) {
			// TODO: BypassCommands
			if(!labyModPlayers.containsKey(player)) {
				System.out.println(Prefix.CPREFIXINFO + "Player " + player.getName() + " (" + player.getUniqueId().toString() + ") uses LabyMod.");
				labyModPlayers.put(player, true);
			}
			if(Config.getLabyModPlayerKickEnable()) {
				if(AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission").equals("true")) {
					if(!player.hasPermission(Permission.BYPASS_PERMISSION)) {
						this.kickPlayer(player);
						return;
					}
				} else {
					this.kickPlayer(player);
					return;
				}
			}
			if(!player.hasPermission(Permission.BYPASS_COMMANDS_PERMISSION)) {
				List<String> commands = MessageManager.getAsCommands(Config.getLabyModPlayerCommands(), player);
				for(String command : commands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
				}
			}
		}
	}
	
	private void kickPlayer(Player player) {
		player.kickPlayer(AntiLaby.getInstance().getMultiLanguage().getMultiLanguageMessage(player, "LabyModPlayerKick", true));
		System.out.println(Prefix.CPREFIXINFO + "Player " + player.getName() + " (" + player.getUniqueId().toString() + ") is not allowed to use LabyMod and has been kicked.");
	}
	
}
