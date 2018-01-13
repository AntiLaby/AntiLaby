package com.github.antilaby.antilaby.api.config;

/**
 * Get the auto update settings from the configuration file.
 * 
 * @author NathanNr
 */
public class GetConfigUpdateSettings extends ConfigFile implements ConfigurationReader {

	private final String NAME = "Update";
	private final String PATH = "AntiLaby.Update.AutoUpdate";

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

	/**
	 * Are release auto updates enabled?
	 */
	public boolean getRelease() {
		return getCfg().getBoolean(PATH + ".Release");
	}

	/**
	 * Are beta auto updates enabled?
	 */
	public boolean getBeta() {
		return getCfg().getBoolean(PATH + ".Beta");
	}

}
