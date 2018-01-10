package com.github.antilaby.antilaby.api.config;

/**
 * Get the LabyMod settings from the configuration file.
 * 
 * @author NathanNr
 */

public class GetConfigLabySettings implements ConfigurationReader {

	private final String PATH = "AntiLaby.LabyModFeature";

	@Override
	public String getPath() {
		return PATH;
	}

}
