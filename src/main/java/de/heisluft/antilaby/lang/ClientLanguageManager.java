package de.heisluft.antilaby.lang;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class ClientLanguageManager implements IClientLanguageManager{
	
	public static final ClientLanguageManager INSTANCE = new ClientLanguageManager();

	public static void initAll() {
		for (final Locale l : Locale.getAll())
			l.init();
	}
	
	private final Map<Player, Locale> mappedLanguages = new HashMap<>();
	
	private ClientLanguageManager() {}

	public Locale getLanguageForPlayer(Player p) {
		return mappedLanguages.get(p);
	}

	public void setLanguageForPlayer(Player p, Locale l) {
		mappedLanguages.put(p, l);
	}

	public String translate(String unlocalized, Locale language, Object... args) {
		return language.translate(unlocalized, args);
	}

	public String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, translatedTo.getLocale(), args);
	}
	
	public String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Locale.byName(language, Locale.EN_US));
	}
}
