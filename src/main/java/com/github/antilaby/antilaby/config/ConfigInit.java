package com.github.antilaby.antilaby.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.antilaby.antilaby.api.updater.VersionType;
import com.github.antilaby.antilaby.log.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.api.exceptions.InternalException;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;

/**
 * Initialize the configuration file and update it from older versions. This file will be reworked.
 *
 * @author NathanNr
 */
public class ConfigInit {

  private File file;
  private FileConfiguration fileConfiguration;
  private static final String CONFIG_VERSION_PATH = "AntiLaby.ConfigVersion";
  private final Logger logger = new Logger("Config/Init");

  /**
   * Initialize the configuration file and update it from older versions.
   *
   * @param file
   *     the configuration file
   * @param fileConfiguration
   *     the file configuration instance of the configuration file
   *
   * @throws IOException
   */
  public ConfigInit(File file, FileConfiguration fileConfiguration) throws IOException {
    this.file = file;
    this.fileConfiguration = fileConfiguration;

    // Check if the configuration file has to be updated
    if(fileConfiguration.get(CONFIG_VERSION_PATH) == null) {
      addDefaults();
      save();
    } else if(fileConfiguration.getInt(CONFIG_VERSION_PATH) == Constants.CURRENT_CONFIG_VERSION) {
      addDefaults();
      save();
    } else {
      update();
    }
  }

  /**
   * Add the default values to the configuration file.
   */
  private void addDefaults() {
    fileConfiguration.options().header("AntiLaby plugin by NathanNr and heisluft, " + Constants.RESOURCE_LINK);

    fileConfiguration.addDefault("AntiLaby.EnableBypassWithPermission", true);
    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Kick.Enable", false);

    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Ban.Info",
        "# AntiLaby allows you to ban players who are using LabyMod permanently from your server. More information about LabyModPlayerBan: " + Constants.WIKI_LABYMODPLAYERBAN_URL); // TODO
    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Ban.Enable", false);
    fileConfiguration.addDefault("AntiLaby.LabyModPlayerAction.Ban.Command", "/ban %player% %reason%");

    // Additional plug-in channels. Clients who use them, will be blocked.
    // TODO: implement Additional Plug-in Channels
    if(fileConfiguration.getStringList("AntiLaby.AdditionalPluginChannels") == null) {
      ArrayList<String> additionalPluginChannels = new ArrayList<>();
      additionalPluginChannels.add("#Here you can add additional plug-in channels which will be blocked.");
      fileConfiguration.set("AntiLaby.AdditionalPluginChannels", additionalPluginChannels);
    }

    // Add default values for auto-updates
    fileConfiguration.addDefault("AntiLaby.Update.AutoUpdate.Release", true);
    fileConfiguration.addDefault("AntiLaby.Update.AutoUpdate.Beta", false);
    if(AntiLaby.getInstance().getVersionType() == VersionType.DEV) {
      fileConfiguration.addDefault("AntiLaby.Update.AutoUpdate.Test", false);
    }


    // Add default values for the feature handling of LabyMod
    final List<String> disabledFeatures = fileConfiguration.getStringList("AntiLaby.LabyModFeatures.Disable");
    final List<String> enabledFeatures = fileConfiguration.getStringList("AntiLaby.LabyModFeatures.Enable");
    for(LabyModFeature labyModFeature : LabyModFeature.values()) {
      switch(labyModFeature.getDefaultValue()) {
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
    if(fileConfiguration.getList("AntiLaby.LabyModFeatures.Disable") == null) fileConfiguration.set("AntiLaby.LabyModFeatures.Disable", disabledFeatures);
    if(fileConfiguration.getList("AntiLaby.LabyModFeatures.Enable") == null) fileConfiguration.set("AntiLaby.LabyModFeatures.Enable", enabledFeatures);
    if(fileConfiguration.getList("AntiLaby.LabyModPlayerAction.ExecuteCommands") == null) {
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
  @SuppressWarnings("unused")
  private void update() {
    // The old file will be overwritten currently.
    // TODO: Update the configuration updater from the old config version 2.
    if(file.exists()) {
      file.delete();
      fileConfiguration = YamlConfiguration.loadConfiguration(file);
      addDefaults();
    }
    // TODO: Enable the config updater
    if(false) { // Currently not in use
      int oldVersion;
      if(fileConfiguration.getString("AntiLaby.ConfigVersion") != null) oldVersion = fileConfiguration.getInt("AntiLaby.ConfigVersion");
      else oldVersion = 0;
      if(oldVersion == 1) {
        logger.info("Updating the configuration file '" + file.getAbsolutePath() + "' from version '" + oldVersion + "' to the current version '" + Constants.CURRENT_CONFIG_VERSION + "'...");
        fileConfiguration.getBoolean("AntiLaby.EnableBypassWithPermission");
        final boolean labyKick = fileConfiguration.getBoolean("AntiLaby.LabyModPlayerKick.Enable");
        ArrayList<String> disabledFeatures = new ArrayList<>();
        ArrayList<String> enabledFeatures = new ArrayList<>();
        for(LabyModFeature lmf : LabyModFeature.values()) {
          if(fileConfiguration.get("AntiLaby.disable." + lmf.toString()).equals("true")) {
            disabledFeatures.add(lmf.toString());
          } else if(fileConfiguration.get("AntiLaby.disable." + lmf.toString()).equals("false")) {
            enabledFeatures.add(lmf.toString());
          }
        }
      /*  final boolean food = fileConfiguration.getBoolean("AntiLaby.disable.FOOD");
        final boolean gui = fileConfiguration.getBoolean("AntiLaby.disable.GUI");
        final boolean nick = fileConfiguration.getBoolean("AntiLaby.disable.NICK");
        final boolean chat = fileConfiguration.getBoolean("AntiLaby.disable.CHAT");
        final boolean extras = fileConfiguration.getBoolean("AntiLaby.disable.EXTRAS")
        final boolean animations = fileConfiguration.getBoolean("AntiLaby.disable.ANIMATIONS");
        final boolean potions = fileConfiguration.getBoolean("AntiLaby.disable.POTIONS");
        final boolean armor = fileConfiguration.getBoolean("AntiLaby.disable.ARMOR");
        final boolean damageIndicator = fileConfiguration.getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
        final boolean minimapRadar = fileConfiguration.getBoolean("AntiLaby.disable.MINIMAP_RADAR"); */
        final List<String> labyModPlayerCommands = fileConfiguration.getStringList("AntiLaby.LabyModPlayerCommands");
        // Delete the old file and generate a new one
        delete();
        addDefaults();
        // Set the saved data in the new file format
        fileConfiguration.set("AntiLaby.LabyModFeatures.Disable", disabledFeatures);
        fileConfiguration.set("AntiLaby.LabyModFeatures.Enable", enabledFeatures);
        fileConfiguration.set("AntiLaby.LabyModPlayerCommands", labyModPlayerCommands);
        // Save the updated file
        try {
          save();
        } catch(IOException e) {
          e.printStackTrace();
        }
        logger.info("The configuration file has been updated successfully!");
      } else if(oldVersion == 2) {
        // TODO: Create the updater
        logger.info("Updating the configuration file '" + file.getAbsolutePath() + "' from version '" + oldVersion + "' to the current version '" + Constants.CURRENT_CONFIG_VERSION + "'.");
        boolean bypassWithPermission = fileConfiguration.getBoolean("AntiLaby.EnableBypassWithPermission");
        boolean labyModPlayerKickEnabled = fileConfiguration.getBoolean("AntiLaby.LabyModPlayerKick.Enable");
        // LabyMod 3 features:
        boolean saturationBar = fileConfiguration.getBoolean("AntiLaby.Features.Disable.SATURATION_BAR");
        boolean chat = fileConfiguration.getBoolean("AntiLaby.Features.Disable.CHAT");
        boolean guiAll = fileConfiguration.getBoolean("AntiLaby.Features.Disable.GUI_ALL");
        boolean guiPotion = fileConfiguration.getBoolean("AntiLaby.Features.Disable.GUI_POTION_EFFECTS");
        boolean guiArmor = fileConfiguration.getBoolean("AntiLaby.Features.Disable.GUI_ARMOR_HUD");
        boolean guiItem = fileConfiguration.getBoolean("AntiLaby.Features.Disable.GUI_ITEM_HUD");
        boolean tags = fileConfiguration.getBoolean("AntiLaby.Features.Disable.TAGS");
        boolean animations = fileConfiguration.getBoolean("AntiLaby.Features.Disable.ANIMATIONS");
        boolean blockbuild = fileConfiguration.getBoolean("AntiLaby.Features.Enable.BLOCKBUILD");
        boolean improvedLava = fileConfiguration.getBoolean("AntiLaby.Features.Enable.IMPROVED_LAVA");
        boolean crosshairSync = fileConfiguration.getBoolean("AntiLaby.Features.Enable.CROSSHAIR_SYNC");
        boolean refillFix = fileConfiguration.getBoolean("AntiLaby.Features.Enable.REFILL_FIX");
        // Features from earlier versions of LabyMod:
        boolean food = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.FOOD");
        boolean gui = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.GUI");
        boolean nick = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.NICK");
        boolean extras = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.EXTRAS");
        boolean potions = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.POTIONS");
        boolean armor = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.ARMOR");
        boolean damageIndicator = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.DAMAGEINDICATOR");
        boolean minimapRadar = fileConfiguration.getBoolean("AntiLaby.OldFeatures.Disable.MINIMAP_RADAR");
        List<String> commands = fileConfiguration.getStringList("AntiLaby.LabyModPlayerCommands");
        boolean autoUpdate = fileConfiguration.getBoolean("AntiLaby.Update.AutoUpdate");
        boolean debugMode = fileConfiguration.getBoolean("AntiLaby.DebugMode");
        delete();
        addDefaults();
        // TODO Delete the old file and create the new one with the stored values.
        try {
          save();
        } catch(IOException e) {
          e.printStackTrace();
        }
        logger.info("The configuration file has been updated successfully!");
      } else {
        delete();
        addDefaults();
        try {
          save();
        } catch(IOException e) {
          e.printStackTrace();
        }
        logger.warn("Failed to update the configuration file, it has been reset to it's default values. Please check the current settings!");
      }
    }
  }

  /**
   * Save the configuration file.
   *
   * @throws IOException
   *     If the configuration somehow failed to save
   */
  private void save() throws IOException {
    fileConfiguration.save(file);
  }

  /**
   * Delete the configuration file. This method is used after updating the configuration file from an older version.
   */
  private void delete() {
    file.delete();
    file = new File(AntiLaby.getInstance().getDataFolder() + "/config.yml");
    fileConfiguration = YamlConfiguration.loadConfiguration(file);
  }

}
