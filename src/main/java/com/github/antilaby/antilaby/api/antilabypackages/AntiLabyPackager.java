package com.github.antilaby.antilaby.api.antilabypackages;

import com.github.antilaby.antilaby.AntiLaby;
import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.config.ConfigReader;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.util.Constants;
import com.github.antilaby.antilaby.util.CraftFeatureProvider;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;

/**
 * Create and send AntiLaby packages to the clients.
 *
 * @author NathanNr
 */
public class AntiLabyPackager {

  private final Logger logger = new Logger("AntiLabyPackager");

  private Player player;
  private Map<LabyModFeature, Boolean> ruleset;
  private boolean forceIgnoreBypassPermission = false;

  private ConfigReader configReader = new ConfigReader();

  AntiLabyPackager() {
  }

  /**
   * Initialize a packager with default settings.
   *
   * @param player the player to send packages to
   */
  public AntiLabyPackager(Player player) {
    this.player = player;

    mapLabyModSettings(configReader.getDisabledFeatures(), configReader.getEnabledFeatures());
  }

  /**
   * Initialize a packager with optional force flag.
   *
   * @param player the player to send packages to
   * @param forceIgnoreBypassPermission if enabled, regulations apply even if the player has the
   *     bypass permission
   */
  public AntiLabyPackager(Player player, boolean forceIgnoreBypassPermission) {
    this.player = player;
    this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;

    mapLabyModSettings(configReader.getDisabledFeatures(), configReader.getEnabledFeatures());
  }

  public AntiLabyPackager(Player player, Map<LabyModFeature, Boolean> ruleset) {
    this.player = player;
    this.ruleset = ruleset;
  }

  /**
   * Initialize a packager with a custom ruleset with optional force flag.
   *
   * @param player the player to send packages to
   * @param ruleset a custom ruleset
   * @param forceIgnoreBypassPermission if enabled, regulations apply even if the player has the
   *     bypass permission
   */
  public AntiLabyPackager(Player player, Map<LabyModFeature, Boolean> ruleset,
                          boolean forceIgnoreBypassPermission) {
    this.player = player;
    this.ruleset = ruleset;
    this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
  }

  /**
   * Map the AntiLaby settings from two lists.
   * //@return AntiLaby packages
   */
  // TODO use String instead of LabyModFeature to allow every String as a feature
  public void mapLabyModSettings(List<String> disabledFeatures, List<String> enabledFeatures) {
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    for (final String disabledFeature : disabledFeatures) {
      try {
        labyModFeatureSettings.put(LabyModFeature.valueOf(disabledFeature), false);
      } catch (final IllegalArgumentException e) {
        logger.error("MapLabyModSettings: Disabled LabyMod feature '" + disabledFeature + "' is "
            + "no valid LabyMod feature and will be ignored.");
      }
    }
    for (final String enabledFeature : enabledFeatures) {
      try {
        labyModFeatureSettings.put(LabyModFeature.valueOf(enabledFeature), true);
      } catch (final IllegalArgumentException e) {
        logger.error("MapLabyModSettings: Enabled LabyMod feature '" + enabledFeature + "' is no "
            + "valid LabyMod feature and will be ignored.");
      }
    }
    this.ruleset = labyModFeatureSettings;
  }

  /**
   * Use the LabyMod default settings (ignore the player).
   */
  public void useLabyModDefaults() {
    this.ruleset.clear();
  }

  /**
   * Allow every LabyMod feature including all features which are disabled by default.
   */
  public void allowEverything() {
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    for (LabyModFeature labyModFeature : LabyModFeature.values()) {
      labyModFeatureSettings.put(labyModFeature, true);
    }
    this.ruleset = labyModFeatureSettings;
  }

  Map<LabyModFeature, Boolean> getRuleset() {
    return ruleset;
  }

  public AntiLabyPackager setRuleset(Map<LabyModFeature, Boolean> ruleset) {
    this.ruleset = ruleset;
    return this;
  }

  /**
   * The packages will be sent to the player even if he has the bypass permission and the bypass is
   * enabled in the configuration file.
   *
   * @param forceIgnoreBypassPermission true to enable
   */
  public AntiLabyPackager setForceIgnoreBypassPermission(boolean forceIgnoreBypassPermission) {
    this.forceIgnoreBypassPermission = forceIgnoreBypassPermission;
    return this;
  }

  /**
   * Send the AntiLaby packages to the player.
   *
   * @return success
   */
  public boolean sendPackages() {
    boolean ignorePlayer = false;
    if (!forceIgnoreBypassPermission) { // Ignore players with the bypass permission if enabled
      // in the configuration file
      if (player.hasPermission(Constants.PERMISSION_BYPASS)) {
        if (configReader.getEnableBypassWithPermission()) {
          useLabyModDefaults();
          ignorePlayer = true;
          AntiLaby.LOG.debug("Player " + player.getName() + " (" + player.getUniqueId() + ") has "
              + "the permission 'antilaby.bypass' and has been ignored.");
        }
      }
    }
    if (ignorePlayer) {
      useLabyModDefaults();
    } else if (!ruleset.isEmpty()) {
      try {
        CraftFeatureProvider.setLabyModFeature(player, ruleset);
      } catch (final Exception e) {
        e.printStackTrace();
        AntiLaby.LOG.error("Failed to send AntiLaby packages to player " + player.getName()
            + "(" + player.getUniqueId() + "): " + e.getMessage());
        return false;
      }
    }
    return true;
  }

}
