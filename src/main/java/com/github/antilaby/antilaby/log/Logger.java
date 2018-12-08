package com.github.antilaby.antilaby.log;

import com.github.antilaby.antilaby.api.config.ConfigReader;
import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * A helper class used for logging purposes to enhance logging a bit
 *
 * @author heisluft
 */
public class Logger {

	private static final ConsoleCommandSender CONSOLE_COMMAND_SENDER = Bukkit.getConsoleSender();
	private static final ConfigReader configReader = new ConfigReader();
	private final String name;

	/**
	 * Constructs a Logger with a given name.
	 *
	 * @param name The name of the Logger
	 */
	public Logger(String name) {
		this.name = name;
	}

	/**
	 * Logs the given message at {@link Level#ERROR ERROR} level
	 *
	 * @param message The log message
	 */
	public void error(String message) {
		log(Level.ERROR, message);
	}

	/**
	 * Logs the given message with the specified {@link Level}
	 *
	 * @param level   The Level to use
	 * @param message The log message
	 */
	public void log(Level level, String message) {
		final String color = level == Level.ERROR || level == Level.FATAL ? "\u00A74"
				: level == Level.WARN ? "\u00A7e" : level == Level.DEBUG ? "\u00A71" : "";
		if (level == Level.DEBUG && !configReader.getDebugMode())
			return;
		CONSOLE_COMMAND_SENDER.sendMessage(color + "[AntiLaby/" + name + "] [" + level.name() + "]: " + message + "\u00A7r");
	}

	/**
	 * Logs the given message at {@link Level#FATAL FATAL} level
	 *
	 * @param message The log message
	 */
	public void fatal(String message) {
		log(Level.FATAL, message);
	}

	/**
	 * Logs the given message at {@link Level#INFO INFO} level
	 *
	 * @param message The log message
	 */
	public void info(String message) {
		log(Level.INFO, message);
	}

	/**
	 * Logs the given message at {@link Level#WARN WARN} level
	 *
	 * @param message The log message
	 */
	public void warn(String message) {
		log(Level.WARN, message);
	}

	/**
	 * Logs the given message at {@link Level#DEBUG DEBUG} level
	 *
	 * @param message The log message
	 */
	public void debug(String message) {
		log(Level.DEBUG, message);
	}
}
