package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public final class LangFileParser {

	private static final Logger LOG = new Logger("Localization");

	public static Map<String, String> parse(File f) {
		try {
			return parse(new FileInputStream(f), f.getName());
		} catch(final FileNotFoundException e) {
			return new HashMap<>();
		}
	}

	public static Map<String, String> parse(InputStream resource, String nameToUse) {
		LOG.debug("Trying to load resource " + nameToUse);
		final Map<String, String> result = new HashMap<>();
		int line = 0;
		try {
			final BufferedReader r = new BufferedReader(new InputStreamReader(resource, Charset.forName("UTF-8")));
			while(r.ready()) {
				line++;
				String ln = r.readLine();
				ln = ln.trim();
				if(!ln.startsWith("#")) if(!ln.contains("="))
					LOG.warn("Could not parse line " + line + " in resource " + nameToUse + ": " + ln);
				else {
					final String[] s = ln.split("=", 2);
					String value = s[1].replaceFirst("#.*", "").trim();
					result.put(s[0].trim().replace(" ", ""), value);
				}
			}
			r.close();
		} catch(final Exception e) {
			LOG.error("Could not parse resource " + nameToUse + " (failed at line " + line + "): " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private LangFileParser() {throw new UnsupportedOperationException();}
}
