package com.github.antilaby.antilaby.api.config;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Get the values of the configuration file.
 * 
 * @author NathanNr
 */
public class ConfigReader {

	/**
	 * Get all LabyMod features which have been allowed in the configuration file.
	 * 
	 * @return enabled LabyMod features
	 */
	public List<String> getEnabledFeatures() {
	    return ConfigFile.getCfg().getStringList("AntiLaby.LabyModFeatures.Enable");
	}

	/**
	 * Get all LabyMod features which have been blocked in the configuration file.
	 * 
	 * @return disabled LabyMod features
	 */
	public List<String> getDisabledFeatures() {
        return ConfigFile.getCfg().getStringList("AntiLaby.LabyModFeatures.Disable");
	}

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
