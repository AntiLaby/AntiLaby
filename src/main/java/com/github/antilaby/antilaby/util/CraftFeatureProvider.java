package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.lang.Locale;
import com.github.antilaby.antilaby.main.AntiLaby;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

/**
 * The FeatureProvider for CraftBukkit.
 *
 * @author heisluft
 */
public final class CraftFeatureProvider extends FeatureProvider {

  /** The packet class, used for casting. */
  private static Class<?> packet;
  /** PacketDataSerializer Constructor. */
  private static Constructor<?> packetDataSerializer;
  /** PacketPlayOutCustomPayload Constructor. */
  private static Constructor<?> packetPlayOutCustomPayload;
  /** private EntityPlayer.locale field. */
  private static Field locale;
  /** private EntityPlayer.playerConnection field. */
  private static Field playerConnection;
  /** PlayerConnection.sendPacket() method. */
  private static Method sendPacket;
  /** CraftPlayer.getHandle() method. */
  private static Method getHandle;

  /** The singleton instance. */
  private static CraftFeatureProvider instance;

  /**
   * Get the singleton instance. If it does not exist, it is created.
   *
   * @return the singleton instance
   */
  static CraftFeatureProvider getInstance() {
    return instance == null ? new CraftFeatureProvider() : instance;
  }

  /**
   * Construct a new FeatureProvider. Private as this is a singleton
   *
   * @throws IllegalStateException if an instance was already created
   */
  private CraftFeatureProvider() {
    if (instance != null) {
      throw new IllegalStateException("CraftFeatureProvider is a singleton");
    }
    try {
      final String nms = "net.minecraft.server.";
      final String obc = "org.bukkit.craftbukkit.";
      final String name = Bukkit.getServer().getClass().getPackage().getName();
      final String version = name.substring(name.lastIndexOf('.') + 1);
      packet = Class.forName(nms + version + ".Packet");
      Class<?> packetDataSerializerC = Class.forName(nms + version + ".PacketDataSerializer");
      packetDataSerializer = packetDataSerializerC.getConstructor(ByteBuf.class);
      packetPlayOutCustomPayload = Class.forName(nms + version + ".PacketPlayOutCustomPayload")
          .getConstructor(String.class, packetDataSerializerC);
      Class<?> craftPlayer = Class.forName(obc + version + ".entity.CraftPlayer");
      getHandle = craftPlayer.getMethod("getHandle");
      Class<?> entityPlayer = Class.forName(nms + version + ".EntityPlayer");
      locale = entityPlayer.getField("locale");
      playerConnection = entityPlayer.getField("playerConnection");
      sendPacket = Class.forName(nms + version + ".PlayerConnection")
          .getMethod("sendPacket", packet);
      instance = this;
    } catch (final ReflectiveOperationException e) {
      LOG.error(e.getMessage());
    }
  }

  /**
   * Sends all disabled LabyMod functions to the client.
   * <em>Will be removed.</em>
   *
   * @param player The {@link Player} to send the data to
   * @param labymodFunctions A {@link Map} containing all {@link LabyModFeature}s and whether
   *     they are enabled
   * @throws IOException If the connection to the client could somehow not be established
   * @throws ReflectiveOperationException If something goes wrong during reflection.
   */
  //TODO: Remove this
  @SuppressWarnings("unchecked")
  public static void setLabyModFeature(Player player, Map<LabyModFeature, Boolean> labymodFunctions)
      throws Exception {

    //LabyMod 3 readable JSON object
    JSONObject jsonObject = new JSONObject();

    //LabyMod-2 readable Map
    final HashMap<String, Boolean> nList = new HashMap<>();

    for (final Entry<LabyModFeature, Boolean> entry : labymodFunctions.entrySet()) {
      if (entry.getKey().getVersion() == 2) {
        nList.put((entry.getKey()).name(), entry.getValue());
      } else {
        jsonObject.put(entry.getKey().name(), entry.getValue());
      }
    }

    //write the data to stream
    final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    final ObjectOutputStream out = new ObjectOutputStream(byteOut);
    out.writeObject(nList);
    ByteBuf bb = Unpooled.buffer();
    bb.writeBytes(byteOut.toByteArray());

    //Send LM2 stuff to Client
    getInstance().sendPluginMessageImpl(player, Constants.LABYMOD_CHANNEL_OLD, bb);

    //LabyMod 3 Damage Indicator
    if (!labymodFunctions.get(LabyModFeature.DAMAGEINDICATOR)) {
      getInstance().sendPluginMessageImpl(
          player, "DAMAGEINDICATOR", Unpooled.buffer().writeBoolean(false));
    }

    bb = Unpooled.buffer();
    bb.writeBytes("PERMISSIONS".getBytes());
    bb.writeBytes(jsonObject.toJSONString().getBytes());
    System.out.println(bb.readableBytes());

    //LabyMod 3 Features (OLD Channel)
    getInstance().sendPluginMessageImpl(player, Constants.LABYMOD_CHANNEL_OLD, bb);

    //LabyMod 3 Features
    getInstance().sendPluginMessageImpl(player, Constants.LABYMOD_CHANNEL, bb);


    //logback
    final StringBuilder b = new StringBuilder("Disabled some LabyMod functions (");
    for (final Entry<String, Boolean> n : nList.entrySet()) {
      if (!n.getValue()) {
        b.append(n.getKey()).append(", ");
      }
    }
    AntiLaby.LOG.info(b.replace(b.length() - 2, b.length(), "").append(") for player ")
        .append(player.getName()).append(" (").append(player.getUniqueId()).append(')').toString());
    //close stream
    out.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void sendPluginMessageImpl(Player p, String channel, ByteBuf message) throws Exception {
    Object dataSerializer = packetDataSerializer.newInstance(message);
    Object packet = packetPlayOutCustomPayload.newInstance(channel, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(p)),
        CraftFeatureProvider.packet.cast(packet));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Locale getLanguageImpl(Player p) {
    try {
      return Locale.byName(((String) locale.get(getHandle.invoke(p))).toLowerCase(), Locale.EN_US);
    } catch (ReflectiveOperationException e) {
      LOG.error(e.getMessage());
      return Locale.EN_US;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String getMinecraftVersionImpl() {
    return Bukkit.getVersion().split("\\(", 2)[1].replace(")", "");
  }
}