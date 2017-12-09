package de.heisluft.antilaby.nms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NmsTools {
	
	private static Class<?> craftPlayer;
	private static Class<?> packetDataSerializer;
	private static Class<?> packetClass;
	private static Constructor<?> packetDataSerializerConstructor;
	private static Constructor<?> packetPlayOutCustomPayloadConstructor;
	private static Map<Player, Object> mappedConnections = new HashMap<>();
	private static boolean init;

	private static String version;

	public static String getVersion() {
		if (!init) try {
			init();
		} catch (final ReflectiveOperationException e) {
			e.printStackTrace();
		}
		return version;
	}
	
	private static void init() throws ReflectiveOperationException {
		if (init) return;
		final String name = Bukkit.getServer().getClass().getPackage().getName();
		version = name.substring(name.lastIndexOf('.') + 1) + ".";
		packetClass = Class.forName("net.minecraft.server." + version + "Packet");
		packetDataSerializer = Class.forName("net.minecraft.server." + version + "PacketDataSerializer");
		packetDataSerializerConstructor = packetDataSerializer.getConstructor(ByteBuf.class);
		packetPlayOutCustomPayloadConstructor = Class
				.forName("net.minecraft.server." + version + "PacketPlayOutCustomPayload")
				.getConstructor(String.class, packetDataSerializer);
		craftPlayer = Class.forName("org.bukkit.craftbukkit." + version + "entity.CraftPlayer");
		init = true;
	}
	
	public static void setLabyModFeature(Player p, HashMap<LabyModFeature, Boolean> list)
			throws IOException, IllegalArgumentException, ReflectiveOperationException, SecurityException {
		if (!init) init();
		final HashMap<String, Boolean> nList = new HashMap<>();
		for (final LabyModFeature f : list.keySet())
			nList.put(f.name(), list.get(f));
		final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		final ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(nList);
		final ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
		Object dataSerializer;
		Object packet;
		dataSerializer = packetDataSerializerConstructor.newInstance(a);
		packet = packetPlayOutCustomPayloadConstructor.newInstance("LABYMOD", dataSerializer);
		Object connection;
		if (mappedConnections.containsKey(p)) connection = mappedConnections.get(p);
		else {
			final Object handle = craftPlayer.getMethod("getHandle").invoke(p);
			mappedConnections.put(p, connection = handle.getClass().getField("playerConnection").get(handle));
		}
		connection.getClass().getMethod("sendPacket", packetClass).invoke(connection, packetClass.cast(packet));
		final StringBuilder b = new StringBuilder("[AntiLaby/INFO] Disabled some LabyMod functions (");
		for (final Entry<String, Boolean> n : nList.entrySet())
			if (!n.getValue()) b.append(n.getKey() + ", ");
		System.out.print(b.replace(b.length() - 2, b.length(), "").toString() + ") for player " + p.getName() + " ("
				+ p.getUniqueId() + ")");

	}
	
}
