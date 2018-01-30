package com.github.antilaby.antilaby.config;

import com.github.antilaby.antilaby.main.AntiLaby;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Get fundamental information about the configuration file and make fundamental operations. This class will be removed
 * if the new configuration API is ready to use!
 *
 * @author NathanNr
 */
public class ConfigFile {

	private static final AntiLaby plugin = AntiLaby.getInstance();

	private static File file;
	private static FileConfiguration cfg;

	public static FileConfiguration getCfg() {
		return cfg;
	}

	public static File getFile() {
		return file;
	}

	public static void init() {
		file = new File(plugin.getDataFolder() + "/config.yml");
		try {
			file.createNewFile();
		} catch(final IOException e) {
			e.printStackTrace();
		}
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	public static void resetConfig() {
		file.delete();
		file = new File(plugin.getDataFolder() + "/config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
	}

	public static void saveFile() {
		try {
			cfg.save(file);
		} catch(final IOException e) {
			e.printStackTrace();
		}
	}
}
