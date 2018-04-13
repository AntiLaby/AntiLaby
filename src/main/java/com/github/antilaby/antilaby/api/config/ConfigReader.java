package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of a configuration section.
 * 
 * @author NathanNr
 */
public interface ConfigReader {

	/**
	 * Get the name of the configuration section.
	 * 
	 * @return name
	 */
	public String getName();

	/**
	 * Get the path of the configuration section in the configuration file.
	 * 
	 * @return path
	 */
	public String getPath();

}
