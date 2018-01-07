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
	 * Converts the given Yaml String-only Configuration to Properties.<p>Usage:</p>
	 * <p><code>yourWritingMethod("&#35;version: " + {@link LanguageVersion#CURRENT_VERSION}.toString());<br>
	 * for(Map.Entry&lt;String, String&gt; e : convertToLocale (yaml).entrySet())<br> yourWritingMethod(e.getKey() + '='
	 * + e.getValue());</code></p><i>Warning: Non-String entries (like Collections) will be lost</i>
	 *
	 * @param yaml
	 * 		the YamlConfiguration to convert
	 *
	 * @return the resulting Map
	 */
	public static SortedMap<String, String> convertYmlToProperties(YamlConfiguration yaml) {
		SortedMap<String, String> result = new TreeMap<>();
		for(Map.Entry<String, Object> entry : flatten(new Yaml().load(yaml.saveToString())).entrySet())
			if(entry.getValue() instanceof String) result.put(entry.getKey(), (String) entry.getValue());
		return result;
	}

	/**
	 * Private constructor, no need to instantiate this class
	 */
	private YAMLConverter() {throw new UnsupportedOperationException();}
}