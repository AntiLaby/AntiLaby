package com.github.nathannr.antilaby.messagemanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.main.AntiLaby;

public class MessageManager {

	public static String getAsChatMessage(String message) {
		message = replaceVariables(message);
		message = Prefix.PREFIX + message;
		return message;
	}
	
	public static String getAsChatMessage(String message, Player player) {
		message = replaceVariables(message, player);
		message = Prefix.PREFIX + message;
		return message;
	}

	public static String replaceVariables(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}

	public static String replaceVariables(String message, Player player) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		try {
			message = replacePlayer(message, player);
		} catch (Exception e) {
		}
		return message;
	}

	public static List<String> getAsCommands(List<String> commands, Player player) {
		ArrayList<String> convertedCommands = new ArrayList<>();
		for (String command : commands) {
			try {
				if (command.startsWith("#")) {
					command = null;
				}
				command = command.replace("/", "");
				command = replacePlayer(command, player);
			} catch (Exception e) {
			}
			if (command != null)
				convertedCommands.add(command);
		}
		return convertedCommands;
	}

	private static String replacePlayer(String message, Player player) {
		message = message.replace("%PLAYER%", player.getName());
		message = message.replace("%PLAYERUUID%", player.getUniqueId().toString());
		return message;
	}
	
	public static String getNoPermissionMessage(Player player) {
		return getAsChatMessage(AntiLaby.getInstance().getMultiLanguage().getMultiLanguageMessage(player, "NoPermission", true));
	}

}
