package com.github.antilaby.antilaby.api.config;

import java.io.File;
import java.io.IOException;

import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.antilaby.antilaby.log.Logger;

/**
 * Get fundamental information about the configuration file and make fundamental operations.
 *
 * @author NathanNr
 */
public class ConfigFile {

	private ConfigFile() {
		throw new UnsupportedOperationException();
	}

	/**
	 * The logger for configuration operations.
	 */
	protected static final Logger logger = new Logger("ConfigManager");

	/**
	 * The path to the configuration file
	 */
	public static final String CONFIG_PATH = AntiLaby.getInstance().getDataFolder() + "config.yml";

	private static File file = new File(CONFIG_PATH);
	private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

	/**
	 * Get the file object of the configuration file.
	 *
	 * @return configuration as File
	 */
	static File getFile() {
		return file;
	}

	/**
	 * Get the FileConfiguration of the configuration file.
	 *
	 * @return configuration as FileConfiguration
	 */
	static FileConfiguration getCfg() {
		return cfg;
	}

	/**
	 * Save the configuration file.
	 */
	public static void save() {
		try {
			cfg.save(file);
		} catch(IOException e) {
			logger.error("Failed to save the configuration file at path '" + CONFIG_PATH + "':");
			e.printStackTrace();
		}
	}

	/**
	 * (Re-)load the configuration file.
	 */
	public static void load() {
		try {
			cfg.load(file);
		} catch(IOException | InvalidConfigurationException e) {
			logger.error("Failed to load the configuration file at path '" + CONFIG_PATH + "':");
			e.printStackTrace();
		}
		try {
			new ConfigInit(file, cfg);
		} catch(IOException e) {
			logger.error("Failed to save the configuration file at path '" + CONFIG_PATH + "':");
			e.printStackTrace();
		}
		save();
	}

}
