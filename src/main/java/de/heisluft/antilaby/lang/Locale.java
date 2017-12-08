package de.heisluft.antilaby.lang;

import java.util.HashMap;
import java.util.Map;

public enum Locale {
	DE_DE("de_de", "Fehler bei nichtexistenter Fehlerübersetzung"), EN_US("en_us",
			"Error with nonexisting translation");

	public static Locale byName(String name, Locale fallback) {
		for (final Locale language : values())
			if (language.getName().equals(name)) return language;
		return fallback;
	}
	
	private final String fallbackErrorMessage;
	private boolean init = false;
	private final String name;
	
	private final Map<String, String> translation = new HashMap<>();
	
	private Locale(String name, String le) {
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
