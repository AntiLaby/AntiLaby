package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.lang.Locale;
import com.github.antilaby.antilaby.main.AntiLaby;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.entity.meta.ClientSettings;
import org.bukkit.entity.Player;

public final class GlowFeatureProvider extends FeatureProvider {

  private static Field settings;
  private static GlowFeatureProvider instance;

  static GlowFeatureProvider getInstance() {
    return instance == null ? new GlowFeatureProvider() : instance;
  }

  private GlowFeatureProvider() {
    if (instance != null) {
      throw new UnsupportedOperationException("GlowstoneUtils is a Singleton!");
    }
    try {
      settings = GlowPlayer.class.getDeclaredField("settings");
      instance = this;
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void sendPluginMessageImpl(Player p, String channel, ByteBuf message) {
    ((GlowPlayer) p).addChannel(channel);
    p.sendPluginMessage(AntiLaby.getInstance(), channel, message.array());
  }

  @Override
  protected Locale getLanguageImpl(Player p) {
    try {
      return Locale.byName(((ClientSettings) settings.get(p))
          .getLocale().toLowerCase(), Locale.EN_US);
    } catch (ReflectiveOperationException e) {
      return Locale.EN_US;
    }
  }

  @Override
  protected String getMCVersionImpl() {
    return GlowServer.GAME_VERSION;
  }
}
