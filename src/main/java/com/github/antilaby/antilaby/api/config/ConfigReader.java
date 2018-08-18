package com.github.antilaby.antilaby.api.config;

import com.github.antilaby.antilaby.api.LabyModFeature;

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
		// TODO
		return false;
	}

	public ConfigReaderLabyModPlayerAction getLabyModPlayerAction() {
		return new ConfigReaderLabyModPlayerAction();
	}

	public ArrayList<String> getAdditionalPluginChannels() {
		// TODO
		return null;
	}

	public ConfigReaderAutoUpdate getAutoUpdate() {
		return new ConfigReaderAutoUpdate();
	}

	public ArrayList<String> getLabyModPlayerCommands() {
	    // TODO
        return null;
    }

}
