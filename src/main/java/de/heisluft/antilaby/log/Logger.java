package de.heisluft.antilaby.log;

import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class Logger {
	
	private static final ConsoleCommandSender c = Bukkit.getConsoleSender();
	private final String name;

	public Logger() {
		this(Thread.currentThread().getStackTrace()[2].getClassName());
	}
	
	public Logger(Class<?> src) {
		this(src.getName());
	}
	
	public Logger(String name) {
		this.name = name;
	}

	public void log(Level level, String log) {
		final String color = level == Level.ERROR || level == Level.FATAL ? "§4" : level == Level.WARN ? "§e" : "";
		c.sendMessage(color + "[AntiLaby/" + name + "] [" + level.name() + "]: " + log + "§r");
	}
	
}
 