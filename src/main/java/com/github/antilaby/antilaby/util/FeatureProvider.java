package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.lang.Locale;
import com.github.antilaby.antilaby.log.Logger;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

/**
 * The FeatureProvider provides Bukkit implementation specific features.
 */
public abstract class FeatureProvider {

  /** The Logger for all FeatureProvider instances. */
  protected static final Logger LOG = new Logger("FeatureProvider");
  /** The singleton instance. */
  protected static FeatureProvider instance = getInstance();

  /**
   * Acquire an instance of a FeatureProvider. Instance creation is handled by subclasses.
   *
   * @return the instance
   */
  private static FeatureProvider getInstance() {
    return ServerHelper.getImplementation() == ServerHelper.ImplementationType.CRAFT_BUKKIT
        ? CraftFeatureProvider.getInstance() : GlowFeatureProvider.getInstance();
  }

  /**
   * Get the servers minecraft version.
   *
   * @return the mc version
   */
  public static String getMinecraftVersion() {
    return instance.getMinecraftVersionImpl();
  }

  /**
   * Get the language setting of a Player.
   *
   * @param p the player to get the language for
   * @return the player's language
   */
  public static Locale getLanguage(Player p) {
    return instance.getLanguageImpl(p);
  }

  /**
   * Sends a PluginMessage to a given Player.
   *
   * @param p the Player to send the message to
   * @param channel the channel to send the message over
   * @param message the message to send
   * @throws java.io.IOException if something goes wrong during transmission
   * @throws Exception on other errors.
   */
  public static void sendPluginMessage(Player p, String channel, ByteBuf message) throws Exception {
    instance.sendPluginMessageImpl(p, channel, message);
  }

  abstract Locale getLanguageImpl(Player p);

  abstract String getMinecraftVersionImpl();

  abstract void sendPluginMessageImpl(Player p, String channel, ByteBuf message) throws Exception;
}
