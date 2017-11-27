package com.github.nathannr.antilaby.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.nathannr.antilaby.main.AntiLaby;

public class ConfigFile {

	private static AntiLaby plugin;
	
	private static File file;
	private static FileConfiguration cfg;
	
	public ConfigFile(AntiLaby antilaby) {
		plugin = antilaby;
		file = new File(plugin.getDataFolder() + "/config.yml");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		cfg = YamlConfiguration.loadConfiguration(file);
		reloadFile();
	}
	
	public static File getFile() {
		return file;
	}
	
	public static FileConfiguration getCfg() {
		return cfg;
	}
	
	public static void resetConfig() {
		file.delete();
		file = new File(plugin.getDataFolder() + "/config.yml");
		cfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public AntiLaby getPlugin() {
		return plugin;
	}
	
	public static void reloadFile() {
		try {
			cfg.load(file);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void saveFile() {
		try {
			cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
