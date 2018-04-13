package com.github.antilaby.antilaby.command;

import com.github.antilaby.antilaby.lang.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * The CommandBase is a superclass implementing both CommandExecutor and TabCompleter. It is self registering so it's
 * constructor requires you to give you a plugin and the commands name. <p> <p>It also provides native support for the
 * default language system while trying to follow mojangs style of doing stuff, so if your command is named "myCommand"
 * and your plugin is named "myPlugin", the usage translation would have to be named: myPlugin.command.myCommand.usage.
 * You only have to keep this in mind if you want to use the {@link #sendUsage} method or the {@link #prefix}
 * field.</p>
 *
 * @author heisluft
 * @see #sendUsage(CommandSender)
 * @see #CommandBase(JavaPlugin, String)
 */
public abstract class CommandBase implements CommandExecutor, TabCompleter {

	/** The Plugin command, only used for registering things and sending info */
	private final PluginCommand command;

	/**
	 * The commands prefix.
	 *
	 * @see #CommandBase(JavaPlugin, String)
	 * @see #sendUsage(CommandSender)
	 */
	protected final String prefix;

	/**
	 * Creates and registers a command with the given name. The name is registered in minecraft-style lowercase, the
	 * actual name is lowerCamelCased to meet minecrafts translation standards. The prefix will be set to
	 * "plugin.commandName", the usage to "plugin.commandName.usage".
	 *
	 * @param plugin
	 * 		the plugin to register this command for
	 * @param cmdName
	 * 		the commands name
	 */
	protected CommandBase(JavaPlugin plugin, String cmdName) {
		prefix = plugin.getName().toLowerCase() + ".command." + cmdName + ".";
		command = plugin.getCommand(cmdName.toLowerCase());
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	/**
	 * Send the usage of this command to a command sender. Note that if the CommandSender is a player, a localized
	 * message will be sent, else the plugin will send the usage stated in the plugin.yml file of your plugin
	 *
	 * @param sender
	 * 		the CommandSender to send the usage of this command to
	 */
	protected void sendUsage(CommandSender sender) {
		if(sender instanceof Player)
			sender.sendMessage(LanguageManager.INSTANCE.translate(prefix + "usage", (Player) sender));
		else sender.sendMessage(command.getUsage());
	}

	/**
	 * Get all tab completion option. Default implementation returns null. Special return cases: <ul>
	 * <li><code>null</code> - does not affect the completion behaviour</li> <li>empty list - disables completion</li>
	 * <li>non-empty list - each entry is treated as a tab completion option</li> </ul>
	 *
	 * @param sender
	 * 		the commands sender
	 * @param cmd
	 * 		the command name
	 * @param label
	 * 		the commands alias which was used
	 * @param args
	 * 		the arguments typed so far
	 *
	 * @return null in default implementation, else a list of all tab completion options for your command
	 */
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return null;
	}

}
