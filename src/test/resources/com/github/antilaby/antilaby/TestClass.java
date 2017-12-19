package com.github.antilaby.antilaby;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

import com.github.antilaby.antilaby.lang.impl.LangFileParser;

public class TestClass {
	
	@Test
	public void n() {
		final YamlConfiguration yaml = new YamlConfiguration();
		yaml.createSection("nad");
		yaml.addDefault("nad.sdl.sss", "%PLAYER% is an ideot");
		LangFileParser.migrateYamlToLang(yaml);
	}
}
