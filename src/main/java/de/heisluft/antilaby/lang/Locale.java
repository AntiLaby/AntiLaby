package de.heisluft.antilaby.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Locale {
	
	public static final Locale UNDEFINED = null;
	public static final Locale DE_DE = new Locale("de_de", "Fehler bei nichtexistenter Fehlerübersetzung");
	public static final Locale EN_US = new Locale("en_us", "Error with nonexisting translation");
	private static final List<Locale> values = new ArrayList<>();
	
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
		l = new Locale(name, EN_US.fallbackErrorMessage);
		return l;
	}
	
	private final String fallbackErrorMessage;
	private boolean init = false;
	private final String name;

	private final Map<String, String> translation = new HashMap<>();

	private Locale(String name, String le) {
		values.add(this);
		this.name = name;
		fallbackErrorMessage = le;
	}

	public String getName() {
		return name;
	}

	void init() {
		if (!init) translation.putAll(new LangFileParser(this).parse());
		init = true;
	}

	public String translate(String toTranslate, Object... args) {
		if (!init) init();
		return translation.containsKey(toTranslate) ? translation.get(toTranslate)
				: translation.containsKey("translation.error") ? translation.get("translation.error")
						: fallbackErrorMessage;
	}
}
