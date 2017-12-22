package com.github.antilaby.antilaby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;

import java.util.ArrayList;
import java.util.List;

public final class AntiLabyCommand implements CommandExecutor, TabCompleter {

	private final AntiLaby plugin;

	public AntiLabyCommand(AntiLaby plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// AntiLaby debug command
		if (cmd.getName().equalsIgnoreCase("antilaby")) if (args.length != 1) sendUsage(sender);
		else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) plugin.reloadPlugin(sender);
			else if (args[0].equalsIgnoreCase("info")) plugin.sendInfo(sender);
			else sendUsage(sender);
		} else sendUsage(sender);
		return true;
	}

	public void sendUsage(CommandSender sender) {
		if (sender instanceof Player) {
			final Player p = (Player) sender;
			p.sendMessage(LanguageManager.INSTANCE.translate("antilaby.command.antiLaby.usage", p));
		} else sender.sendMessage("Usage: /antilaby <info|reload>");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		// Tab-complete for the AntiLaby debug command
		if (cmd.getName().equalsIgnoreCase("antilaby") && args.length == 1) {
			List<String> list = new ArrayList<>();
			list.add("info");
			list.add("reload");
			return list;
		} else if (cmd.getName().equalsIgnoreCase("antilaby")) {
			List<String> empty = new ArrayList<>();
			return empty;
		}

		return null;
	}
}
