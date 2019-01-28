package com.github.antilaby.antilaby.api.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Format commands properly to execute them.
 *
 * @author NathanNr
 */
public class ExecutableCommand {

  private String commandLine;
  private CommandSender sender;

  public ExecutableCommand(String commandLine) {
    this.commandLine = format(commandLine);
  }

  public ExecutableCommand(String commandLine, CommandSender sender) {
    this.sender = sender;
  }

  public CommandSender getSender() {
    return sender;
  }

  public void setSender(CommandSender sender) {
    this.sender = sender;
  }

  public String getCommandLine() {
    return commandLine;
  }

  public void setCommandLine(String commandLine) {
    this.commandLine = format(commandLine);
  }

  public boolean execute() {
    if (sender == null)
      sender = Bukkit.getConsoleSender();
    return Bukkit.dispatchCommand(sender, commandLine);
  }

  public boolean execute(String commandLine) {
    this.commandLine = commandLine;
    return execute();
  }

  public boolean execute(CommandSender sender) {
    this.sender = sender;
    return execute();
  }

  public boolean execute(String commandLine, CommandSender sender) {
    this.commandLine = commandLine;
    this.sender = sender;
    return execute();
  }

  private String format(String commandLine) {
    if (commandLine.startsWith("/"))
      return commandLine.substring(1);
    if (commandLine.startsWith("#"))
      return null;
    return commandLine;
  }

}
