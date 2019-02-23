package com.github.antilaby.antilaby.api.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Format commands properly to execute them.
 *
 * @author NathanNr
 */
public class ExecutableCommand {

  private final String commandLine;
  private final CommandSender sender;

  public ExecutableCommand(String commandLine) {
    this(commandLine, Bukkit.getConsoleSender());
  }

  public ExecutableCommand(String commandLine, CommandSender sender) {
    this.commandLine = commandLine;
    this.sender = sender;
  }

  public boolean execute() {
    return execute(commandLine, sender);
  }

  public boolean execute(String commandLine) {
    return execute(commandLine, sender);
  }

  public boolean execute(CommandSender sender) {
    return execute(commandLine, sender);
  }

  /**
   * Execute the stored command.
   *
   * @param commandLine the command line string
   * @param sender the sender to execute the command as
   * @return whether the command could be executed
   */
  public boolean execute(String commandLine, CommandSender sender) {
    if (commandLine.startsWith("/")) {
      commandLine = commandLine.substring(1);
    } else if (commandLine.startsWith("#")) {
      commandLine = null;
    }
    return Bukkit.dispatchCommand(sender, commandLine);
  }
}
