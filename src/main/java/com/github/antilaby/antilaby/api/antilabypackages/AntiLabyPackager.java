package com.github.antilaby.antilaby.api.antilabypackages;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.config.ConfigReader;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Constants;
import com.github.antilaby.antilaby.util.NmsUtils;
import org.bukkit.entity.Player;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Create and send AntiLaby packages to the clients.
 *
 * @author NathanNr
 */
public class AntiLabyPackager {

  private final Logger logger = new Logger("AntiLabyPackager");

  private Player player;
  private Map<LabyModFeature, Boolean> labyModFeatureSettings;
  private boolean forceIgnoreBypassPermission = false;

  private ConfigReader configReader = new ConfigReader();

  AntiLabyPackager() {
  }

  public AntiLabyPackager(Player player) {
    this.player = player;

    mapLabyModSettings(configReader.getDisabledFeatures(), configReader.getEnabledFeatures());
  }

  public AntiLabyPackager(Player player, boolean forceIgnoreBypassPermission) {
    this.player = player;
    this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;

    mapLabyModSettings(configReader.getDisabledFeatures(), configReader.getEnabledFeatures());
  }

  public AntiLabyPackager(Player player, Map<LabyModFeature, Boolean> disabledLabyModFeatures) {
    this.player = player;
    this.labyModFeatureSettings = disabledLabyModFeatures;
  }

  public AntiLabyPackager(Player player, Map<LabyModFeature, Boolean> disabledLabyModFeatures, boolean forceIgnoreBypassPermission) {
    this.player = player;
    this.labyModFeatureSettings = disabledLabyModFeatures;
    this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
  }

  /**
   * Map the AntiLaby settings from two lists
   */
  // TODO use String instead of LabyModFeature to allow every String as a feature
  public void mapLabyModSettings(List<String> disabledFeatures, List<String> enabledFeatures) {
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    for (final String disabledFeature : disabledFeatures) {
      try {
        labyModFeatureSettings.put(LabyModFeature.valueOf(disabledFeature), false);
      } catch (final IllegalArgumentException e) {
        logger.error("MapLabyModSettings: Disabled LabyMod feature '" + disabledFeature + "' is no valid LabyMod feature and will be ignored.");
      }
    }
    for (final String enabledFeature : enabledFeatures) {
      try {
        labyModFeatureSettings.put(LabyModFeature.valueOf(enabledFeature), true);
      } catch (final IllegalArgumentException e) {
        logger.error("MapLabyModSettings: Enabled LabyMod feature '" + enabledFeature + "' is no valid LabyMod feature and will be ignored.");
      }
    }
    this.labyModFeatureSettings = labyModFeatureSettings;
  }

  /**
   * Use the LabyMod default settings (ignore the player)
   */
  public void useLabyModDefaults() {
    this.labyModFeatureSettings.clear();
  }

  /**
   * Allow every LabyMod feature including all features which are disabled by default
   */
  public void allowEverything() {
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    for (LabyModFeature labyModFeature : LabyModFeature.values()) {
      labyModFeatureSettings.put(labyModFeature, true);
    }
    this.labyModFeatureSettings = labyModFeatureSettings;
  }

  public AntiLabyPackager setLabyModFeatureSettings(Map<LabyModFeature, Boolean> labyModFeatureSettings) {
    this.labyModFeatureSettings = labyModFeatureSettings;
    return this;
  }

  Map<LabyModFeature, Boolean> getLabyModFeatureSettings() {
    return labyModFeatureSettings;
  }

  /**
   * The packages will be sent to the player even if he has the bypass permission and the bypass is enabled in the configuration file
   *
   * @param forceIgnoreBypassPermission true to enable
   */
  public AntiLabyPackager setForceIgnoreBypassPermission(boolean forceIgnoreBypassPermission) {
    this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
    return this;
  }

  /**
   * Send the AntiLaby packages to the player
   *
   * @return success
   */
  public boolean sendPackages() {
    boolean ignorePlayer = false;
    if (!forceIgnoreBypassPermission) // Ignore players with the bypass permission if enabled in the configuration file
    {
      if (player.hasPermission(Constants.PERMISSION_BYPASS)) {
        if (configReader.getEnableBypassWithPermission()) {
          useLabyModDefaults();
          ignorePlayer = true;
          AntiLaby.LOG.debug("Player " + player.getName() + " (" + player.getUniqueId() + ") has the permission 'antilaby.bypass' and has been ignored.");
        }
      }
    }
    if (ignorePlayer) {
      useLabyModDefaults();
    } else if (!labyModFeatureSettings.isEmpty()) {
      try {
        NmsUtils.setLabyModFeature(player, labyModFeatureSettings);
      } catch (final Exception e) {
        e.printStackTrace();
        AntiLaby.LOG.error("Failed to send AntiLaby packages to player " + player.getName() + " (" + player.getUniqueId() + "): " + e.getMessage());
        return false;
      }
    }
    return true;
  }

}
