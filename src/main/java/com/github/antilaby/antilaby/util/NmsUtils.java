package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * The class for NMS reflection magic
 *
 * @author heisluft
 */
public final class NmsUtils {
  private static final Logger LOG = new Logger("Reflection");

  private static final String NMS = "net.minecraft.server.";
  private static final String OBC = "org.bukkit.craftbukkit.";

  private static Class<?> packet;
  private static Constructor<?> packetDataSerializer;
  private static Constructor<?> packetPlayOutCustomPayload;
  private static Field locale;
  private static Field playerConnection;
  private static Method sendPacket;
  private static Method getHandle;

  private static boolean init;
  private static String version;

  /**
   * Private constructor, no need to instantiate this class
   */
  private NmsUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets the servers NMS version, for example 1_8_8
   *
   * @return The NMS version
   */
  public static String getVersion() {
    if (!init) {
      try {
        init();
      } catch (final ReflectiveOperationException e) {
        LOG.error(e.getMessage());
      }
    }
    return version;
  }

  @Nonnull
  public static String getLang(Player p) {
    try {
      if (!init) {
        init();
      }
      return ((String) locale.get(Objects.requireNonNull(getHandle.invoke(p)))).toLowerCase();
    } catch (ReflectiveOperationException e) {
      LOG.error(e.getMessage());
      return "en_us";
    }
  }

  /**
   * Initializes all static fields
   *
   * @throws ReflectiveOperationException If something failed during reflection
   */
  private static void init() throws ReflectiveOperationException {
    if (init) {
      return;
    }
    final String name = Bukkit.getServer().getClass().getPackage().getName();
    version = name.substring(name.lastIndexOf('.') + 1);
    packet = Class.forName(NMS + version + ".Packet");
    Class<?> packetDataSerializerC = Class.forName(NMS + version + ".PacketDataSerializer");
    packetDataSerializer = packetDataSerializerC.getConstructor(ByteBuf.class);
    packetPlayOutCustomPayload = Class.forName(NMS + version + ".PacketPlayOutCustomPayload").getConstructor(String.class, packetDataSerializerC);
    Class<?> craftPlayer = Class.forName(OBC + version + ".entity.CraftPlayer");
    getHandle = craftPlayer.getMethod("getHandle");
    Class<?> entityPlayer = Class.forName(NMS + version + ".EntityPlayer");
    locale = entityPlayer.getField("locale");
    playerConnection = entityPlayer.getField("playerConnection");
    sendPacket = Class.forName(NMS + version + ".PlayerConnection").getMethod("sendPacket", packet);
    init = true;
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
    if (!init) {
      init();
    }

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
    sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), NmsUtils.packet.cast(packet));

    //LabyMod 3 Damage Indicator
    if (!labymodFunctions.get(LabyModFeature.DAMAGEINDICATOR)) {
      dataSerializer = packetDataSerializer.newInstance(Unpooled.buffer().writeBoolean(false));
      packet = packetPlayOutCustomPayload.newInstance("DAMAGEINDICATOR", dataSerializer);
      sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), NmsUtils.packet.cast(packet));
    }

    bb = Unpooled.buffer();
    bb.writeBytes("PERMISSIONS".getBytes());
    bb.writeBytes(jsonObject.toJSONString().getBytes());
    System.out.println(bb.readableBytes());

    dataSerializer = packetDataSerializer.newInstance(bb);

    //LabyMod 3 Features (OLD Channel)
    packet = packetPlayOutCustomPayload.newInstance(Constants.LABYMOD_CHANNEL_OLD, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), NmsUtils.packet.cast(packet));

    //LabyMod 3 Features
    packet = packetPlayOutCustomPayload.newInstance(Constants.LABYMOD_CHANNEL, dataSerializer);
    sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), NmsUtils.packet.cast(packet));


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

}