package com.github.antilaby.antilaby.util;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Does all Converting Magic.
 */
public final class YamlConverter {

  /**
   * Flats the given Map by joining keys
   *
   * @param source
   *     the nested Map
   *
   * @return the flattened Map
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Nonnull
  private static SortedMap<String, Object> flatten(Map<String, Object> source) {
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
   * Converts the given Yaml String-only Configuration to Properties.<p>Usage:</p> <p><code> for(Map.Entry&lt;String,
   * String&gt; e : convertToLocale (yaml).entrySet())<br> yourWritingMethod(e.getKey() + '=' +
   * e.getValue());</code></p><i>Warning: Non-String entries (like Collections) will be lost</i>
   *
   * @param yaml
   *     the YAML file path to convert
   *
   * @return the resulting Map
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  public static SortedMap<String, String> convertYmlToProperties(Path yaml) {
    SortedMap<String, String> result = new TreeMap<>();
    try {
      byte[] bytes = Files.readAllBytes(yaml);
      Object parsed = new Yaml().load(new String(bytes));
      if(!(parsed instanceof Map)) throw new YAMLException("Invalid YAML File: " + yaml.getFileName());
      for(Map.Entry<String, Object> entry : flatten((Map<String, Object>) parsed).entrySet())
        if(entry.getValue() instanceof String) result.put(entry.getKey(), (String) entry.getValue());
    } catch(IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Private constructor, no need to instantiate this class
   */
  private YamlConverter() {throw new UnsupportedOperationException();}
}