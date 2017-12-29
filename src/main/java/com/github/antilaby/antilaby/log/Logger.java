package com.github.antilaby.antilaby.log;

import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * A helper class used for logging purposes to enhance logging a bit
 *
 * @author heisluft
 */
public class Logger {
	
	private static final ConsoleCommandSender c = Bukkit.getConsoleSender();
	private final String name;
	
	/**
	 * Constructs a Logger with a given name.
	 *
	 * @param name
	 * 		The name of the Logger
	 */
	public Logger(String name) {
		this.name = name;
	}
	
	/**
	 * Logs the given message at {@link Level#ERROR ERROR} level
	 *
	 * @param message
	 * 		The log message
	 */
	public void error(String message) {
		log(Level.ERROR, message);
	}
	
	/**
	 * Logs the given message with the specified {@link Level}
	 *
	 * @param level
	 * 		The Level to use
	 * @param message
	 * 		The log message
	 */
	public void log(Level level, String message) {
		final String color = level == Level.ERROR || level == Level.FATAL ? "§4" : level == Level.WARN ? "§e" : "";
		c.sendMessage(color + "[AntiLaby/" + name + "] [" + level.name() + "]: " + message + "§r");
	}
	
	/**
	 * Logs the given message at {@link Level#FATAL FATAL} level
	 *
	 * @param message
	 * 		The log message
	 */
	public void fatal(String message) {
		log(Level.FATAL, message);
	}
	
	/**
	 * Logs the given message at {@link Level#INFO INFO} level
	 *
	 * @param message
	 * 		The log message
	 */
	public void info(String message) {
		log(Level.INFO, message);
	}
	
	/**
	 * Logs the given message at {@link Level#WARN WARN} level
	 *
	 * @param message
	 * 		The log message
	 */
	public void warn(String message) {
		log(Level.WARN, message);
	}
	
}
