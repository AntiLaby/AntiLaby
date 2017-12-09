package de.heisluft.antilaby.lang;

public interface ILanguageManager {
	
	public String translate(String unlocalized, Locale language, Object... args);

	public String translate(String unlocalized, String language, Object... args);
}
