package com.github.antilaby.antilaby.api.config;

/**
 * Get the LabyMod settings from the configuration file.
 * 
 * @author NathanNr
 */
public class GetConfigLabySettings extends ConfigFile implements ConfigurationReader {

	private final String NAME = "LabyModFeatures";
	private final String PATH = "AntiLaby.LabyModFeature";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getPath() {
		return PATH;
	}

	@Override
	public Object getValue(String id) {
		return getCfg().getBoolean(PATH + "." + id);
	}

}
