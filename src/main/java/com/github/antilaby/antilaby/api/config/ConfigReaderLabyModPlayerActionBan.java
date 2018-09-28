package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of the configuration file from the LabyMod Player Ban section.
 *
 * @author NathanNr
 */
public class ConfigReaderLabyModPlayerActionBan {

	public boolean isEnabled() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerAction.Ban.Enable");
	}

	public String getCommand() {
		return ConfigFile.getCfg().getString("AntiLaby.LabyModPlayerAction.Ban.Command");
	}

}
