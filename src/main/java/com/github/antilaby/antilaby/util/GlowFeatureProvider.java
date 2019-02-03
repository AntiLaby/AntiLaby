package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.AntiLaby;
import com.github.antilaby.antilaby.lang.Locale;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.ClientSettings;
import org.bukkit.entity.Player;

/**
 * The FeatureProvider for Glowstone.
 */
public final class GlowFeatureProvider extends FeatureProvider {

  /** private GlowPlayer.settings attribute. */
  private static Field settings;
  /** The singleton instance. */
  private static GlowFeatureProvider instance;

  /**
   * Construct a new FeatureProvider. Private as this is a singleton
   *
   * @throws IllegalStateException if an instance was already created
   */
  private GlowFeatureProvider() {
    if (instance != null) {
      throw new IllegalStateException("GlowFeatureProvider is a Singleton!");
    }
    try {
      settings = GlowPlayer.class.getDeclaredField("settings");
      instance = this;
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the singleton instance. If it does not exist, it is created.
   *
   * @return the singleton instance
   */
  static GlowFeatureProvider getInstance() {
    return instance == null ? new GlowFeatureProvider() : instance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void sendPluginMessageImpl(Player p, String channel, ByteBuf message) {
    ((GlowPlayer) p).addChannel(channel);
    p.sendPluginMessage(AntiLaby.getInstance(), channel, message.array());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Locale getLanguageImpl(Player p) {
    try {
      return Locale.byName(((ClientSettings) settings.get(p))
          .getLocale().toLowerCase(), Locale.EN_US);
    } catch (ReflectiveOperationException e) {
      return Locale.EN_US;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getMinecraftVersionImpl() {
    return GlowServer.GAME_VERSION;
  }
}
