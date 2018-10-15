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
	 * The build id of the current version
	 * TODO
	 */
	public static final int VERSION_ID = 2;
	/**
	 * The SpigotMC.org resource id
	 */
	public static final int RESOURCE_ID = 21347;
	/**
	 * The SpigotMC.org resource link
	 */
	public static final String RESOURCE_LINK = "https://www.spigotmc.org/resources/" + RESOURCE_ID + '/';
	/**
	 * The SpigotMC.org legacy version link
	 */
	public static final String UPDATE_URL = "https://api.spigotmc.org/legacy/update.php?resource=" + RESOURCE_ID;
	/**
	 * The AntiLaby chat prefix
	 */
	public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8[&e&lAntiLaby&8]&r ");
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
	 * The plug-in channel LabyMod uses to exchange information with the server (new name)
	 */
	public static final String LABYMOD_CHANNEL = "LMC";
	/**
	 * The plug-in channel LabyMod uses to exchange information with the server (old name)
	 */
	public static final String LABYMOD_CHANNEL_OLD = "LABYMOD";
	/**
	 * The current configuration version
	 */
	public static final int CURRENT_CONFIG_VERSION = 3;
	/**
	 * Link to the git repository
	 */
	public static final String GITHUB_URL = "https://github.com/AntiLaby/AntiLaby/";
	/**
	 * Link to the bugtracker
	 */
	public static final String BUG_REPORT_URL = "https://github.com/AntiLaby/AntiLaby/issues";

	/**
	 * Url to the wiki of AntiLaby
	 */
	public static final String WIKI_URL = "https://github.com/AntiLaby/AntiLaby/wiki";

	/**
	 * Url to the wiki of AntiLaby: LabyModPlayerBan
	 */
	public static final String WIKI_LABYMODPLAYERBAN_URL = "https://github.com/AntiLaby/AntiLaby/wiki/LabyMod-Player-Ban";

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private Constants() {
		throw new UnsupportedOperationException();
	}

}
