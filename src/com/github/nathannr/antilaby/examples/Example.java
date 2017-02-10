package com.github.nathannr.antilaby.examples;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager;
import com.github.nathannr.antilaby.api.antilabypackages.AntiLabyPackager.LabyModFeature;

public class Example {

	/**
	 * Example class how to use the AntiLaby API
	 * 
	 * WARNING: It's highly recommended to use only methods from the package 'com.github.nathannr.antilaby.api'!
	 * Other's may not be compatible for multiple Minecraft versions!
	 */
	
	
	/**
	 * Example simple AntiLaby API method
	 * @author NathanNr
	 */
	public static void simpleAntiLabyPackageExample(Player p) {
		AntiLabyPackager alp = new AntiLabyPackager(p); // Create instance
		alp.sendPackages(); // Send AntiLaby packages using the settings out of the config file
	}
	
	/**
	 * Example AntiLaby API method
	 * @author NathanNr
	 */
	public static void antiLabyPackageExample(Player p) {
		AntiLabyPackager alp = new AntiLabyPackager(p); // Create instance
		
		@SuppressWarnings("unused")
		HashMap<LabyModFeature, Boolean> configSettings = alp.getConfigLabyModSettings(); // Get disabled functions out of the config file
		
		// Chose your own disabled functions
		HashMap<LabyModFeature, Boolean> myPackageSettings = new HashMap<>();
		myPackageSettings.put(LabyModFeature.DAMAGEINDICATOR, true);
		myPackageSettings.put(LabyModFeature.POTIONS, true);
		alp.setDisabledLabyModFeatures(myPackageSettings);
		
		alp.setForceIgnoreBypassPerission(true); // Send the packages even if the player has the permission 'antilaby.bypass' and bypassing is enabled in the config file
		
		alp.sendPackages(); //Finally send the AntiLaby packages
	}
	
}
