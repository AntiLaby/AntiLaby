package com.github.antilaby.antilaby.config;

import java.util.List;

import com.github.antilaby.antilaby.main.AntiLaby;

public class Config {
	
	public static boolean isAnimationsDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ANIMATIONS");
	}

	public static boolean isArmorDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.ARMOR");
	}

	public static boolean isBlockBuildDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.BLOCKBUILD");
	}

	public static boolean isChatDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.CHAT");
	}

	public static boolean isDamageIndicatorDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.DAMAGEINDICATOR");
	}
	
	public static boolean isBypassWithPermissionsDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.EnableBypassWithPermission");
	}
	
	public static boolean isExtrasDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.EXTRAS");
	}
	
	public static boolean isFoodDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.FOOD");
	}
	
	public static boolean isGuiDisabled() {
		return AntiLaby.getInstance().getConfig().getBoolean("AntiLaby.disable.GUI");
	}
	
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
