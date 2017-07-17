package com.github.nathannr.antilaby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.util.Prefix;
import com.github.nathannr.antilaby.main.AntiLaby;

public class AntiLabyCommand implements CommandExecutor {

	private AntiLaby plugin;

	public AntiLabyCommand(AntiLaby plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// AntiLaby debug command
		if (cmd.getName().equalsIgnoreCase("antilaby")) {
			if (args.length != 1) {
				sendUsage(sender);
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
					plugin.reloadPlugin(sender);
				} else if (args[0].equalsIgnoreCase("info")) {
					plugin.sendInfo(sender);
				} else {
					sendUsage(sender);
				}
			} else {
				sendUsage(sender);
			}
		}
		return true;
	}

	public void sendUsage(CommandSender sender) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(Prefix.PREFIX + "§cUsage: /antilaby <info|reload>§r");
		} else {
			sender.sendMessage(Prefix.CPREFIXINFO + "Usage: /antilaby <info|reload>");
		}
	}
}
