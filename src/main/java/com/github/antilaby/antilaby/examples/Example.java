package com.github.antilaby.antilaby.examples;

import com.github.antilaby.antilaby.api.LabyModFeature;
import com.github.antilaby.antilaby.api.LabyPlayer;
import com.github.antilaby.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.antilaby.antilaby.util.NmsUtils;
import org.bukkit.entity.Player;

import java.util.EnumMap;

/**
 * This class gives a few examples on how to use the AntiLaby API<br>
 * WARNING: It's highly recommended to use only methods from the package 'com.github.antilaby.antilaby.api', or, if you
 * really know what you are doing, use {@link NmsUtils} directly and handle the exceptions properly! Other solutions may
 * or may not work or be compatible for multiple Minecraft versions!
 */
public class Example {

	/**
	 * same as {@link #antiLabyPackageExample(Player)}, but uses chaining
	 */
	public static boolean antiLabyChainedPackagerExample(Player player) {
		final EnumMap<LabyModFeature, Boolean> myPackageSettings = new EnumMap<>(LabyModFeature.class);
		myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
		myPackageSettings.put(LabyModFeature.POTIONS, true);
		return new AntiLabyPackager(player).setDisabledLabyModFeatures(myPackageSettings).setForceIgnoreBypassPermission(true).sendPackages();
	}

	/**
	 * Example AntiLaby API method
	 */
	public static boolean antiLabyPackageExample(Player player) {
		// Create instance
		final AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player);

		// Get disabled functions out of the config file
		/* final HashMap<LabyModFeature, Boolean> configSettings = */
		antiLabyPackager.getConfigLabyModSettings();

		// Choose your own disabled functions
		final EnumMap<LabyModFeature, Boolean> myPackageSettings = new EnumMap<>(LabyModFeature.class);
		myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
		myPackageSettings.put(LabyModFeature.POTIONS, true);
		antiLabyPackager.setDisabledLabyModFeatures(myPackageSettings);
		/*
		 * Send the packages even if the player has the permission
		 * 'antilaby.bypass' and bypassing is enabled in the config file
		 */
		antiLabyPackager.setForceIgnoreBypassPermission(true);
		// Send the AntiLaby packages
		return antiLabyPackager.sendPackages();
	}

	/**
	 * Test if a player uses LabyMod
	 *
	 * @param player
	 * 		The Player to test
	 *
	 * @return true the player uses LabyMod, false otherwise
	 */
	public static boolean labyPlayer(Player player) {
		return LabyPlayer.doesPlayerUseLabyMod(player);
	}

	/**
	 * Example simple AntiLaby API method
	 */
	public static boolean simpleAntiLabyPackageExample(Player player) {
		// Create a LabyPlayer
		final AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player);
		// Send AntiLaby packages
		return antiLabyPackager.sendPackages();
	}

}
