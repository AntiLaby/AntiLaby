package com.github.antilaby.antilaby.api.config;

import java.util.List;

/**
 * Get the LabyMod settings from the configuration file.
 * 
 * @author NathanNr
 */
public class ConfigLabySettingsReader extends ConfigFile implements ConfigReader {

	private final String NAME = "LabyModFeatures";
	private final String PATH = "AntiLaby.LabyModFeatures";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getPath() {
		return PATH;
	}

	/**
	 * Get the list of LabyMod functions, that are enabled in the configuration file.
	 * @return enabled LabyMod functions
	 */
	public List<String> getEnabledFeatures() {
		return getCfg().getStringList(PATH + ".Enable");
	}

	/**
	 * Get the list of LabyMod functions, that are disabled in the configuration file.
	 * @return disabled LabyMod functions
	 */
	public List<String> getDisabledFeatures() {
		return getCfg().getStringList(PATH + ".Disable");
	}

}