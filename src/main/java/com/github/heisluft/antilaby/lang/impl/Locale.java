package com.github.heisluft.antilaby.lang.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.github.heisluft.antilaby.lang.ILocale;
import com.github.heisluft.antilaby.util.IOUtils;

public class Locale implements ILocale {
	
	private static final List<Locale> values = new ArrayList<>();
	private static final String DEFAULT_FALLBACK_MESSAGE = "Unknown Error";
	public static final Locale EN_US = new Locale("en_us", DEFAULT_FALLBACK_MESSAGE);
	public static final Locale DE_DE = new Locale("de_de", DEFAULT_FALLBACK_MESSAGE);
	public static final Locale FR_FR = new Locale("fr_fr", DEFAULT_FALLBACK_MESSAGE);
	public static final Locale UNDEFINED = null;

	public static Locale byName(String name, Locale fallback) {
		for (final Locale language : values)
			if (language.getName().equals(name)) return language;
		return fallback;
	}
	
	public static Locale getOrCreate(String name) {
		return getOrCreate(name, DEFAULT_FALLBACK_MESSAGE);
	}

	public static Locale getOrCreate(String name, String fallbackMessage) {
		if (!isLangNameValid(name)) return UNDEFINED;
		fallbackMessage = fallbackMessage != null && !fallbackMessage.isEmpty() ? fallbackMessage
				: DEFAULT_FALLBACK_MESSAGE;
		Locale l = byName(name, UNDEFINED);
		if (values.contains(l)) return l;
		l = new Locale(name, fallbackMessage);
		return l;
	}
	
	public static boolean isLangNameValid(String name) {
		final String[] nameParts = name.split("_");
		return nameParts.length == 2 && nameParts[0].length() == 2 && nameParts[1].length() == 2;
	}

	public static List<Locale> values() {
		return values;
	}

	private final String fallbackErrorMessage;
	private boolean init = false;
	private final String name;

	private final Map<String, String> translation = new HashMap<>();

	private Locale(String name, String le) {
		this.name = name;
		fallbackErrorMessage = le;
		values.add(this);
	}
	
	@Override
	public String getName() {
		return name;
	}

	public Locale init(boolean overwrite) {
		if (init) return this;
		try {
			final File f = new File(LanguageManager.RESOURCE_PATH + File.separator + name + ".lang");
			if (!f.exists() || overwrite) {
				f.createNewFile();
				final InputStream is = getClass().getClassLoader().getResourceAsStream(name + ".lang");
				IOUtils.copyStream(is, new FileOutputStream(f));
			}
			translation.putAll(LangFileParser.parse(f));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		init = true;
		return this;
	}
	
	@Override
	public String translate(String toTranslate, Object... args) {
		if (!init) init(false);
		return translation.containsKey(toTranslate)
				? ChatColor.translateAlternateColorCodes('&', String.format(translation.get(toTranslate), args))
				: translation.containsKey("translation.error") ? translation.get("translation.error")
						: fallbackErrorMessage;
	}
}
