package com.github.antilaby.antilaby.command;

import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.config.ConfigFile;
import com.github.antilaby.antilaby.lang.LanguageManager;
import com.github.antilaby.antilaby.main.AntiLaby;

import static com.github.antilaby.antilaby.main.AntiLaby.LOG;

import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AntiLabyCommand extends CommandBase {

	private static final List<String> CMD_INFO = new ArrayList<>();

	static {
		CMD_INFO.add("&1-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-");
		CMD_INFO.add("&9AntiLaby plugin version " + AntiLaby.getInstance().getDescription().getVersion() + " by the " + "AntiLaby Team&r");
		CMD_INFO.add("&9More information about the plugin: " + Constants.RESOURCE_LINK + "&r");
		CMD_INFO.add("&9Use '/antilaby reload' to reload the plugin.&r");
		CMD_INFO.add("&9Use '/labyinfo <player>' to check if a player uses LabyMod.&r");
		CMD_INFO.add("&1-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-&r");
	}

	public AntiLabyCommand() {
		super(AntiLaby.getInstance(), "antiLaby");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("antilaby")) if(args.length != 1) sendUsage(sender);
		else {
			if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) reloadPlugin(sender);
			else if(args[0].equalsIgnoreCase("info")) sendInfo(sender);
			else sendUsage(sender);
		}
		else sendUsage(sender);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if(args.length == 1) {
			List<String> list = new ArrayList<>(2);
			if("info".startsWith(args[0])) list.add("info");
			if("reload".startsWith(args[0])) list.add("reload");
			return list;
		}
		return new ArrayList<>(0);
	}

	/**
	 * Send information about this plug-in to a command sender (console / player)
	 */
	private void sendInfo(CommandSender sender) {
		CMD_INFO.forEach(s -> {
			if(sender instanceof Player) sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			else LOG.info(s.replaceAll("&([0-9]|r|[a-f])", ""));
		});
	}

	/**
	 * Reload the plug-in as instructed by the given CommandSender
	 *
	 * @param sender
	 * 		the sender requesting the reload
	 */
	private void reloadPlugin(CommandSender sender) {
		if(sender instanceof Player) {
			final Player player = (Player) sender;
			if(!player.hasPermission("antilaby.reload")) {
				player.sendMessage(LanguageManager.INSTANCE.translate("antilaby.command.noPermission", player));
				LOG.info("Player " + player.getName() + " (" + player.getUniqueId() + ") tried to reload AntiLaby: " + "Permission 'antilaby.reload' is missing!");
				return;
			}
		}
		if(sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Constants.PREFIX + LanguageManager.INSTANCE.translate(prefix + "reload.start", player));
			LOG.info(player.getName() + " (" + player.getUniqueId() + "): Reloading AntiLaby...");
		} else LOG.info("Reloading AntiLaby...");
		ConfigFile.load();
		for(final Player all : Bukkit.getOnlinePlayers())
			new AntiLabyPackager(all).sendPackages();
		if(sender instanceof Player) {
			final Player player = (Player) sender;
			player.sendMessage(Constants.PREFIX + LanguageManager.INSTANCE.translate(prefix + "reload.complete", player));
			LOG.info(player.getName() + " (" + player.getUniqueId() + "): Reload complete!");
		} else LOG.info("Reload complete!");
	}

}
