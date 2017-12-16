package com.github.antilaby.antilaby.pluginchannel;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.github.antilaby.antilaby.config.Config;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;

public class IncomingPluginChannel implements PluginMessageListener, Listener {

	private static HashMap<String, String> labyModPlayers = new HashMap<>();

	public static HashMap<String, String> getLabyModPlayers() {
		return labyModPlayers;
	}
	
	public static void setLabyModPlayers(HashMap<String, String> labyModPlayers) {
		IncomingPluginChannel.labyModPlayers = labyModPlayers;
	}
	
	private void kickPlayer(Player player) {
		player.kickPlayer(LanguageManager.INSTANCE.translate("labymod.playerKickMessage", player));
		AntiLaby.LOG.info("Player " + player.getName() + " (" + player.getUniqueId().toString()
				+ ") is not allowed to use LabyMod and has been kicked.");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if (channel.equals(Constants.LABYMOD_CHANNEL)) {
			if (!labyModPlayers.containsKey(player.getUniqueId().toString())) {
				AntiLaby.LOG.info(
						"Player " + player.getName() + " (" + player.getUniqueId().toString() + ") uses LabyMod.");
				labyModPlayers.put(player.getUniqueId().toString(), player.getName());
			}
			if (Config.getLabyModPlayerKickEnable()) if (AntiLaby.getInstance().getConfig()
					.getString("AntiLaby.EnableBypassWithPermission").equals("true")) {
				if (!player.hasPermission(Constants.PERMISSION_BYPASS)) {
					kickPlayer(player);
					return;
				}
			} else {
				kickPlayer(player);
				return;
			}
			if (!player.hasPermission(Constants.PERMISSION_BYPASS_JOIN_COMMANDS))
				for (final String command : Config.getLabyModPlayerCommands())
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%PLAYER%", player.getName())
						.replace("%UUID%", player.getUniqueId().toString()));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (labyModPlayers.containsKey(event.getPlayer().getUniqueId().toString()))
			labyModPlayers.remove(event.getPlayer().getUniqueId().toString());
	}

}
