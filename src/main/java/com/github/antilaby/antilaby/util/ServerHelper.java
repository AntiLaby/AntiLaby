package com.github.antilaby.antilaby.util;

import java.lang.reflect.Field;

/**
 * Provides Information about this Server
 *
 * @author NathanNr, heisluft
 */
public final class ServerHelper {

	/**
	 * The servers Bukkit implementation
	 */
	public enum ImplementationType {
		/** Server is CraftBukkit based */
		CRAFT_BUKKIT,
		/** Server is Glowstone based */
		GLOWSTONE
	}

	/**
	 * The servers Bukkit API type
	 */
	public enum ApiType {
		/** Server only implements Bukkit */
		BUKKIT,
		/** Server implements Spigot */
		SPIGOT,
		/** Server implements PaperSpigot */
		PAPER_SPIGOT
	}

	private static boolean isBungeeCord;
	private static ApiType type;
	private static ImplementationType implementation;

	/**
	 * Gets this servers Bukkit Api type
	 *
	 * @return the APIType
	 */
	public static ApiType getServerType() {
		if(type == null) init();
		return type;
	}

	/**
	 * Gets this servers Bukkit implementation
	 *
	 * @return the ImplementationType
	 */
	public static ImplementationType getImplementation() {
		if(implementation == null) init();
		return implementation;
	}

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private ServerHelper() {throw new UnsupportedOperationException();}

	private static void init() {
		try {
			Class.forName("net.glowstone.GlowServer");
			type = ApiType.PAPER_SPIGOT;
			implementation = ImplementationType.GLOWSTONE;
		} catch(ClassNotFoundException e) {
			implementation = ImplementationType.CRAFT_BUKKIT;
		}
		Class<?> spigotClass;
		try {
			spigotClass = Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException e) {
			type = ApiType.BUKKIT;
			isBungeeCord = false;
			return;
		}
		ApiType spigotType;
		try {
			Class.forName("com.destroystokyo.paper.PaperCommand");
			spigotType = ApiType.PAPER_SPIGOT;
		} catch(Exception e) {
			spigotType = ApiType.SPIGOT;
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
