package com.github.heisluft.antilaby.lang;

/**
 * An Interface defining a Locale
 *
 * @since 2.0
 * @author heisluft
 */
public interface ILocale {
	
	/**
	 * Gets the name of this {@link ILocale}
	 *
	 * @return the name of this ILocale
	 * @author heisluft
	 */
	public abstract String getName();
	
	/**
	 * Translates the unlocalized sString to this {@link ILocale}
	 *
	 * @param unlocalized
	 *            The unlocalized String to translate
	 * @param args
	 *            Optional arguments used to replace variables
	 * @return The correctly formatted, localized String
	 * @author heisluft
	 */
	public abstract String translate(String unlocalized, Object... args);
}
