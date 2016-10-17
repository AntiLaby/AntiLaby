package com.github.nathannr.antilaby.features;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabComplete implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("antilaby") && args.length == 1) {
			List<String> list = new ArrayList<>();
			list.add("info");
			list.add("reload");
			return list;
		} else if(cmd.getName().equalsIgnoreCase("antilaby")) {
			List<String> empty = new ArrayList<>();
			return empty;
		}
		
		return null;
	}

	

}
