package com.github.antilaby.antilaby.lang;

import com.github.antilaby.antilaby.lang.impl.Locale;

/**
 * A Translator Interface
 *
 * @author heisluft
 * @since 2.0
 */
public interface ILanguageManager {

	/**
	 * Translates the unlocalized String to the specified {@link Locale} name
	 *
	 * @param unlocalized
	 * 		The unlocalized String to translate
	 * @param language
	 * 		The name of the Locale
	 * @param args
	 * 		Optional arguments used to replace variables
	 *
	 * @return the correctly formatted, localized String
	 */
	String translate(String unlocalized, String language, Object... args);

	/**
	 * Translates the unlocalized String to the specified {@link Locale}
	 *
	 * @param unlocalized
	 * 		The unlocalized String to translate
	 * @param language
	 * 		The Locale to translate the unlocalized String to
	 * @param args
	 * 		Optional arguments used to replace variables
	 *
	 * @return the correctly formatted, localized String
	 */
	String translate(String unlocalized, Locale language, Object... args);
}
