package com.github.antilaby.antilaby.config;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.log.Logger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Update the configuration file to a newer version
 *
 * @author NathanNr
 */
public class ConfigUpdater {

  private final Logger logger = new Logger("ConfigUpdater");

  private final FileConfiguration initialConfigData;
  private final int fromVersion;
  private final int toVersion;

  public ConfigUpdater(FileConfiguration initialConfigData, int fromVersion, int toVersion) {
    this.initialConfigData = initialConfigData;
    this.fromVersion = fromVersion;
    this.toVersion = toVersion;
  }

  /**
   * Update the FileConfiguration to the new version, provided in the constructor
   *
   * @return the FileConfiguration in the new format
   */
  public FileConfiguration getUpdatedData() {
    FileConfiguration updatedData = initialConfigData;

    int i = fromVersion;
    while (i < toVersion) {
      if (updatedData != null) {
        update(updatedData, i);
      } else {
        break;
      }
    }

    return updatedData;
  }

  /**
   * Update the config file to the next version
   */
  private FileConfiguration update(FileConfiguration oldFileConfiguration, int oldVersion) {
    FileConfiguration newFileConfiguration = oldFileConfiguration;

    if (oldVersion == 2) {
      logger.debug("Updating the configuration file from version " + oldVersion + " to version " + ++oldVersion + "...");


      // Getting the old data from oldFileConfiguration
      boolean bypassWithPermission = oldFileConfiguration.getBoolean("AntiLaby.EnableBypassWithPermission");
      boolean labyModPlayerKickEnabled = oldFileConfiguration.getBoolean("AntiLaby.LabyModPlayerKick.Enable");

      ArrayList<String> disabledFeatures = new ArrayList<>();
      ArrayList<String> enabledFeatures = new ArrayList<>();
      for (LabyModFeature lmf : LabyModFeature.values()) {
        if (oldFileConfiguration.get("AntiLaby.Features.Disable." + lmf.toString()).equals("true")) {
          disabledFeatures.add(lmf.toString());
        } else if (oldFileConfiguration.get("AntiLaby.Features.Disable." + lmf.toString()).equals("false")) {
          enabledFeatures.add(lmf.toString());
        }
      }

      List<String> commands = oldFileConfiguration.getStringList("AntiLaby.LabyModPlayerCommands");
      boolean autoUpdate = oldFileConfiguration.getBoolean("AntiLaby.Update.AutoUpdate");
      boolean debugMode = oldFileConfiguration.getBoolean("AntiLaby.DebugMode");


      // Setting the new data to newFileConfiguration
      newFileConfiguration.set("AntiLaby.EnableBypassWithPermission", bypassWithPermission);
      newFileConfiguration.set("AntiLaby.LabyModPlayerAction.Kick.Enable", labyModPlayerKickEnabled);

      newFileConfiguration.set("AntiLaby.LabyModFeatures.Disable", disabledFeatures);
      newFileConfiguration.set("AntiLaby.LabyModFeatures.Enable", enabledFeatures);

      newFileConfiguration.set("AntiLaby.LabyModPlayerAction.ExecuteCommands", commands);
      newFileConfiguration.set("AntiLaby.Update.AutoUpdate.Release", autoUpdate);
      newFileConfiguration.set("AntiLaby.DebugMode", debugMode);
    } else {
      logger.error("Updating from config version " + oldVersion + " is not supported. You config settings will be reset to the defaults.");
      newFileConfiguration = null;
    }

    return newFileConfiguration;
  }

}
