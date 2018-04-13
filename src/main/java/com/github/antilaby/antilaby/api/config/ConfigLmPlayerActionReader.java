package com.github.antilaby.antilaby.api.config;

/**
 * Read the LabyModPlayerAction settings from the configuration file.
 *
 * @author NathanNr
 */
public class ConfigLmPlayerActionReader extends ConfigFile implements ConfigReader {

	private final String NAME = "LabyModPlayerAction";
	private final String PATH = "AntiLaby.LabyModPlayerAction";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getPath() {
		return PATH;
	}

	/**
	 * Is LabyModPlayerKick enabled?
	 * 
	 * @return whether LabyModPlayerKick is enabled
	 */
	public boolean getLabyModPlayerKickEnabled() {
		return getCfg().getBoolean(PATH + ".Kick.Enabled");
	}

	/**
	 * Is LabyModPlayerBan enabled?
	 * 
	 * @return whether LabyModPlayerBan is enabled
	 */
	public boolean getLabyModPlayerBanEnabled() {
		return getCfg().getBoolean(PATH + ".Ban.Enabled");
	}

}