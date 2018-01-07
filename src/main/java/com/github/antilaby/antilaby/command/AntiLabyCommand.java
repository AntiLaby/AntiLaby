package com.github.antilaby.antilaby.command;

import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class AntiLabyCommand extends CommandBase {

	public AntiLabyCommand() {
		super(AntiLaby.getInstance(), "antiLaby");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// AntiLaby debug command
		if(cmd.getName().equalsIgnoreCase("antilaby")) if(args.length != 1) sendUsage(sender);
		else {
			if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl"))
				AntiLaby.getInstance().reloadPlugin(sender);
			else if(args[0].equalsIgnoreCase("info")) AntiLaby.getInstance().sendInfo(sender);
			else sendUsage(sender);
		}
		else sendUsage(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(args.length == 1) {
			List<String> list = new ArrayList<>(2);
			list.add("info");
			list.add("reload");
			return list;
		}
		return new ArrayList<>(0);
	}
}
