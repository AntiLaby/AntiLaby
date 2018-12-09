package com.github.antilaby.antilaby.pluginchannel;

import com.github.antilaby.antilaby.api.command.ExecutableCommand;
import com.github.antilaby.antilaby.config.ConfigReader;
import com.github.antilaby.antilaby.lang.LanguageManager;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles incoming packages from clients
 */
public class IncomingPluginChannel implements PluginMessageListener, Listener {

	// TODO: Use the new configuration API

	private static final Logger LOGGER = new Logger("IncomingPluginChannel");

	private static final Pattern UUID_PATTERN = Pattern.compile("%UUID%");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%PLAYER%");
	/**
	 * A map of players who are using LabyMod
	 */
	private static HashMap<String, String> labyModPlayers = new HashMap<>();

	private ConfigReader configReader = new ConfigReader();

	/**
	 * Get a map of players who are using LabyMod
	 *
	 * @return HashMap with the player's UUID (as string) and name
	 */
	public static HashMap<String, String> getLabyModPlayers() {
		return labyModPlayers;
	}

	public static void setLabyModPlayers(HashMap<String, String> labyModPlayers) {
		IncomingPluginChannel.labyModPlayers = labyModPlayers;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if(channel.equals(Constants.LABYMOD_CHANNEL) || channel.equals(Constants.LABYMOD_CHANNEL_OLD)) {
			if(!labyModPlayers.containsKey(player.getUniqueId().toString())) {
				LOGGER.debug("Player " + player.getName() + " (" + player.getUniqueId().toString() + ") uses " + new String(data) + '!');
				labyModPlayers.put(player.getUniqueId().toString(), player.getName());
				// Send notification
				if(!configReader.getLabyModPlayerAction().kickEnabled()) for(Player all : Bukkit.getOnlinePlayers()) {
					if(all.hasPermission(Constants.PERMISSION_LABYINFO_NOTIFICATIONS)) {
						all.sendMessage(Constants.PREFIX + LanguageManager.INSTANCE.translate("antilaby.command.labyInfo.labyMod", all, player.getName()));
					}
				}
			}

			// Ban the player?
			if(configReader.getLabyModPlayerAction().getBan().isEnabled()) {
				if(configReader.getEnableBypassWithPermission()) {
					if(!player.hasPermission(Constants.PERMISSION_BYPASS)) {
						banPlayer(player);
						return;
					}
				} else {
					banPlayer(player);
					return;
				}
			}

			// Kick the player?
			if(configReader.getLabyModPlayerAction().kickEnabled()) {
				if(configReader.getEnableBypassWithPermission()) {
					if(!player.hasPermission(Constants.PERMISSION_BYPASS)) {
						kickPlayer(player);
						return;
					}
				} else {
					kickPlayer(player);
					return;
				}
			}

			// Join commands
			if(!player.hasPermission(Constants.PERMISSION_BYPASS_JOIN_COMMANDS)) {
				List<String> labyModJoinCommands = configReader.getLabyModPlayerAction().getJoinCommands(false);
				for(final String command : labyModJoinCommands) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), UUID_PATTERN.matcher(PLAYER_PATTERN.matcher(command).replaceAll(Matcher.quoteReplacement(player.getName())))
							.replaceAll(Matcher.quoteReplacement(player.getUniqueId().toString())));
				}
			}
		}
	}

	/**
	 * Kick a player (who is not allowed to use LabyMod)
	 *
	 * @param player
	 * 		The player who should be kicked
	 */
	private void kickPlayer(Player player) {
		player.kickPlayer(LanguageManager.INSTANCE.translate("labymod.playerKickMessage", player));
		LOGGER.info("Player " + player.getName() + " (" + player.getUniqueId().toString() + ") is not allowed to use LabyMod and has been kicked.");
		// Send notification
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(all.hasPermission(Constants.PERMISSION_LABYINFO_NOTIFICATIONS)) {
				all.sendMessage(Constants.PREFIX + LanguageManager.INSTANCE.translate("antilaby.notifyKickMessage", all, player.getName()));
			}
		}
	}

	/**
	 * Ban a player (who is not allowed to use LabyMod)
	 *
	 * @param player
	 * 		The player who should be banned
	 */
	private void banPlayer(Player player) {
		String banMessage = "LabyMod is not allowed!"; // TODO: Get message from language file
		String commandLine = configReader.getLabyModPlayerAction().getBan().getCommand();
		// Replace variables
		try {
			commandLine = commandLine.replaceAll("%PLAYER%", player.getName());
			commandLine = commandLine.replaceAll("%UUID%", player.getUniqueId().toString());
			commandLine = commandLine.replaceAll("%MESSAGE%", banMessage);
			commandLine = ChatColor.translateAlternateColorCodes('&', commandLine);
		} catch(Exception e) { /* Ignore */ }
		// Execute ban
		ExecutableCommand executableCommand = new ExecutableCommand(commandLine, Bukkit.getConsoleSender());
		executableCommand.execute();
		LOGGER.info("Player " + player.getName() + " (" + player.getUniqueId().toString() + ") is not allowed to use LabyMod and has been banned using the following command: '" +
				ChatColor.stripColor(commandLine) + "'");
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(labyModPlayers.containsKey(event.getPlayer().getUniqueId().toString())) labyModPlayers.remove(event.getPlayer().getUniqueId().toString());
	}

}
