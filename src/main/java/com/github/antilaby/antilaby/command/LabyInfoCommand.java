package com.github.antilaby.antilaby.command;

import com.github.antilaby.antilaby.api.LabyPlayer;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LabyInfoCommand implements CommandExecutor {

	private static boolean commandAvailable = true;

	@Deprecated
	public static void setCommandAvailability() {
		commandAvailable = true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final LanguageManager lan = LanguageManager.INSTANCE;
		if(commandAvailable) {
			if(args.length == 1) {
				final Player targetPlayer = Bukkit.getPlayer(args[0]);
				if(Bukkit.getOnlinePlayers().contains(targetPlayer)) {
					final boolean uses = LabyPlayer.doesPlayerUseLabyMod(targetPlayer);
					if(sender instanceof Player) {
						final Player player = (Player) sender;
						if(player.hasPermission(Constants.PERMISSION_LABYINFO)) {
							if(uses)
								player.sendMessage(lan.translate("antilaby.command.labyInfo.labyMod", player, targetPlayer.getName()));
							else
								player.sendMessage(lan.translate("antilaby.command.labyInfo.noLabyMod", player, targetPlayer.getName()));
						} else {
							player.sendMessage(lan.translate("antilaby.command.noPermission", player));
							AntiLaby.LOG.debug("Player " + player.getName() + " (" + player.getUniqueId() + ") to use LabyInfo: Permission 'antilaby.labyinfo' is " + "missing!");
						}
					} else if(uses) sender.sendMessage("Player '" + args[0] + "' uses LabyMod.");
					else sender.sendMessage("Player '" + args[0] + "' doesn't use LabyMod.");
				} else if(sender instanceof Player) {
					final Player player = (Player) sender;
					player.sendMessage(lan.translate("antilaby.command.labyInfo.playerOffline", player, args[0]));
				} else AntiLaby.LOG.info("Player '" + args[0] + "' is offline!");
			} else if(sender instanceof Player) {
				final Player player = (Player) sender;
				player.sendMessage(lan.translate("antilaby.command.labyInfo.usage", player));
			} else sender.sendMessage("Usage: /labyinfo <player>");
		} else if(sender instanceof Player) {
			final Player player = (Player) sender;
			if(player.hasPermission(Constants.PERMISSION_LABYINFO))
				player.sendMessage(lan.translate("antilaby.command.labyInfo.reload", player));
			else player.sendMessage(lan.translate("antilaby.command.noPermission", player));
		} else AntiLaby.LOG.info("Sorry, but LabyInfo is currently not available.");
		return true;
	}

}
