package de.heisluft.antilaby.lang.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import de.heisluft.antilaby.lang.ILocale;

public class Locale implements ILocale {
	
	public static final Locale UNDEFINED = null;
	private static final List<Locale> values = new ArrayList<>();
	private static final String DEFAULT_FALLBACK_MESSAGE = "Unknown Error";
	public static final Locale EN_US = new Locale("en_us", DEFAULT_FALLBACK_MESSAGE);
	public static final Locale DE_DE = new Locale("de_de", DEFAULT_FALLBACK_MESSAGE);

	public static Locale byName(String name, Locale fallback) {
		for (final Locale language : values)
			if (language.getName().equals(name)) return language;
		return fallback;
	}
	
	public static List<Locale> getAll() {
		return values;
	}

	public static Locale getOrCreate(String name) {
		Locale l = byName(name, UNDEFINED);
		if (values.contains(l)) return l;
		l = new Locale(name, DEFAULT_FALLBACK_MESSAGE);
		return l;
	}
	
	public static Locale getOrCreate(String name, String fallback) {
		Locale l = byName(name, UNDEFINED);
		if (values.contains(l)) return l;
		l = new Locale(name, fallback);
		return l;
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

	public Locale init() {
		if (!init) translation.putAll(new LangFileParser(this).parse());
		init = true;
		return this;
	}

	@Override
	public String translate(String toTranslate, Object... args) {
		if (!init) init();
		return translation.containsKey(toTranslate)
				? ChatColor.translateAlternateColorCodes('&', String.format(translation.get(toTranslate), args))
				: translation.containsKey("translation.error") ? translation.get("translation.error")
						: fallbackErrorMessage;
	}
}
