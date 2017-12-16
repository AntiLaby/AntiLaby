package com.github.nathannr.antilaby.pluginchannel;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.github.nathannr.antilaby.config.Config;
import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.messagemanager.MessageManager;

import de.heisluft.antilaby.lang.impl.LanguageManager;
import de.heisluft.antilaby.util.Constants;

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
			if (!player.hasPermission(Constants.PERMISSION_BYPASS_JOIN_COMMANDS)) {
				final List<String> commands = MessageManager.getAsCommands(Config.getLabyModPlayerCommands(), player);
				for (final String command : commands)
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (labyModPlayers.containsKey(event.getPlayer().getUniqueId().toString()))
			labyModPlayers.remove(event.getPlayer().getUniqueId().toString());
	}
	
}
