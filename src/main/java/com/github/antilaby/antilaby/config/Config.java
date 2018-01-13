package com.github.antilaby.antilaby.config;

import com.github.antilaby.antilaby.main.AntiLaby;

import java.util.List;

/**
 * Get values from the configuration file. This class will get removed if the
 * new configuration API is ready to use!
 * 
 * @author NathanNr
 */
public class Config {

	public static boolean getANIMATIONS() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ANIMATIONS");
	}

	public static boolean getARMOR() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ARMOR");
	}

	public static boolean getBLOCKBUILD() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.BLOCKBUILD");
	}

	public static boolean getCHAT() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.CHAT");
	}

	public static boolean getDAMAGEINDICATOR() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
	}

	public static boolean getEnableBypassWithPermission() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.EnableBypassWithPermission");
	}

	public static boolean getEXTRAS() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.EXTRAS");
	}

	public static boolean getFOOD() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.FOOD");
	}

	public static boolean getGUI() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.GUI");
	}

	/**
	 * Please use LabyModJoinCommands#getLabyModJoinCommands()
	 */
	@Deprecated
	public static List<String> getLabyModPlayerCommands() {
		return AntiLaby.getInstance().getConfig().getStringList("AntiLaby.LabyModPlayerCommands");
	}

	public static boolean getLabyModPlayerKickEnable() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.LabyModPlayerKick.Enable");
	}

	@Deprecated
	public static String getLabyModPlayersKickMessage() {
		return AntiLaby.getInstance().getConfig().getString("AntiLaby.LabyModPlayersKick.KickMessage");
	}

	public static boolean getMINIMAP_RADAR() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.MINIMAP_RADAR");
	}

	public static boolean getNICK() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.NICK");
	}

	public static boolean getPOTIONS() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.POTIONS");
	}

	public static boolean isAutoUpdateEnabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.Update.AutoUpdate");
	}

	public static boolean isDebugModeEnabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.DebugMode");
	}

}
