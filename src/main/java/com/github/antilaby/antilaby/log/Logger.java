package com.github.antilaby.antilaby.log;

import com.github.antilaby.antilaby.config.ConfigReader;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

/**
 * A helper class used for logging purposes to enhance logging a bit.
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
   * Logs the given message at ERROR level.
   *
   * @param message The log message
   */
  public void error(String message) {
    CONSOLE_COMMAND_SENDER.sendMessage("§4[AntiLaby/" + name + "] [ERROR]: " + message + "§r");
  }

  /**
   * Logs the given message at FATAL level.
   *
   * @param message The log message
   */
  public void fatal(String message) {
    CONSOLE_COMMAND_SENDER.sendMessage("§4[AntiLaby/" + name + "] [FATAL]: " + message + "§r");
  }

  /**
   * Logs the given message at INFO level.
   *
   * @param message The log message
   */
  public void info(String message) {
    CONSOLE_COMMAND_SENDER.sendMessage("[AntiLaby/" + name + "] [INFO]: " + message + "§r");
  }

  /**
   * Logs the given message at WARN level.
   *
   * @param message The log message
   */
  public void warn(String message) {
    CONSOLE_COMMAND_SENDER.sendMessage("§e[AntiLaby/" + name + "] [WARN]: " + message + "§r");
  }

  /**
   * Logs the given message at DEBUG level.
   *
   * @param message The log message
   */
  public void debug(String message) {
    if (configReader.getDebugMode()) {
      CONSOLE_COMMAND_SENDER.sendMessage("[AntiLaby/" + name + "] [DEBUG]: " + message + "§r");
    }
  }
}
