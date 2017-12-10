package de.heisluft.antilaby.lang;

/**
 * A Translator Interface holding an {@link ILocale} implementation
 *
 * @param <T>
 *            The {@link ILocale} implementation
 * @since 3.0-Alpha20171210-Rewrite
 * @author heisluft
 */
public interface ILanguageManager<T extends ILocale> {
	
	/**
	 * Translates the unlocalized String to the specified {@link T ILocale}
	 * implementation name
	 *
	 * @param unlocalized
	 *            The unlocalized String to translate
	 * @param language
	 *            The name of the {@link ILocale} implementation
	 * @param args
	 *            Optional arguments used to replace variables
	 * @return the correctly formatted, localized String
	 * @author heisluft
	 */
	public String translate(String unlocalized, String language, Object... args);

	/**
	 * Translates the unlocalized String to the specified {@link ILocale}
	 * implementation
	 *
	 * @param unlocalized
	 *            The unlocalized String to translate
	 * @param language
	 *            The {@link ILocale} implementation to translate the unlocalized
	 *            String to
	 * @param args
	 *            Optional arguments used to replace variables
	 * @return the correctly formatted, localized String
	 * @author heisluft
	 */
	public String translate(String unlocalized, T language, Object... args);
}
