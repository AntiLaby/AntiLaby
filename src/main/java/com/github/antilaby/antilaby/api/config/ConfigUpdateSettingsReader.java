package com.github.antilaby.antilaby.api.config;

/**
 * Get the auto update settings from the configuration file.
 *
 * @author NathanNr
 */
public class ConfigUpdateSettingsReader extends ConfigFile implements ConfigReader {

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

	/**
	 * Are release auto updates enabled?
	 *
	 * @return whether auto-update of releases is enabled
	 */
	public boolean getRelease() {
		return getCfg().getBoolean(PATH + ".Release");
	}

	/**
	 * Are beta auto updates enabled?
	 *
	 * @return whether auto-update is enabled
	 */
	public boolean getBeta() {
		return getCfg().getBoolean(PATH + ".Beta");
	}

}
