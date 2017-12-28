package com.github.antilaby.antilaby.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AntiLabyTabComplete implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		// Tab-complete for the AntiLaby debug command
		if(cmd.getName().equalsIgnoreCase("antilaby") && args.length == 1) {
			List<String> list = new ArrayList<>(2);
			list.add("info");
			list.add("reload");
			return list;
		} else if(cmd.getName().equalsIgnoreCase("antilaby")) {
			return new ArrayList<>(0);
		}
		
		return null;
	}
	
}
