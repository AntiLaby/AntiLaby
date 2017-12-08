package de.heisluft.antilaby.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LangFileParser {

	public static Map<String, String> parse(File f) {
		try {
			return parse(new FileInputStream(f), f.getName());
		} catch (final FileNotFoundException e) {
			return null;
		}
	}
	
	public static Map<String, String> parse(InputStream resource) {
		return parse(resource, "unknown");
	}
	
	public static Map<String, String> parse(InputStream resource, String nameToUse) {
		System.out.println("Loading lang resource " + nameToUse);
		final Map<String, String> result = new HashMap<>();
		if (resource == null) return result;
		try {
			final BufferedReader r = new BufferedReader(new InputStreamReader(resource));
			while (r.ready()) {
				final String ln = r.readLine().trim();
				if (!ln.startsWith("#")) if (!ln.contains("="))
					System.out.println("Could not parse line in resource" + nameToUse + ": " + ln);
				else {
					final String[] s = ln.split("=", 2);
					result.put(s[0].trim().replace(" ", ""), s[1].trim());
				}
			}
			r.close();
		} catch (final IOException e) {
			System.out.println("Could not parse lang resource: " + nameToUse);
		}
		return result;
	}
	
	private final Locale l;
	
	public LangFileParser(Locale l) {
		this.l = l;
	}

	public Map<String, String> parse() {
		return parse(LangFileParser.class.getResourceAsStream(l.getName() + ".lang"), l.getName() + ".lang");
	}

}
