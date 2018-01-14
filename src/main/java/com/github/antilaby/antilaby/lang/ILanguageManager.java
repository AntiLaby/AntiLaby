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
	 * Initializes the LanguageManager. In this method you should initialize all your {@link Locale Locales}
	 */
	void init();

	/**
	 * This method should return true if either this instance is already initialized or this implementation does not
	 * need initialization
	 *
	 * @return True, if this instance is initialized
	 */
	boolean isInit();

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
