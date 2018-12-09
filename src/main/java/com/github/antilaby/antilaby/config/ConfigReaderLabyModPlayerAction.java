package com.github.antilaby.antilaby.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Get the values of the configuration file from the LabyModPlayerAction section.
 *
 * @author NathanNr
 */
public class ConfigReaderLabyModPlayerAction {

	/**
	 * Read 'AntiLaby.LabyModPlayerAction.Kick.Enabled' from the configuration file
	 *
	 * @return true if the LabyModPlayerAction Kick is enabled
	 */
	public boolean kickEnabled() {
		return ConfigFile.getFileConfiguration().getBoolean("AntiLaby.LabyModPlayerAction.Kick.Enabled");
	}

	public ConfigReaderLabyModPlayerActionBan getBan() {
		return new ConfigReaderLabyModPlayerActionBan();
	}

	/**
	 * Get the LabyModPlayerJoinCommands from the configuration file
	 *
	 * @return list of commands without comments
	 */
	public List<String> getJoinCommands() {
		return getJoinCommands(false);
	}

	/**
	 * Get the LabyModPlayerJoinCommands from the configuration file
	 *
	 * @param includeComments true if comments should be included
	 * @return list of commands
	 */
	public List<String> getJoinCommands(boolean includeComments) {
		List<String> commandsComments = ConfigFile.getFileConfiguration().getStringList("AntiLaby.LabyModPlayerAction.ExecuteCommands");
		if (!includeComments) {
			List<String> commands = new ArrayList<>();
			for (final String command : commandsComments)
				if (!command.startsWith("#"))
					commands.add(command);
			return commands;
		} else {
			return commandsComments;
		}
	}

}
