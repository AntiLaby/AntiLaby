package com.github.antilaby.antilaby.api.config;

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
	public boolean getLabyModPlayerKickEnabled() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerAction.Kick.Enabled");
	}

	/**
	 * Read 'AntiLaby.LabyModPlayerAction.Ban.Enabled' from the configuration file
	 *
	 * @return true if the LabyModPlayerAction Ban is enabled
	 */
	public ConfigReaderLabyModPlayerAction getLabyModPlayerBan() {
		return new ConfigReaderLabyModPlayerAction();
	}

}
