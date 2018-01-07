package com.github.antilaby.antilaby.api.spigot;

import java.lang.reflect.Field;

/**
 * Check if a server uses BungeeCord
 *
 * @author NathanNr
 */
public class BungeeChecker {

	private boolean isBungeecord;

	public BungeeChecker() {
		try {
			Field field = Class.forName("org.spigotmc.SpigotConfig").getDeclaredField("bungee");
			field.setAccessible(true);
			this.isBungeecord = field.getBoolean(null);
		} catch(Exception e) {
			this.isBungeecord = false;
		}
	}

	public boolean isBungeecord() {
		return isBungeecord;
	}

}
