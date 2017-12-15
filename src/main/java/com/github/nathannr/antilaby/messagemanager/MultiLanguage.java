package com.github.nathannr.antilaby.messagemanager;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MultiLanguage {
	
	private boolean languageLoaded = false;
	private final JavaPlugin plugin;
	private final String cprefix;
	private String fallbackFile = "en_us";
	
	public MultiLanguage(JavaPlugin plugin, String cprefix) {
		this.plugin = plugin;
		this.cprefix = cprefix;
		initLanguage();
	}
	
	public MultiLanguage(JavaPlugin plugin, String cprefix, String fallbackFile) {
		this.plugin = plugin;
		this.cprefix = cprefix;
		this.fallbackFile = fallbackFile;
		initLanguage();
	}
	
	public String getFallbackLanguageMessage(String path, boolean translateAlternateColorCodes) {
		// Get a message in the fallback language
		final File fallbackFile = new File(plugin.getDataFolder() + "/language/" + this.fallbackFile + ".yml");
		final FileConfiguration fallbackCfg = YamlConfiguration.loadConfiguration(fallbackFile);
		if (fallbackCfg.getString(path) == null) initLanguage();
		if (fallbackCfg.getString(path) != null) {
			if (translateAlternateColorCodes == true)
				return ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path));
			else return fallbackCfg.getString(path);
		} else throw new MultiLanguageException(cprefix + "MultiLanguageMessage error: Path '" + path
				+ "' does not exists in the fallback language file.");
	}
	
	public String getMultiLanguageMessage(Player player, String path, boolean translateAlternateColorCodes) {
		// Get a message in player's language
		if (languageLoaded == false) initLanguage();
		final File file = new File(plugin.getDataFolder() + "/language/" + player.getLocale().toLowerCase() + ".yml");
		final File fallbackFile = new File(plugin.getDataFolder() + "/language/" + this.fallbackFile + ".yml");
		final FileConfiguration fallbackCfg = YamlConfiguration.loadConfiguration(fallbackFile);
		if (path.isEmpty() || path == null)
			throw new MultiLanguageException("Plugin tried to send a MultiLanguageMessage with an empty or null path.");
		if (file.exists()) {
			final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if (cfg.getString(path) != null) {
				if (translateAlternateColorCodes == true)
					return ChatColor.translateAlternateColorCodes('&', cfg.getString(path));
				else return cfg.getString(path);
			} else {
				if (fallbackCfg.getString(path) == null) initLanguage();
				if (fallbackCfg.getString(path) != null) {
					if (translateAlternateColorCodes == true)
						return ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path));
					else return fallbackCfg.getString(path);
				} else throw new MultiLanguageException(cprefix + "MultiLanguageMessage error: Path '" + path
						+ "' does not exists in the fallback language file.");
			}
		} else {
			if (fallbackCfg.getString(path) == null) initLanguage();
			if (fallbackCfg.getString(path) != null) {
				if (translateAlternateColorCodes == true)
					return ChatColor.translateAlternateColorCodes('&', fallbackCfg.getString(path));
				else return fallbackCfg.getString(path);
			} else throw new MultiLanguageException(cprefix + "MultiLanguageMessage error: Path '" + path
					+ "' does not exists in the fallback language file.");
		}
	}

	public void initLanguage() {
		languageLoaded = true;
	}
	
}
