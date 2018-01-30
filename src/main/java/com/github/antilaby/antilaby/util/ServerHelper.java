package com.github.antilaby.antilaby.util;

import java.lang.reflect.Field;

/**
 * Check if a server uses BungeeCord
 *
 * @author NathanNr, heisluft
 */
public final class ServerHelper {

	/**
	 * The servers Bukkit implementation
	 */
	public enum ServerType {
		/** Server is CraftBukkit only */CRAFTBUKKIT, /** Server is Spigot */SPIGOT,
		/** Server is PaperSpigot */PAPERSPIGOT
	}

	private static boolean isBungeeCord;
	private static ServerType type;

	/**
	 * Gets this servers Bukkit implementation
	 *
	 * @return the ServerType
	 */
	public static ServerType getServerType() {
		if(type == null) init();
		return type;
	}

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private ServerHelper() {throw new UnsupportedOperationException();}

	private static void init() {
		Class<?> spigotClass;
		try {
			spigotClass = Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException e) {
			type = ServerType.CRAFTBUKKIT;
			isBungeeCord = false;
			return;
		}
		ServerType spigotType;
		try {
			Class.forName("com.destroystokyo.paper.PaperCommand");
			spigotType = ServerType.PAPERSPIGOT;
		} catch(Exception e) {
			spigotType = ServerType.SPIGOT;
		}
		type = spigotType;
		boolean bungeeCord;
		try {
			Field field = spigotClass.getDeclaredField("bungee");
			field.setAccessible(true);
			bungeeCord = field.getBoolean(null);
		} catch(Exception e) {
			bungeeCord = false;
		}
		isBungeeCord = bungeeCord;
	}

	/**
	 * Gets whether this Server is part of a BungeeCord network
	 *
	 * @return true if User enabled the bungeeCord option in the servers spigot.yml
	 */
	public static boolean isBungeeCord() {
		if(type == null) init();
		return isBungeeCord;
	}

}
