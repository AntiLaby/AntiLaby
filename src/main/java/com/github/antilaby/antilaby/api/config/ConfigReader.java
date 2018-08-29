package com.github.antilaby.antilaby.api.config;

import java.util.List;

/**
 * Get the values of the configuration file.
 *
 * @author NathanNr
 */
public class ConfigReader {

	/**
	 * Read 'AntiLaby.LabyModFeatures.Enable' from the configuration file
	 *
	 * @return a list of all enabled LabyMod features
	 */
	public List<String> getEnabledFeatures() {
		return ConfigFile.getCfg().getStringList("AntiLaby.LabyModFeatures.Enable");
	}

	/**
	 * Read 'AntiLaby.LabyModFeatures.Disable' from the configuration file
	 *
	 * @return a list of all disabled LabyMod features
	 */
	public List<String> getDisabledFeatures() {
		return ConfigFile.getCfg().getStringList("AntiLaby.LabyModFeatures.Disable");
	}

	/**
	 * Read 'AntiLaby.EnableBypassWithPermission' from the configuration file
	 *
	 * @return true if the bypass for players with the bypass permission is enabled
	 */
	public boolean getEnableBypassWithPermission() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.EnableBypassWithPermission");
	}

	public ConfigReaderLabyModPlayerAction getLabyModPlayerAction() {
		return new ConfigReaderLabyModPlayerAction();
	}

	public List<String> getAdditionalPluginChannels() {
		return ConfigFile.getCfg().getStringList("AntiLaby.AdditionalPluginChannels");
	}

	public ConfigReaderAutoUpdate getAutoUpdate() {
		return new ConfigReaderAutoUpdate();
	}

	public List<String> getLabyModPlayerCommands() {
		return ConfigFile.getCfg().getStringList("AntiLaby.LabyModPlayerCommands");
	}

	public boolean getDebugMode() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.DebugMode");
	}

	public boolean getConfigVersion() {
		return ConfigFile.getCfg().getBoolean("AntiLaby.ConfigVersion");
	}

}
