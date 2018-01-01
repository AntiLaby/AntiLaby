package com.github.antilaby.antilaby.nms;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.log.Logger;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.Reflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The class for NMS reflection magic
 *
 * @author heisluft
 */
public final class NmsTools {
	private static final Logger LOG = new Logger("Reflection");
	private static final String NMS = "net.minecraft.server.";
	private static final String OBC = "org.bukkit.craftbukkit.";
	private static Class<?> craftPlayer;
	private static Class<?> packetDataSerializer;
	private static Class<?> packetClass;
	private static Constructor<?> packetDataSerializerConstructor;
	private static Constructor<?> packetPlayOutCustomPayloadConstructor;
	private static boolean init;
	private static String version;
	
	/**
	 * Gets the servers NMS version, for example 1_8_8
	 *
	 * @return The NMS version
	 */
	public static String getVersion() {
		if(!init) try {
			init();
		} catch(final ReflectiveOperationException e) {
			LOG.error(e.getMessage());
		}
		return version;
	}
	
	public static String getLang(Player p) {
		if(!init) try {
			init();
		} catch(final ReflectiveOperationException e) {
			LOG.error(e.getMessage());
		}
		return Reflection.getField(craftPlayer.cast(p), "locale");
	}
	
	/**
	 * Initializes all static fields
	 *
	 * @throws ReflectiveOperationException
	 * 		If something failed during reflection
	 */
	private static void init() throws ReflectiveOperationException {
		if(init) return;
		final String name = Bukkit.getServer().getClass().getPackage().getName();
		version = name.substring(name.lastIndexOf('.') + 1);
		packetClass = Class.forName(NMS + version + ".Packet");
		packetDataSerializer = Class.forName(NMS + version + ".PacketDataSerializer");
		packetDataSerializerConstructor = packetDataSerializer.getConstructor(ByteBuf.class);
		packetPlayOutCustomPayloadConstructor = Class.forName(NMS + version + ".PacketPlayOutCustomPayload")
				                                        .getConstructor(String.class, packetDataSerializer);
		craftPlayer = Class.forName(OBC + version + ".entity.CraftPlayer");
		init = true;
	}
	
	/**
	 * Sends all disabled LabyMod functions to the client
	 *
	 * @param player
	 * 		The {@link Player} to send the data to
	 * @param labymodFunctions
	 * 		A {@link Map} containing all {@link LabyModFeature}s and whether they are enabled
	 *
	 * @throws IOException
	 * 		If the connection to the client could somehow not be established
	 * @throws IllegalArgumentException
	 * 		Somehow the accepted method parameter types changed. I'm impressed.
	 * @throws ReflectiveOperationException
	 * 		If something goes wrong during {@link #init}.
	 * @throws SecurityException
	 * 		Someone created a {@link SecurityManager}. Not good.
	 */
	public static void setLabyModFeature(Player player, Map<LabyModFeature, Boolean> labymodFunctions)
			throws IOException, ReflectiveOperationException {
		if(!init) init();
		final HashMap<String, Boolean> nList = new HashMap<>();
		for(final Entry<LabyModFeature, Boolean> entry : labymodFunctions.entrySet())
			nList.put((entry.getKey()).name(), entry.getValue());
		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		final ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(nList);
		final ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
		Object dataSerializer = packetDataSerializerConstructor.newInstance(a);
		Object packet = packetPlayOutCustomPayloadConstructor.newInstance("LABYMOD", dataSerializer);
		Object handle = craftPlayer.getMethod("getHandle").invoke(player);
		Object connection = handle.getClass().getField("playerConnection").get(handle);
		connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, packetClass.cast(packet));
		final StringBuilder b = new StringBuilder("[AntiLaby/INFO] Disabled some LabyMod functions (");
		for(final Entry<String, Boolean> n : nList.entrySet())
			if(!n.getValue()) b.append(n.getKey()).append(", ");
		AntiLaby.LOG.info(b.replace(b.length() - 2,
				b.length(),
				"").append(") for player ").append(player.getName()).append(" (").append(player.getUniqueId()).append(
				')').toString());
		out.close();
	}
	
	/**
	 * Private constructor, no need to instantiate this class
	 */
	private NmsTools() {}
	
}
