package com.github.antilaby.antilaby.lang;

/**
 * An Interface defining a Locale
 *
 * @author heisluft
 * @since 2.0
 */
public interface ILocale {
	
	/**
	 * Gets the name of this {@link ILocale}
	 *
	 * @return the name of this ILocale
	 *
	 * @author heisluft
	 */
	String getName();
	
	/**
	 * Translates the unlocalized sString to this {@link ILocale}
	 *
	 * @param unlocalized
	 * 		The unlocalized String to translate
	 * @param args
	 * 		Optional arguments used to replace variables
	 *
	 * @return The correctly formatted, localized String
	 *
	 * @author heisluft
	 */
	String translate(String unlocalized, Object... args);
}
