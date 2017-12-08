package de.heisluft.antilaby.lang;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class LanguageManager {
	
	public static final LanguageManager INSTANCE = new LanguageManager();

	public static void initAll() {
		for (final Locale l : Locale.values())
			l.init();
	}
	
	public static String translate(String unlocalized, Locale language, Object... args) {
		return language.translate(unlocalized, args);
	}
	
	public static String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, translatedTo.getLocale(), args);
	}

	public static String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Locale.byName(language, Locale.EN_US));
	}

	private final Map<Player, Locale> mappedLanguages = new HashMap<>();

	private LanguageManager() {}

	public Locale getLanguageForPlayer(Player p) {
		return mappedLanguages.get(p);
	}
	
	public void setLanguageForPlayer(Player p, Locale l) {
		mappedLanguages.put(p, l);
	}
}
