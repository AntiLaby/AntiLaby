package com.github.antilaby.antilaby.api.antilabypackages;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.antilaby.DummyServer;
import com.github.antilaby.antilaby.api.LabyModFeature;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AntiLabyPackagerTest {

  @BeforeAll
  public static void setup() throws ReflectiveOperationException {
    Field f = Bukkit.class.getDeclaredField("server");
    f.setAccessible(true);
    f.set(null, new DummyServer());
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

    AntiLabyPackager antiLabyPackager = new AntiLabyPackager(disabledFeatures, enabledFeatures);

    //when
    Map<LabyModFeature, Boolean> labyModFeatureSettings = new EnumMap<>(LabyModFeature.class);
    labyModFeatureSettings.put(LabyModFeature.TAGS, false);
    labyModFeatureSettings.put(LabyModFeature.IMPROVED_LAVA, false);
    labyModFeatureSettings.put(LabyModFeature.ANIMATIONS, false);

    labyModFeatureSettings.put(LabyModFeature.REFILL_FIX, true);
    labyModFeatureSettings.put(LabyModFeature.BLOCKBUILD, true);

    //then
    assertEquals(antiLabyPackager.getLabyModFeatureSettings(), labyModFeatureSettings);
  }

}
