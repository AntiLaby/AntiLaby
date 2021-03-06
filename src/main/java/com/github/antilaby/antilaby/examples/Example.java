package com.github.antilaby.antilaby.examples;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.pluginchannel.IncomingPluginChannel;
import com.github.antilaby.antilaby.util.FeatureProvider;
import java.util.EnumMap;
import org.bukkit.entity.Player;

/**
 * This class gives a few examples on how to use the AntiLaby API<br> WARNING: It's highly
 * recommended to use only methods from the package 'com.github.antilaby.antilaby.api', or, if you
 * really know
 * what you are doing, use {@link FeatureProvider} directly and handle the exceptions properly!
 * Other solutions may or may not work or be compatible for multiple Minecraft versions!
 */
public class Example {

  /**
   * Same as {@link #antiLabyPackageExample(Player)}, but uses chaining.
   *
   * @param player the Player to send to
   * @return same as {@link #antiLabyPackageExample(Player)}
   */
  public static boolean antiLabyChainedPackagerExample(Player player) {
    final EnumMap<LabyModFeature, Boolean> myPackageSettings = new EnumMap<>(LabyModFeature.class);
    myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
    myPackageSettings.put(LabyModFeature.POTIONS, true);
    return new AntiLabyPackager(player).setRuleset(myPackageSettings)
        .setForceIgnoreBypassPermission(true).sendPackages();
  }

  /**
   * Example AntiLaby API method.
   *
   * @param player the Player to send to
   * @return if the {@link AntiLabyPackager} completed successfully
   */
  public static boolean antiLabyPackageExample(Player player) {
    // Create instance
    final AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player);

    // Choose your own disabled functions
    final EnumMap<LabyModFeature, Boolean> myPackageSettings = new EnumMap<>(LabyModFeature.class);
    myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
    myPackageSettings.put(LabyModFeature.POTIONS, true);
    antiLabyPackager.setRuleset(myPackageSettings);
    /*
     * Send the packages even if the player has the permission
     * 'antilaby.bypass' and bypassing is enabled in the config file
     */
    antiLabyPackager.setForceIgnoreBypassPermission(true);
    // Send the AntiLaby packages
    return antiLabyPackager.sendPackages();
  }

  /**
   * Test if a player uses LabyMod.
   *
   * @param player The Player to test
   * @return true the player uses LabyMod, false otherwise
   */
  public static boolean labyPlayer(Player player) {
    return IncomingPluginChannel.getLabyModPlayers().containsKey(player.getUniqueId().toString());
  }

  /**
   * Example simple AntiLaby API method.
   *
   * @param player the Player to send to
   * @return same as {@link #antiLabyPackageExample(Player)}
   */
  public static boolean simpleAntiLabyPackageExample(Player player) {
    // Create a LabyPlayer
    final AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player);
    // Send AntiLaby packages
    return antiLabyPackager.sendPackages();
  }

}
