package com.github.nathannr.antilaby.config;

import com.github.nathannr.antilaby.main.AntiLaby;

public class Config {

	public static boolean getEnableBypassWithPermission() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.EnableBypassWithPermission");
	}
	
	public static boolean getLabyModPlayersKickEnable() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.LabyModPlayersKick.Enable");
	}
	
	public static String getLabyModPlayersKickMessage() {
		return AntiLaby.getInstance().getConfig().getString("AntiLaby.LabyModPlayersKick.KickMessage");
	}
	
	
	public static boolean getFOOD() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.FOOD");
	}

	public static boolean getGUI() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.GUI");
	}

	public static boolean getNICK() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.NICK");
	}

	public static boolean getBLOCKBUILD() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.BLOCKBUILD");
	}

	public static boolean getCHAT() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.CHAT");
	}

	public static boolean getEXTRAS() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.EXTRAS");
	}

	public static boolean getANIMATIONS() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ANIMATIONS");
	}

	public static boolean getPOTIONS() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.POTIONS");
	}

	public static boolean getARMOR() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ARMOR");
	}

	public static boolean getDAMAGEINDICATOR() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
	}

	public static boolean getMINIMAP_RADAR() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.MINIMAP_RADAR");
	}

}
