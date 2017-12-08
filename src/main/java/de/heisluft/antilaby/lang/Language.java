package de.heisluft.antilaby.lang;

import java.util.HashMap;
import java.util.Map;

public enum Language {
	DE_DE("de_de", "Fehler bei nichtexistenter Fehlerübersetzung"), EN_US("en_us",
			"Error with nonexisting translation");
	
	public static Language byName(String name, Language fallback) {
		for (final Language language : values())
			if (language.getName().equals(name)) return language;
		return fallback;
	}

	private final String fallbackErrorMessage;
	private boolean init = false;
	private final String name;

	private final Map<String, String> translation = new HashMap<>();

	private Language(String name, String le) {
		this.name = name;
		fallbackErrorMessage = le;
	}

	public String getName() {
		return name;
	}

	private void init() {
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
