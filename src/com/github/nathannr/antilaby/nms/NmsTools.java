package com.github.nathannr.antilaby.nms;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NmsTools {

	private static String version;
	
	static String getVersion() {
		if(version != null) return version;
		String name = Bukkit.getServer().getClass().getPackage().getName();
		version = name.substring(name.lastIndexOf('.') + 1) + ".";
		return version;
	}

	public static Class<?> getNMSClass(String className) {
		String fullName = "net.minecraft.server." + getVersion() + className;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clazz;
	}

	public static Object getNMSPlayer(Player p) {
		try {
			return p.getClass().getMethod("getHandle").invoke(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getField(Field f) {
		f.setAccessible(true);
		return f;
	}

}
