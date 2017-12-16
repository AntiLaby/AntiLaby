package de.heisluft.antilaby.nms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;
import com.github.nathannr.antilaby.main.AntiLaby;

import de.heisluft.antilaby.log.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * The class for NMS reflection magic
 *
 * @author heisluft
 *
 */
public final class NmsTools {
	
	private static Class<?> craftPlayer;
	private static Class<?> packetDataSerializer;
	private static Class<?> packetClass;
	private static Constructor<?> packetDataSerializerConstructor;
	private static Constructor<?> packetPlayOutCustomPayloadConstructor;
	private static final Map<Player, Object> MAPPED_CONNECTIONS = new HashMap<>();
	private static boolean init;
	private static final Logger LOG = new Logger("Reflection");

	private static String version;

	/**
	 * Gets the servers NMS version, for example 1_8_8
	 *
	 * @return The NMS version
	 */
	public static String getVersion() {
		if (!init) try {
			init();
		} catch (final ReflectiveOperationException e) {
			LOG.log(Level.ERROR, e.getMessage());
		}
		return version;
	}

	/**
	 * Initializes all static fields
	 *
	 * @throws ReflectiveOperationException
	 *             If something failed during reflection
	 */
	private static void init() throws ReflectiveOperationException {
		if (init) return;
		final String name = Bukkit.getServer().getClass().getPackage().getName();
		version = name.substring(name.lastIndexOf('.') + 1);
		packetClass = Class.forName("net.minecraft.server." + version + ".Packet");
		packetDataSerializer = Class.forName("net.minecraft.server." + version + ".PacketDataSerializer");
		packetDataSerializerConstructor = packetDataSerializer.getConstructor(ByteBuf.class);
		packetPlayOutCustomPayloadConstructor = Class
				.forName("net.minecraft.server." + version + ".PacketPlayOutCustomPayload")
				.getConstructor(String.class, packetDataSerializer);
		craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
		init = true;
	}
	
	/**
	 * Sends all disabled LabyMod functions to the client
	 *
	 * @param player
	 *            The {@link Player} to send the data to
	 * @param labymodFunctions
	 *            A {@link Map} containing all {@link LabyModFeature}s and whether
	 *            they are enabled
	 * @throws IOException
	 *             If the connection to the client could somehow not be established
	 * @throws IllegalArgumentException
	 *             Somehow the accepted method parameter types changed. I'm
	 *             impressed.
	 * @throws ReflectiveOperationException
	 *             If something goes wrong during {@link #init}.
	 * @throws SecurityException
	 *             Someone created a {@link SecurityManager}. Not good.
	 */
	public static void setLabyModFeature(Player player, HashMap<LabyModFeature, Boolean> labymodFunctions)
			throws IOException, IllegalArgumentException, ReflectiveOperationException, SecurityException {
		if (!init) init();
		final HashMap<String, Boolean> nList = new HashMap<>();
		for (final LabyModFeature f : labymodFunctions.keySet())
			nList.put(f.name(), labymodFunctions.get(f));
		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		final ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(nList);
		final ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
		Object dataSerializer;
		Object packet;
		dataSerializer = packetDataSerializerConstructor.newInstance(a);
		packet = packetPlayOutCustomPayloadConstructor.newInstance("LABYMOD", dataSerializer);
		Object connection;
		if (MAPPED_CONNECTIONS.containsKey(player)) connection = MAPPED_CONNECTIONS.get(player);
		else {
			final Object handle = craftPlayer.getMethod("getHandle").invoke(player);
			MAPPED_CONNECTIONS.put(player, connection = handle.getClass().getField("playerConnection").get(handle));
		}
		connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, packetClass.cast(packet));
		final StringBuilder b = new StringBuilder("[AntiLaby/INFO] Disabled some LabyMod functions (");
		for (final Entry<String, Boolean> n : nList.entrySet())
			if (!n.getValue()) b.append(n.getKey() + ", ");
		AntiLaby.LOG.log(Level.INFO, b.replace(b.length() - 2, b.length(), "").toString() + ") for player "
				+ player.getName() + " (" + player.getUniqueId() + ")");

	}
	
	/**
	 * Private constructor, no need to instantiate this class
	 */
	private NmsTools() {}
	
}
