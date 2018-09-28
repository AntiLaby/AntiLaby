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
	public boolean kickEnabled() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.LabyModPlayerAction.Kick.Enabled");
	}

	public ConfigReaderLabyModPlayerActionBan getBan() {
		return new ConfigReaderLabyModPlayerActionBan();
	}

}
