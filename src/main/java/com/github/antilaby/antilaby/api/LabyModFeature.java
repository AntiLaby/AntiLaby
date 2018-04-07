package com.github.antilaby.antilaby.api;

/**
 * Provides every configurable LabyMod feature
 *
 * @author NathanNr
 */
public enum LabyModFeature {
	// Old LabyMod 2 features
	FOOD("enabled"), GUI("enabled"), NICK("enabled"), EXTRAS("enabled"), POTIONS("enabled"), ARMOR(
			"enabled"), DAMAGEINDICATOR("enabled"), MINIMAP_RADAR("enabled"),

	// LabyMod 3 features
	BLOCKBUILD("false"), IMPROVED_LAVA("false"), CROSSHAIR_SYNC("false"), REFILL_FIX("false"), GUI_ALL(
			"enabled"), GUI_POTION_EFFECTS("enabled"), GUI_ARMOR_HUD("enabled"), GUI_ITEM_HUD(
					"enabled"), TAGS("enabled"), CHAT("enabled"), ANIMATIONS("enabled"), SATURATION_BAR("enabled");

	private final String defaultValue;

	LabyModFeature(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Get the default value from LabyMod
	 * 
	 * @return default value
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}
}