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
 * The class for NMS reflection magic
 *
 * @author heisluft
 */
public final class CraftFeatureProvider extends FeatureProvider {

  private static Class<?> packet;
  private static Constructor<?> packetDataSerializer;
  private static Constructor<?> packetPlayOutCustomPayload;
  private static Field locale;
  private static Field playerConnection;
  private static Method sendPacket;
  private static Method getHandle;

  private static CraftFeatureProvider instance;

  private static boolean init;

  static CraftFeatureProvider getInstance() {
    return instance == null ? new CraftFeatureProvider() : instance;
  }

  /**
   * Private constructor as this is a singleton.
   */
  private CraftFeatureProvider() {
    if (instance != null) {
      throw new UnsupportedOperationException("NMSUtils is a singleton");
    }
    try {
      String nms = "net.minecraft.server.";
      String obc = "org.bukkit.craftbukkit.";
      String name = Bukkit.getServer().getClass().getPackage().getName();
      String version = name.substring(name.lastIndexOf('.') + 1);
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
   * Sends all disabled LabyMod functions to the client
   *
   * @param player           The {@link Player} to send the data to
   * @param labymodFunctions A {@link Map} containing all {@link LabyModFeature}s and whether they are enabled
   * @throws IOException                  If the connection to the client could somehow not be established
   * @throws ReflectiveOperationException If something goes wrong during {@link #init}.
   */
  @SuppressWarnings("unchecked")
  public static void setLabyModFeature(Player player, Map<LabyModFeature, Boolean> labymodFunctions) throws IOException, ReflectiveOperationException {

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
    Object dataSerializer = packetDataSerializer.newInstance(bb);
    Object packet = packetPlayOutCustomPayload.newInstance(Constants.LABYMOD_CHANNEL_OLD, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), CraftFeatureProvider.packet.cast(packet));

    //LabyMod 3 Damage Indicator
    if (!labymodFunctions.get(LabyModFeature.DAMAGEINDICATOR)) {
      dataSerializer = packetDataSerializer.newInstance(Unpooled.buffer().writeBoolean(false));
      packet = packetPlayOutCustomPayload.newInstance("DAMAGEINDICATOR", dataSerializer);
      sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), CraftFeatureProvider.packet.cast(packet));
    }

    bb = Unpooled.buffer();
    bb.writeBytes("PERMISSIONS".getBytes());
    bb.writeBytes(jsonObject.toJSONString().getBytes());
    System.out.println(bb.readableBytes());

    dataSerializer = packetDataSerializer.newInstance(bb);

    //LabyMod 3 Features (OLD Channel)
    packet = packetPlayOutCustomPayload.newInstance(Constants.LABYMOD_CHANNEL_OLD, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), CraftFeatureProvider.packet.cast(packet));

    //LabyMod 3 Features
    packet = packetPlayOutCustomPayload.newInstance(Constants.LABYMOD_CHANNEL, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), CraftFeatureProvider.packet.cast(packet));


    //logback
    final StringBuilder b = new StringBuilder("Disabled some LabyMod functions (");
    for (final Entry<String, Boolean> n : nList.entrySet()) {
      if (!n.getValue()) {
        b.append(n.getKey()).append(", ");
      }
    }
    AntiLaby.LOG.info(b.replace(b.length() - 2, b.length(), "").append(") for player ").append(player.getName()).append(" (").append(player.getUniqueId()).append(')').toString());
    //close stream
    out.close();
  }

  @Override
  protected void sendPluginMessageImpl(Player p, String channel, ByteBuf message) throws Exception {
    Object dataSerializer = packetDataSerializer.newInstance(message);
    Object packet = packetPlayOutCustomPayload.newInstance(channel, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(p)), CraftFeatureProvider.packet.cast(packet));
  }

  @Override
  protected Locale getLanguageImpl(Player p) {
    try {
      return Locale.byName(((String) locale.get(getHandle.invoke(p))).toLowerCase(), Locale.EN_US);
    } catch (ReflectiveOperationException e) {
      LOG.error(e.getMessage());
      return Locale.EN_US;
    }
  }

  @Override
  protected String getMCVersionImpl() {
    return Bukkit.getVersion().split("\\(", 2)[1].replace(")", "");
  }
}