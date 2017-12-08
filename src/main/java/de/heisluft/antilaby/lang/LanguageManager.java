package de.heisluft.antilaby.lang;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class LanguageManager {

	public static final LanguageManager INSTANCE = new LanguageManager();
	
	public static String translate(String unlocalized, Language language, Object... args) {
		return language.translate(unlocalized, args);
	}
	
	public static String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, translatedTo.getLocale(), args);
	}
	
	public static String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Language.byName(language, Language.EN_US));
	}
	
	private final Map<Player, Language> mappedLanguages = new HashMap<>();
	
	private LanguageManager() {}
	
	public Language getLanguageForPlayer(Player p) {
		return mappedLanguages.get(p);
	}

	public void setLanguageForPlayer(Player p, Language l) {
		mappedLanguages.put(p, l);
	}
}
