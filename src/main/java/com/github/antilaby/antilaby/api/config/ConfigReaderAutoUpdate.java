package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of the configuration file from the AutoUpdate section.
 *
 * @author NathanNr
 */
public class ConfigReaderAutoUpdate {

	/**
	 * Read 'AntiLaby.Update.AutoUpdate.Release' from the configuration file
	 *
	 * @return true if auto update for release versions is enabled
	 */
	public boolean autoUpdateReleaseEnabled() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.Update.AutoUpdate.Release");
	}

	/**
	 * Read 'AntiLaby.Update.AutoUpdate.Beta' from the configuration file
	 *
	 * @return true if auto update for beta versions is enabled
	 */
	public boolean autoUpdateBetaEnabled() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.Update.AutoUpdate.Beta");
	}

}
