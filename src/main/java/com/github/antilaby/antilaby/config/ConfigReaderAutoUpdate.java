package com.github.antilaby.antilaby.config;

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
	public boolean release() {
		return ConfigFile.getFileConfiguration().getBoolean("AntiLaby.Update.AutoUpdate.Release");
	}

	/**
	 * Read 'AntiLaby.Update.AutoUpdate.Beta' from the configuration file
	 *
	 * @return true if auto update for beta versions is enabled
	 */
	public boolean beta() {
		return ConfigFile.getFileConfiguration().getBoolean("AntiLaby.Update.AutoUpdate.Beta");
	}

	/**
	 * Read 'AntiLaby.Update.AutoUpdate.Test' from the configuration file
	 * Only for testing the update system
	 * The default value will only be generated, if the {@link com.github.antilaby.antilaby.api.updater.VersionType} equals dev
	 *
	 * @return true if auto update for test versions is enabled
	 */
	public boolean test() {
		return ConfigFile.getFileConfiguration().getBoolean("AntiLaby.Update.AutoUpdate.Test");
	}

}
