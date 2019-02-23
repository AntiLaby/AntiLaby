package com.github.antilaby.antilaby.api;

/**
 * Provides every configurable LabyMod feature.
 *
 * @author NathanNr
 */
public enum LabyModFeature {
  // Old LabyMod 2 features
  FOOD(true, 2), GUI(true, 2), NICK(true, 2), EXTRAS(true, 2),
  POTIONS(true, 2), ARMOR(true, 2), DAMAGEINDICATOR(true, 2), MINIMAP_RADAR(true, 2),

  // LabyMod 3 features
  BLOCKBUILD(false, 3), IMPROVED_LAVA(false, 3), CROSSHAIR_SYNC(false, 3), REFILL_FIX(false, 3),
  GUI_ALL(true, 3), GUI_POTION_EFFECTS(true, 3), GUI_ARMOR_HUD(true, 3), GUI_ITEM_HUD(true, 3),
  TAGS(true, 3), CHAT(true, 3), ANIMATIONS(true, 3), SATURATION_BAR(true, 3);

  private final boolean defaultValue;
  private final int version;

  LabyModFeature(boolean defaultValue, int version) {
    this.version = version;
    this.defaultValue = defaultValue;
  }

  /**
   * Get the LabyMod version of this feature.
   *
   * @return the version (either 2 or 3 as of now)
   */
  public int getVersion() {
    return version;
  }

  /**
   * Get the default value from LabyMod.
   *
   * @return default value
   */
  public String getDefaultValue() {
    return this.defaultValue ? "enabled" : "disabled";
  }

}