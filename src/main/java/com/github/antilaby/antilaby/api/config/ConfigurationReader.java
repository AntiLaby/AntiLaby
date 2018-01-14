package com.github.antilaby.antilaby.api.config;

/**
 * Get the values of a configuration section.
 * 
 * @author NathanNr
 */
public interface ConfigurationReader {

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

	/**
	 * Get a value from the configuration file.
	 * 
	 * @param id
	 *            the unique id of the requested value
	 * @return the requested value
	 */
	public Object getValue(String id);

}
