package com.github.antilaby.antilaby.config;

import java.util.List;

import com.github.antilaby.antilaby.main.AntiLaby;
import com.google.common.collect.ObjectArrays;

public final class Config {

	private final static CharSequence NODE_PREFIX = "AntiLaby";
	private final static CharSequence NODE_DISABLE_PREFIX = "disable";
	private final static char   NODE_SEPERATOR = '.';

	private static String concatValues(CharSequence... values) {
		ObjectArrays.concat(NODE_PREFIX, values);
		return String.join(String.valueOf(NODE_SEPERATOR), values);
	}

	public static boolean isAnimationsDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "ANIMATIONS"));
	}

	public static boolean isArmorDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "ARMOR"));
	}

	public static boolean isBlockBuildDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "BLOCKBUILD"));
	}

	public static boolean isChatDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "CHAT"));
	}

	public static boolean isDamageIndicatorDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "DAMAGEINDICATOR"));
	}
	
	public static boolean isBypassWithPermissionsDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues("EnableBypassWithPermission"));
	}
	
	public static boolean isExtrasDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "EXTRAS"));
	}
	
	public static boolean isFoodDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "FOOD"));
	}
	
	public static boolean isGuiDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean(concatValues(NODE_DISABLE_PREFIX, "GUI"));
	}

	/*
	* I don't concat here |
	* because deprecated  ↓
	*/
	
	@Deprecated
	/**
	 * Please use LabyModJoinCommands#getLabyModJoinCommands()
	 * @author NathanNr
	 */
	public static List<String> getLabyModPlayerCommands() {
		return AntiLaby.getInstance().getConfig().getStringList("AntiLaby.LabyModPlayerCommands");
	}
	
	public static boolean isKickEnabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.LabyModPlayerKick.Enable");
	}
	
	@Deprecated
	public static String getLabyModPlayersKickMessage() {
		return AntiLaby.getInstance().getConfig().getString("AntiLaby.LabyModPlayersKick.KickMessage");
	}
	
	public static boolean isMinimapRadarDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.MINIMAP_RADAR");
	}
	
	public static boolean isNickDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.NICK");
	}
	
	public static boolean isPotionConsumeDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.POTIONS");
	}
	
	public static boolean isAutoUpdateEnabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.Update.AutoUpdate");
	}
	
}
