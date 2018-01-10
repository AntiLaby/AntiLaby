package com.github.antilaby.antilaby.api.config;

/**
 * Get the LabyMod settings from the configuration file.
 * 
 * @author NathanNr
 */

public class GetConfigUpdateSettings implements ConfigurationReader {

	private final String PATH = "AntiLaby.Update";

	@Override
	public String getPath() {
		return PATH;
	}

}
