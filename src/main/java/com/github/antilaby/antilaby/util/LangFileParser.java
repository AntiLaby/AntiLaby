package com.github.antilaby.antilaby.util;

import com.github.antilaby.antilaby.lang.impl.LanguageVersion;
import com.github.antilaby.antilaby.log.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class LangFileParser {

	private static final Logger LOG = new Logger("Localization");
	private static final Pattern VERSION_PATTERN = Pattern.compile("^#\\s?version:\\s?\\d*\\.?\\d*\\.?\\d*$");
	private static final Pattern REPLACE_PATTERN = Pattern.compile("^.*version:\\s?");

	public static LanguageVersion getVersion(File lFile) {
		if(!lFile.exists()) throw new NullPointerException("File does not exist");
		BufferedReader r = null;
		try {
			return getVersion((r = new BufferedReader(new FileReader(lFile))).readLine());
		} catch(final Exception e) {
			// ignore silently
		} finally {
			IOUtils.closeSilently(r);
		}
		return LanguageVersion.UNDEFINED;
	}

	public static LanguageVersion getVersion(String headerFrom) {
		if(headerFrom.isEmpty()) return LanguageVersion.UNDEFINED;
		if(VERSION_PATTERN.matcher(headerFrom).matches())
			return LanguageVersion.getOrCreate(REPLACE_PATTERN.matcher(headerFrom).replaceAll(""));
		return LanguageVersion.UNDEFINED;

	}

	public static Map<String, String> parse(File f) {
		try {
			return parse(new FileInputStream(f), f.getName());
		} catch(final FileNotFoundException e) {
			return null;
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
					result.put(s[0].trim().replace(" ", ""), s[1].trim());
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
