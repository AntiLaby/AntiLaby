package com.github.antilaby.antilaby.command;

import com.comphenix.protocol.PacketType;
import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LabyInfoCommand extends CommandBase {

	public LabyInfoCommand() {
		super(AntiLaby.getInstance(), "labyInfo");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final LanguageManager lan = LanguageManager.INSTANCE;
		if(args.length == 1) {
			final Player targetPlayer = Bukkit.getPlayer(args[0]);
			if(Bukkit.getOnlinePlayers().contains(targetPlayer)) {
				final boolean uses = IncomingPluginChannel.getLabyModPlayers().containsKey(
						targetPlayer.getUniqueId().toString());
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(player.hasPermission(Constants.PERMISSION_LABYINFO)) {
						if(uses) player.sendMessage(lan.translate(prefix + "labyMod", player, targetPlayer.getName()));
						else player.sendMessage(lan.translate(prefix + "noLabyMod", player, targetPlayer.getName()));
					} else {
						player.sendMessage(Constants.PREFIX + lan.translate("antilaby.command.noPermission", player));
						AntiLaby.LOG.debug(
								"Player " + player.getName() + " (" + player.getUniqueId() + ") to use LabyInfo: " +
										"Permission 'antilaby.labyinfo' is " + "missing!");
					}
				} else if(uses) sender.sendMessage(Constants.PREFIX + "Player '" + args[0] + "' uses LabyMod.");
				else sender.sendMessage(
							Constants.PREFIX + lan.translate(prefix + "playerOffline", (Player) sender, args[0]));
			} else if(sender instanceof Player) {
				sender.sendMessage(
						Constants.PREFIX + lan.translate(prefix + "playerOffline", (Player) sender, args[0]));
			} else AntiLaby.LOG.info("Player '" + args[0] + "' is offline!");
		} else sendUsage(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return args.length == 1 ? null : new ArrayList<>();
	}
}
