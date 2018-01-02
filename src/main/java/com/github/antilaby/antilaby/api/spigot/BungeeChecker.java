package com.github.antilaby.antilaby.api.spigot;

import java.lang.reflect.Field;

public class BungeeChecker {

	/**
	 * Check if a server uses BungeeCord
	 * 
	 * @author NathanNr
	 */

	private boolean isBungeecord;

	public BungeeChecker() {
		init();
	}

	public void init() {
		try {
			Field field = Class.forName("org.spigotmc.SpigotConfig").getDeclaredField("bungee");
			field.setAccessible(true);
			this.isBungeecord = field.getBoolean(null);
		} catch (Exception e) {
			this.isBungeecord = false;
		}
	}

	public boolean isBungeecord() {
		return isBungeecord;
	}

}
