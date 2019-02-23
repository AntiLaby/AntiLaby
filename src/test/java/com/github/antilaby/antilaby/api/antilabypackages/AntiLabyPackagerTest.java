package com.github.antilaby.antilaby.api.antilabypackages;

import com.github.antilaby.DummyServer;
import com.github.antilaby.antilaby.api.LabyModFeature;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the packager.
 *
 * @author NathanNr
 */
public class AntiLabyPackagerTest {

  private static AntiLabyPackager antiLabyPackager;

  /**
   * Hack the bukkit class.
   *
   * @throws ReflectiveOperationException if hacking fails
   */
  @BeforeAll
  public static void setup() throws ReflectiveOperationException {
    Field f = Bukkit.class.getDeclaredField("server");
    f.setAccessible(true);
    f.set(null, new DummyServer());
  }

  @BeforeEach
  public void init() {
    antiLabyPackager = new AntiLabyPackager();
  }

  @Test
  public void assertThatMapLabyModSettingsMapsCorrectly() {
    //given
    List<String> disabledFeatures = new ArrayList<>();
    disabledFeatures.add("TAGS");
    disabledFeatures.add("IMPROVED_LAVA");
    disabledFeatures.add("ANIMATIONS");

    List<String> enabledFeatures = new ArrayList<>();
    enabledFeatures.add("REFILL_FIX");
    enabledFeatures.add("BLOCKBUILD");

    antiLabyPackager.mapLabyModSettings(disabledFeatures, enabledFeatures);

    //when
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    labyModFeatureSettings.put(LabyModFeature.TAGS, false);
    labyModFeatureSettings.put(LabyModFeature.IMPROVED_LAVA, false);
    labyModFeatureSettings.put(LabyModFeature.ANIMATIONS, false);

    labyModFeatureSettings.put(LabyModFeature.REFILL_FIX, true);
    labyModFeatureSettings.put(LabyModFeature.BLOCKBUILD, true);

    //then
    Assertions.assertEquals(labyModFeatureSettings, antiLabyPackager.getRuleset());
  }

  @Test
  public void assertThatUseLabyModDefaultsClearsTheSettings() {
    //given
    List<String> disabledFeatures = new ArrayList<>();
    disabledFeatures.add("TAGS");
    disabledFeatures.add("IMPROVED_LAVA");
    disabledFeatures.add("ANIMATIONS");

    List<String> enabledFeatures = new ArrayList<>();
    enabledFeatures.add("REFILL_FIX");
    enabledFeatures.add("BLOCKBUILD");

    antiLabyPackager.mapLabyModSettings(disabledFeatures, enabledFeatures);

    //when
    antiLabyPackager.useLabyModDefaults();

    //then
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    Assertions.assertEquals(labyModFeatureSettings, antiLabyPackager.getRuleset());
  }
}
