package com.github.antilaby.antilaby.config;

import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.AntiLaby;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Get fundamental information about the configuration file and make fundamental operations.
 *
 * @author NathanNr
 */
public class ConfigFile {

  /**
   * The logger for configuration operations.
   */
  protected static final Logger logger = new Logger("ConfigManager");
  /**
   * The path to the configuration file.
   */
  private static final File FILE = new File(AntiLaby.getInstance().getDataFolder(), "config.yml");
  private static FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(FILE);

  /**
   * No instances can be created.
   */
  private ConfigFile() {
    throw new UnsupportedOperationException();
  }


  /**
   * Get the FileConfiguration of the configuration file.
   *
   * @return configuration as FileConfiguration
   */
  static FileConfiguration getFileConfiguration() {
    return fileConfiguration;
  }

  /**
   * Save the configuration file.
   */
  private static void save() {
    try {
      fileConfiguration.save(FILE);
    } catch (IOException e) {
      logger.error("Failed to save the configuration file at '" + FILE.getAbsolutePath() + "':");
      e.printStackTrace();
    }
  }

  /**
   * (Re-)load the configuration file.
   */
  public static void load() {
    try {
      fileConfiguration.load(FILE);
    } catch (IOException | InvalidConfigurationException e) {
      logger.error("Failed to load the configuration file at '" + FILE.getAbsolutePath() + "':");
      e.printStackTrace();
    }
    try {
      new ConfigInit(FILE, fileConfiguration);
    } catch (IOException e) {
      logger.error("Failed to save the configuration file at '" + FILE.getAbsolutePath() + "':");
      e.printStackTrace();
    }
    save();
  }

}
