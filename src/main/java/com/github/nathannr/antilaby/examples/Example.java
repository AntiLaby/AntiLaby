package com.github.nathannr.antilaby.examples;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;
import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;

import de.heisluft.antilaby.nms.NmsTools;

/**
 * This class gives a few examples on how to use the AntiLaby API<br>
 *
 * WARNING: It's highly recommended to use only methods from the package
 * 'com.github.nathannr.antilaby.api', or, if you really know what you are
 * doing, use {@link NmsTools} directly and handle the exceptions properly!
 * Other solutions may or may not work or be compatible for multiple Minecraft
 * versions!
 */
public class Example {

	/**
	 * same as {@link #antiLabyPackageExample(Player)}, but uses chaining
	 *
	 * @author heisluft
	 *
	 */
	public static boolean antiLabyChainedPackagerExample(Player player) {
		final HashMap<LabyModFeature, Boolean> myPackageSettings = new HashMap<>();
		myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
		myPackageSettings.put(LabyModFeature.POTIONS, true);
		return new AntiLabyPackager(player).setDisabledLabyModFeatures(myPackageSettings)
				.setForceIgnoreBypassPermission(true).sendPackages();
	}
	
	/**
	 * Example AntiLaby API method
	 *
	 * @author NathanNr
	 */
	public static boolean antiLabyPackageExample(Player player) {
		// Create instance
		final AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player);

		// Get disabled functions out of the config file
		@SuppressWarnings("unused")
		final HashMap<LabyModFeature, Boolean> configSettings = antiLabyPackager.getConfigLabyModSettings();
		
		// Choose your own disabled functions
		final HashMap<LabyModFeature, Boolean> myPackageSettings = new HashMap<>();
		myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
		myPackageSettings.put(LabyModFeature.POTIONS, true);
		antiLabyPackager.setDisabledLabyModFeatures(myPackageSettings);
		/*
		 * Send the packages even if the player has the permission 'antilaby.bypass' and
		 * bypassing is enabled in the config file
		 */
		antiLabyPackager.setForceIgnoreBypassPermission(true);
		// Send the AntiLaby packages
		return antiLabyPackager.sendPackages();
	}

	/**
	 * Example simple AntiLaby API method
	 *
	 * @author NathanNr
	 */
	public static boolean simpleAntiLabyPackageExample(Player player) {
		final AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player); // Create instance
		return antiLabyPackager.sendPackages(); // Send AntiLaby packages using the settings out of the config file
	}

}
