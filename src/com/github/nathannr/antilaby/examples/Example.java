package com.github.nathannr.antilaby.examples;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.LabyModFeature;
import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;

public class Example {

	/**
	 * Example class how to use the AntiLaby API
	 * 
	 * WARNING: It's highly recommended to use only methods from the package 'com.github.nathannr.antilaby.api'!
	 * Other's may not work or be compatible for multiple Minecraft versions!
	 */
	
	
	/**
	 * Example simple AntiLaby API method
	 * @author NathanNr
	 */
	public static void simpleAntiLabyPackageExample(Player player) {
		AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player); // Create instance
		antiLabyPackager.sendPackages(); // Send AntiLaby packages using the settings out of the config file
	}
	
	/**
	 * Example AntiLaby API method
	 * @author NathanNr
	 */
	public static void antiLabyPackageExample(Player player) {
		AntiLabyPackager antiLabyPackager = new AntiLabyPackager(player); // Create instance
		
		@SuppressWarnings("unused")
		HashMap<LabyModFeature, Boolean> configSettings = antiLabyPackager.getConfigLabyModSettings(); // Get disabled functions out of the config file
		
		// Chose your own disabled functions
		HashMap<LabyModFeature, Boolean> myPackageSettings = new HashMap<>();
		myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
		myPackageSettings.put(LabyModFeature.POTIONS, true);
		antiLabyPackager.setDisabledLabyModFeatures(myPackageSettings);
		
		antiLabyPackager.setForceIgnoreBypassPerission(true); // Send the packages even if the player has the permission 'antilaby.bypass' and bypassing is enabled in the config file
		
		antiLabyPackager.sendPackages(); //Finally send the AntiLaby packages
	}
	
}
