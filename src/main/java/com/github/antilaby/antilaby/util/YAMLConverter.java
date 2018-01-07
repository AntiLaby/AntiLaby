package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.lang.impl.LanguageVersion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Does all Converting Magic.
 */
public final class YAMLConverter {

	/**
	 * Flats the given Map by joining keys
	 *
	 * @param source
	 * 		the nested Map
	 *
	 * @return the flattened Map
	 */
	@SuppressWarnings("unchecked")
	public static SortedMap<String, Object> flatten(Map<String, Object> source) {
		SortedMap<String, Object> result = new TreeMap<>();
		for(String key : source.keySet()) {
			Object value = source.get(key);
			if(value instanceof Map) {
				Map<String, Object> subMap = flatten((Map<String, Object>) value);
				for(String subkey : subMap.keySet()) result.put(key + '.' + subkey, subMap.get(subkey));
			} else if(value instanceof Collection) {
				StringBuilder joiner = new StringBuilder();
				String separator = "";
				for(Object element : ((Collection) value)) {
					Map<String, Object> subMap = flatten(Collections.singletonMap(key, element));
					joiner.append(separator).append(subMap.entrySet().iterator().next().getValue().toString());

					separator = ",";
				}
				result.put(key, joiner.toString());
			} else result.put(key, value);
		}
		return result;
	}

	/**
	 * Converts the given Yaml Language Configuration to the new one.
	 * There are two ways of using this:<br><br>
	 * 1.
	 * appendHeader == true, Just print the configuration via<br><br>
	 * <code>for(Map.Entry&lt;String, String&gt; e : convertToLocale (true, yaml).entrySet())<br>
	 * yourWritingMethod(e.getKey() + '=' + e.getValue());</code><br><br>
	 * 2. appendHeader == false, set header manually:<br><br>
	 * <code>yourWritingMethod("&#35;version: " + LanguageVersion.CURRENT_VERSION.toString());<br>
	 * for(Map.Entry&lt;String, String&gt; e : convertToLocale (false, yaml).entrySet())<br>
	 * yourWritingMethod(e.getKey() + '=' + e.getValue());</code>
	 *
	 * @param appendVersion
	 * 		whether the LanguageVersion header should be printed
	 * @param yaml
	 * 		the YamlConfiguration to convert
	 *
	 * @return the resulting Map
	 */
	public static SortedMap<String, String> convertToLocale(boolean appendVersion, YamlConfiguration yaml) {
		System.out.println("#version" + LanguageVersion.CURRENT_VERSION);
		SortedMap<String, String> result = new TreeMap<>();
		if(appendVersion) result.put("#version", LanguageVersion.CURRENT_VERSION.toString());
		for(Map.Entry<String, Object> entry : flatten(new Yaml().load(yaml.saveToString())).entrySet())
			if(entry.getValue() instanceof String) result.put(entry.getKey(), (String) entry.getValue());
		return result;
	}

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private YAMLConverter() {throw new UnsupportedOperationException();}
}