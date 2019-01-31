package com.github.antilaby.antilaby.util;

import com.comphenix.protocol.PacketType;
import com.github.antilaby.antilaby.lang.Locale;
import com.github.antilaby.antilaby.log.Logger;
import io.netty.buffer.ByteBuf;
import org.bukkit.entity.Player;

public abstract class FeatureProvider {

  protected static FeatureProvider instance = getInstance();

  protected static final Logger LOG = new Logger("SpecificFeatures");

  private static FeatureProvider getInstance() {
    return ServerHelper.getImplementation() == ServerHelper.ImplementationType.CRAFT_BUKKIT
        ? CraftFeatureProvider.getInstance() : GlowFeatureProvider.getInstance();
  }

  public static String getMCVersion() {
    return instance.getMCVersionImpl();
  }

  public static Locale getLanguage(Player p) {
    return instance.getLanguageImpl(p);
  }

  public static void sendPluginMessage(Player p, String channel, ByteBuf message) throws Exception {
    instance.sendPluginMessageImpl(p, channel, message);
  }

  protected abstract Locale getLanguageImpl(Player p);

  protected abstract String getMCVersionImpl();

  protected abstract void sendPluginMessageImpl(Player p, String channel, ByteBuf message) throws Exception;
}
