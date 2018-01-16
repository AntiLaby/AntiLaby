package com.github.antilaby.antilaby.api.spigot;

import java.lang.reflect.Field;

/**
 * Check if a server uses BungeeCord
 *
 * @author NathanNr, heisluft
 */
public class ServerHelper {

	/**
	 * The servers Bukkit implementation
	 */
	public enum ServerType {
		/** Server is CraftBukkit only */CRAFTBUKKIT, /** Server is Spigot */SPIGOT,
		/** Server is PaperSpigot */PAPERSPIGOT
	}

	private final boolean isBungeeCord;
	private final ServerType type;

	/**
	 * Gets this servers Bukkit implementation
	 *
	 * @return the ServerType
	 */
	public ServerType getServerType() {
		return type;
	}

	public ServerHelper() {
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
	public boolean isBungeeCord() {
		return isBungeeCord;
	}

}
