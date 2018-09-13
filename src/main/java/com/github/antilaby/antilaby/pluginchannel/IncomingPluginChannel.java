package com.github.antilaby.antilaby.pluginchannel;

import com.github.antilaby.antilaby.api.LabyModJoinCommands;
import com.github.antilaby.antilaby.api.command.ExecutableCommand;
import com.github.antilaby.antilaby.api.config.ConfigReader;
import com.github.antilaby.antilaby.api.config.LabyModPlayerBanMethod;
import com.github.antilaby.antilaby.api.exceptions.InternalException;
import com.github.antilaby.antilaby.config.Config;
import com.github.antilaby.antilaby.lang.LanguageManager;
import com.github.antilaby.antilaby.log.Logger;
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

	// TODO: Use the new configuration API

	public static final String LOCATION = "IncomingPackageHandler";

	private static final Pattern UUID_PATTERN = Pattern.compile("%UUID%");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%PLAYER%");
	private static HashMap<String, String> labyModPlayers = new HashMap<>();

	private ConfigReader configReader = new ConfigReader();
	private Logger logger = new Logger(LOCATION);

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
				AntiLaby.LOG.debug(
						"Player " + player.getName() + " (" + player.getUniqueId().toString() + ") uses " + new String(
								data) + '!');
				labyModPlayers.put(player.getUniqueId().toString(), player.getName());
				// Send notification
				if(!Config.getLabyModPlayerKickEnable()) for(Player all : Bukkit.getOnlinePlayers()) {
					if(all.hasPermission(Constants.PERMISSION_LABYINFO_NOTIFICATIONS)) {
						all.sendMessage(Constants.PREFIX + LanguageManager.INSTANCE.translate(
								"antilaby.command.labyInfo.labyMod", all, player.getName()));
					}
				}
			}
			// Already the new config API:
			if(configReader.getLabyModPlayerAction().getBan().isEnabled()) {
				LabyModPlayerBanMethod labyModPlayerBanMethod = configReader.getLabyModPlayerAction().getBan().getMethod();
				switch (labyModPlayerBanMethod) {
					case DISABLED:
						throw new InternalException(LOCATION, "Two opposing settings for LabyMod Player Ban have been detected.", null);
					case BUILT_IN:
						// TODO: Implement built-in ban system
						break;
					case CUSTOM:
						String banMessage = "LabyMod is not allowed!" /*TODO: Get message from language file*/;
						String commandLine = configReader.getLabyModPlayerAction().getBan().getCustomCommand();
						try {
							commandLine = commandLine.replaceAll("%PLAYER%", player.getName());
							commandLine = commandLine.replaceAll("%UUID%", player.getUniqueId().toString());
							commandLine = commandLine.replaceAll("%MESSAGE%", banMessage);
						} catch (Exception e) {
							// Ignore
						}
						new ExecutableCommand(commandLine, Bukkit.getConsoleSender()).execute();
						break;
					case UNKNOWN:
						logger.warn("LabyMod Player Ban method is unknown! The vanilla ban system will be used.");
					case VANILLA:
						new ExecutableCommand("ban " + player.getName() + " LabyMod is not allowed!" /*TODO: Get message from language file*/, Bukkit.getConsoleSender()).execute();
						break;
				}
			}
			// TODO: new API!!
			if(Config.getLabyModPlayerKickEnable()) {
				if (AntiLaby.getInstance().getConfig().getString("AntiLaby.EnableBypassWithPermission").equals("true")) {
					if (!player.hasPermission(Constants.PERMISSION_BYPASS)) {
						kickPlayer(player);
						return;
					}
				} else {
					kickPlayer(player);
					return;
				}
			}
			if(!player.hasPermission(Constants.PERMISSION_BYPASS_JOIN_COMMANDS)) {
				LabyModJoinCommands labyModJoinCommands = new LabyModJoinCommands();
				for(final String command : labyModJoinCommands.getLabyModJoinCommands(false)) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), UUID_PATTERN.matcher(
							PLAYER_PATTERN.matcher(command).replaceAll(
									Matcher.quoteReplacement(player.getName()))).replaceAll(
							Matcher.quoteReplacement(player.getUniqueId().toString())));
				}
			}
		}
	}

	private void kickPlayer(Player player) {
		player.kickPlayer(LanguageManager.INSTANCE.translate("labymod.playerKickMessage", player));
		AntiLaby.LOG.info(
				"Player " + player.getName() + " (" + player.getUniqueId().toString() + ") is not allowed to use " +
						"LabyMod and has been kicked.");
		// Send notification
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(all.hasPermission(Constants.PERMISSION_LABYINFO_NOTIFICATIONS)) {
				all.sendMessage(Constants.PREFIX + LanguageManager.INSTANCE.translate("antilaby.notifyKickMessage",
						all,
						player.getName()));
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(labyModPlayers.containsKey(event.getPlayer().getUniqueId().toString()))
			labyModPlayers.remove(event.getPlayer().getUniqueId().toString());
	}

}
