package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import org.bukkit.ChatColor;

/**
 * Contains all relevant constants of AntiLaby
 *
 * @author heisluft
 */
public final class Constants {

	/**
	 * The SpigotMC.org resource id
	 */
	public static final int RESOURCE_ID = 21347;
	/**
	 * The AntiLaby chat prefix
	 */
	public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8[&e&lAntiLaby&8]&r ");
	/**
	 * The SpigotMC.org resource link
	 */
	public static final String RESOURCE_LINK = "https://www.spigotmc.org/resources/" + RESOURCE_ID + '/';
	/**
	 * The SpigotMC.org legacy version link
	 */
	public static final String UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID;
	/**
	 * The permission for bypassing the {@link AntiLabyPackager}
	 */
	public static final String PERMISSION_BYPASS = "antilaby.bypass";
	/**
	 * The permission for bypassing the commands invoked when joining while having LabyMod activated
	 */
	public static final String PERMISSION_BYPASS_JOIN_COMMANDS = "antilaby.bypasscommands";
	/**
	 * The permission to use /labyinfo
	 */
	public static final String PERMISSION_LABYINFO = "antilaby.labyinfo";
	/**
	 * The permission to receive notifications about players who are using LabyMod
	 */
	public static final String PERMISSION_LABYINFO_NOTIFICATIONS = "antilaby.labyinfo.notifications";
	/**
	 * The plugin channel LabyMod uses to exchange information with the server
	 */
	public static final String LABYMOD_CHANNEL = "LABYMOD";
	/**
	 * The current configuration version
	 */
	public static final int CURRENT_CONFIG_VERSION = 3;

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private Constants() {
		throw new UnsupportedOperationException();
	}

}
