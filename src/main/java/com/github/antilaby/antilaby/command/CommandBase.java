package com.github.antilaby.antilaby.command;

import com.github.antilaby.antilaby.lang.impl.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class CommandBase implements CommandExecutor, TabCompleter {

	private final PluginCommand command;
	protected final String prefix;

	protected CommandBase(JavaPlugin plugin, String cmdName) {
		prefix = plugin.getName().toLowerCase() + ".command." + cmdName + ".";
		command = plugin.getCommand(cmdName.toLowerCase());
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	protected void sendUsage(CommandSender sender) {
		if(sender instanceof Player)
			sender.sendMessage(LanguageManager.INSTANCE.translate(prefix + "usage", (Player) sender));
		else sender.sendMessage(command.getUsage());
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return null;
	}
}
