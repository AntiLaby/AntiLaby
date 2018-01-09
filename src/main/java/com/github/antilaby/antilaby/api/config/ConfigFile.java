package com.github.antilaby.antilaby.api.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.antilaby.antilaby.log.Logger;

/**
 * Get fundamental information about the configuration file and make fundamental
 * operations.
 * 
 * @author NathanNr
 */

public abstract class ConfigFile {

	/**
	 * The logger for configuration operations.
	 */
	protected final Logger logger = new Logger("ConfigManager");

	/**
	 * The path to the configuration file
	 */
	public static final String CONFIG_PATH = "plugins/AntiLaby/config.yml";

	private File file = new File(CONFIG_PATH);
	private FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	/**
	 * Get the file object of the configuration file.
	 * 
	 * @return configuration as File
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Get the FileConfiguration of the configuration file.
	 * 
	 * @return configuration as FileConfiguration
	 */
	public FileConfiguration getCfg() {
		return cfg;
	}

	/**
	 * Save the configuration file.
	 */
	public void save() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			logger.error("Failed to save the configuration file at path '" + CONFIG_PATH + "':");
			e.printStackTrace();
		}
	}

	/**
	 * (Re-)load the configuration file.
	 */
	public void load() {
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			logger.error("Failed to load the configuration file at path '" + CONFIG_PATH + "':");
			e.printStackTrace();
		}
		try {
			new ConfigInit(file, cfg);
		} catch (IOException e) {
			logger.error("Failed to save the configuration file at path '" + CONFIG_PATH + "':");
			e.printStackTrace();
		}
		save();
	}

}
