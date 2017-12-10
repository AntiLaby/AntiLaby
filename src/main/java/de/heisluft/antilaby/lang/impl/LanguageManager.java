package de.heisluft.antilaby.lang.impl;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import de.heisluft.antilaby.lang.IClientLanguageManager;
import de.heisluft.antilaby.log.Logger;

public class LanguageManager implements IClientLanguageManager<Locale> {

	public static final LanguageManager INSTANCE = new LanguageManager();

	static final Logger LOG = new Logger("Localization");
	private static boolean isInit = false;

	public static void initAll() {
		for (final Locale l : Locale.getAll())
			l.init();
		isInit = true;
	}
	
	public static boolean isInit() {
		return isInit;
	}

	private final Map<Player, Locale> mappedLanguages = new HashMap<>();

	private LanguageManager() {}
	
	@Override
	public Locale getLanguageForPlayer(Player p) {
		return mappedLanguages.get(p);
	}

	@Override
	public void setLanguageForPlayer(Player p, Locale l) {
		
		mappedLanguages.put(p, l);
	}
	
	@Override
	public String translate(String unlocalized, Locale language, Object... args) {
		return language.translate(unlocalized, args);
	}
	
	@Override
	public String translate(String unlocalized, Player translatedTo, Object... args) {
		return translate(unlocalized, translatedTo.getLocale(), args);
	}

	@Override
	public String translate(String unlocalized, String language, Object... args) {
		return translate(unlocalized, Locale.byName(language, Locale.EN_US), args);
	}
}
