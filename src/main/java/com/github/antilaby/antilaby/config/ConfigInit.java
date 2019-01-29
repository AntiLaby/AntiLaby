package com.github.antilaby.antilaby.config;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.api.exceptions.InternalException;
import com.github.antilaby.antilaby.api.updater.VersionType;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Initialize the configuration file and update it from older versions. This file will be reworked.
 *
 * @author NathanNr
 */
public class ConfigInit {

  private static final String CONFIG_VERSION_PATH = "AntiLaby.ConfigVersion";
  private final Logger logger = new Logger("Config/Init");
  private File file;
  private FileConfiguration fileConfiguration;

  /**
   * Initialize the configuration file and update it from older versions.
   *
   * @param file              the configuration file
   * @param fileConfiguration the file configuration instance of the configuration file
   * @throws IOException      usually an error while saving the file
   */
  public ConfigInit(File file, FileConfiguration fileConfiguration) throws IOException {
    this.file = file;
    this.fileConfiguration = fileConfiguration;

    // Check if the configuration file has to be updated
    logger.debug("Initializing the config file...");
    if (fileConfiguration.get(CONFIG_VERSION_PATH) == null) {
      addDefaults();
      save();
    } else if (fileConfiguration.getInt(CONFIG_VERSION_PATH) == Constants.CURRENT_CONFIG_VERSION) {
      addDefaults();
      save();
    } else {
      update();
      addDefaults();
      save();
    }
    logger.debug("Initialized the config file.");
  }

  /**
   * Add the default values to the configuration file.
   */
  private void addDefaults() {
    logger.debug("Adding defaults to the config file...");

    fileConfiguration.options().header("AntiLaby plug-in by NathanNr and heisluft, " + Constants.RESOURCE_LINK);

    fileConfiguration.addDefault("AntiLaby.EnableBypassWithPermission", true);
    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Kick.Enable", false);

    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Ban.Info",
        "# AntiLaby allows you to ban players who are using LabyMod permanently from your server. More information about LabyModPlayerBan: " + Constants.WIKI_LABYMODPLAYERBAN_URL); // TODO
    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Ban.Enable", false);
    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Ban.Command", "/ban %player% %reason%");

    // Additional plug-in channels. Clients who use them, will be blocked.
    // TODO: implement Additional Plug-in Channels
    if (fileConfiguration.getStringList("AntiLaby.AdditionalPluginChannels") == null) {
      ArrayList<String> additionalPluginChannels = new ArrayList<>();
      additionalPluginChannels.add("#Here you can add additional plug-in channels which will be blocked.");
      fileConfiguration.set("AntiLaby.AdditionalPluginChannels", additionalPluginChannels);
    }

    // Add default values for auto-updates
    fileConfiguration.addDefault("AntiLaby.Update.AutoUpdate.Release", true);
    fileConfiguration.addDefault("AntiLaby.Update.AutoUpdate.Beta", false);
    if (AntiLaby.getInstance().getVersionType() == VersionType.DEV) {
      fileConfiguration.addDefault("AntiLaby.Update.AutoUpdate.Test", false);
    }


    // Add default values for the feature handling of LabyMod
    final List<String> disabledFeatures = fileConfiguration.getStringList("AntiLaby.LabyModFeatures.Disable");
    final List<String> enabledFeatures = fileConfiguration.getStringList("AntiLaby.LabyModFeatures.Enable");
    for (LabyModFeature labyModFeature : LabyModFeature.values()) {
      switch (labyModFeature.getDefaultValue()) {
        case "enabled":
          disabledFeatures.add(labyModFeature.toString());
          break;
        case "disabled":
          enabledFeatures.add(labyModFeature.toString());
          break;
        default:
          throw new InternalException("Configuration", "Detected unknown default LabyMod feature value while creating the configuration file.", null);
      }
    }

    // Set the string lists to the configuration file, if they do not exist yet
    if (fileConfiguration.getList("AntiLaby.LabyModFeatures.Disable") == null) {
      fileConfiguration.set("AntiLaby.LabyModFeatures.Disable", disabledFeatures);
    }
    if (fileConfiguration.getList("AntiLaby.LabyModFeatures.Enable") == null) {
      fileConfiguration.set("AntiLaby.LabyModFeatures.Enable", enabledFeatures);
    }
    if (fileConfiguration.getList("AntiLaby.LabyModPlayerAction.ExecuteCommands") == null) {
      final List<String> commands = fileConfiguration.getStringList("AntiLaby.LabyModPlayerAction.ExecuteCommands");
      commands.add("#These commands will be executed once if a player with LabyMod joins the server.");
      commands.add("#If the player has the permission \"antilaby.bypasscommands\" the commands won't be executed.");
      commands.add("#You can use %PLAYER% to get the player's name. Example (remove \"#\" to enable):");
      commands.add("#/tellraw %PLAYER% {\"text\":\"Welcome LabyMod player!\"}");
      fileConfiguration.set("AntiLaby.LabyModPlayerAction.ExecuteCommands", commands);
    }
    fileConfiguration.addDefault("AntiLaby.DebugMode", false);
    fileConfiguration.addDefault("AntiLaby.ConfigVersion", Constants.CURRENT_CONFIG_VERSION);
    fileConfiguration.options().copyDefaults(true);
  }

  /**
   * Updates the configuration file from an older version to the latest configuration version.
   */
  private void update() {
    int oldConfigVersion = fileConfiguration.getInt(ConfigReader.getConfigVersionPath());
    FileConfiguration updatedConfiguration = new ConfigUpdater(fileConfiguration, oldConfigVersion, Constants.CURRENT_CONFIG_VERSION).getUpdatedData();
    fileConfiguration = fileConfiguration != null ? updatedConfiguration : new YamlConfiguration();
    logger.debug("Updated the config file.");
  }

  /**
   * Save the configuration file.
   *
   * @throws IOException If the configuration somehow failed to save
   */
  private void save() throws IOException {
    fileConfiguration.save(file);
    logger.debug("Saved the config file.");
  }

  /**
   * Delete the configuration file. This method is used after updating the configuration file from an older version.
   */
  @SuppressWarnings("unused")
  private void delete() {
    file.delete();
    file = new File(AntiLaby.getInstance().getDataFolder() + "/config.yml");
    fileConfiguration = YamlConfiguration.loadConfiguration(file);
    logger.debug("Deleted the config file.");
  }

}
