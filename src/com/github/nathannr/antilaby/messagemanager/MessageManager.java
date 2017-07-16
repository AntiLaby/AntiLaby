package com.github.nathannr.antilaby.messagemanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {

	public static String replaceVariables(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);
		return message;
	}

	public static String replaceVariables(String message, Player player) {
		message = ChatColor.translateAlternateColorCodes('&', message);

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
				command = command.replace("%PLAYER%", player.getName());
				command = command.replace("%PLAYERUUID%", player.getUniqueId().toString());
			} catch (Exception e) {
			}
			if (command != null)
				convertedCommands.add(command);
		}
		return convertedCommands;
	}

}
