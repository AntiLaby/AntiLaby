package com.github.antilaby.antilaby.pluginchannel;

import com.github.antilaby.antilaby.api.LabyModJoinCommands;
import com.github.antilaby.antilaby.config.Config;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomingPluginChannel implements PluginMessageListener, Listener {
	
	private static final Pattern UUID_PATTERN = Pattern.compile("%UUID%");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%PLAYER%");
	private static HashMap<String, String> labyModPlayers = new HashMap<>();
	
	public static HashMap<String, String> getLabyModPlayers() {
		return labyModPlayers;
	}
	
	public static void setLabyModPlayers(HashMap<String, String> labyModPlayers) {
		IncomingPluginChannel.labyModPlayers = labyModPlayers;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(channel.equals(Constants.LABYMOD_CHANNEL)) {
			if(!labyModPlayers.containsKey(player.getUniqueId().toString())) {
				AntiLaby.LOG.info(
						"Player " + player.getName() + " (" + player.getUniqueId().toString() + ") uses " + new String(
								data) + '!');
				labyModPlayers.put(player.getUniqueId().toString(), player.getName());
			}
			if(Config.getLabyModPlayerKickEnable())
				if(AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission")
						   .equals("true")) {
					if(!player.hasPermission(Constants.PERMISSION_BYPASS)) {
						kickPlayer(player);
						return;
					}
				} else {
					kickPlayer(player);
					return;
				}
			if(!player.hasPermission(Constants.PERMISSION_BYPASS_JOIN_COMMANDS)) {
				LabyModJoinCommands labyModJoinCommands = new LabyModJoinCommands();
				for(final String command : labyModJoinCommands.getLabyModJoinCommands(false)) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), UUID_PATTERN.matcher(PLAYER_PATTERN.matcher
							                                                                                      (command)
							                                                                       .replaceAll(Matcher
									                                                                                   .quoteReplacement(
											                                                                                   player.getName()))).replaceAll(
							Matcher.quoteReplacement(player.getUniqueId().toString())));
				}
			}
		}
	}
	
	private void kickPlayer(Player player) {
		player.kickPlayer(LanguageManager.INSTANCE.translate("labymod.playerKickMessage", player));
		AntiLaby.LOG.info("Player " + player.getName() + " (" + player.getUniqueId().toString()
				                  + ") is not allowed to use LabyMod and has been kicked.");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(labyModPlayers.containsKey(event.getPlayer().getUniqueId().toString()))
			labyModPlayers.remove(event.getPlayer().getUniqueId().toString());
	}
	
}
