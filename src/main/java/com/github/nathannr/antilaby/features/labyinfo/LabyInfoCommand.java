package com.github.nathannr.antilaby.features.labyinfo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyPlayer;
import com.github.nathannr.antilaby.api.util.Permission;
import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.main.AntiLaby;
import com.github.nathannr.antilaby.messagemanager.MessageManager;

public class LabyInfoCommand implements CommandExecutor {

	private static boolean commandAvailable = true;

	public static void setCommandAvailability() {
		if (!Bukkit.getOnlinePlayers().isEmpty()) commandAvailable = false;
		else commandAvailable = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (commandAvailable) {
			if (args.length == 1) {
				final Player targetPlayer = Bukkit.getPlayer(args[0]);
				if (Bukkit.getOnlinePlayers().contains(targetPlayer)) {
					final LabyPlayer labyPlayer = new LabyPlayer(targetPlayer);
					if (sender instanceof Player) {
						final Player player = (Player) sender;
						if (player.hasPermission(Permission.LABYINFO_PERMISSION)) {
							if (labyPlayer.usesLabyMod()) player.sendMessage(
									MessageManager.getAsChatMessage(AntiLaby.getInstance().getMultiLanguage()
											.getMultiLanguageMessage(player, "LabyInfo.LabyMod", true), targetPlayer));
							else player.sendMessage(MessageManager.getAsChatMessage(AntiLaby.getInstance()
									.getMultiLanguage().getMultiLanguageMessage(player, "LabyInfo.NoLabyMod", true),
									targetPlayer));
						} else {
							player.sendMessage(MessageManager.getNoPermissionMessage(player));
							System.out.println(
									Prefix.CPREFIXINFO + "Player " + player.getName() + " (" + player.getUniqueId()
											+ ") to use LabyInfo: Permission 'antilaby.labyinfo' is missing!");
						}
					} else if (labyPlayer.usesLabyMod()) sender.sendMessage("Player '" + args[0] + "' uses LabyMod.");
					else sender.sendMessage("Player '" + args[0] + "' doesn't use LabyMod.");
				} else if (sender instanceof Player) {
					final Player player = (Player) sender;
					player.sendMessage(MessageManager.getAsChatMessage(AntiLaby.getInstance().getMultiLanguage()
							.getMultiLanguageMessage(player, "LabyInfo.PlayerOffline", true), player));
				} else sender.sendMessage(Prefix.CPREFIXERROR + "Player '" + args[0] + "' is offline!");
			} else if (sender instanceof Player) {
				final Player player = (Player) sender;
				player.sendMessage(MessageManager.getAsChatMessage("§cUsage: /labyinfo <player>§r"));
			} else sender.sendMessage("Usage: /labyinfo <player>");
		} else if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (player.hasPermission(Permission.LABYINFO_PERMISSION)) {
				if (player.getLocale().equalsIgnoreCase("de_de")) player.sendMessage(MessageManager.getAsChatMessage(
						"§cMomentan ist LabyInfo nach einem Server-Reload nicht verfügbar. Bitte starte den Server neu, um LabyInfo nutzen zu können! In einem zukünftigen Update wird diese Funktion verfügbar sein.§r"));
				else player.sendMessage(MessageManager.getAsChatMessage(
						"§cSorry, but LabyInfo is currently not available after a server reload. Please restart your server to use LabyInfo! Reload-support will be available in a future update.§r"));
			} else player.sendMessage(MessageManager.getNoPermissionMessage(player));
		} else sender.sendMessage(Prefix.CPREFIXINFO
				+ "Sorry, but LabyInfo is currently not available after a server reload. Please restart your server to use LabyInfo! Reload-support will be available in a future update.");
		return true;
	}

}
