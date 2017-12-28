package com.github.antilaby.antilaby;

import com.github.antilaby.antilaby.lang.impl.LangFileParser;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;

public class TestClass {
	
	static String toCSV(String yamldata) {
		Map<String, String> rt = new HashMap<>();
		String currentKey = "";
		Map<String, Object> yaml = Yaml.<Map<String, Object>>load(yamldata);
		for(Map.Entry<String, Object> entry : yaml.entrySet()) System.out.println(entry);
		return "";
	}
	
	@Test
	public void n() {
		final YamlConfiguration yaml = new YamlConfiguration();
		yaml.set("sdl.sss", "%PLAYER% is an ideot");
		yaml.set("sdls.sssr", "%PLAYER% is an ideot");
		yaml.set("sdls.sssr.a", "%PLAYER% is an ideot");
		yaml.set("sdl.sssr", "%PLAYER% is an ideot");
		toCSV(yaml);
		LangFileParser.migrateYamlToLang(yaml);
	}
}