package de.heisluft.antilaby.lang;

import org.bukkit.entity.Player;

/**
 * A {@link Player}-specific Translator Interface holding an {@link ILocale}
 * implementation
 *
 * @param <T>
 *            the {@link ILocale} implementation
 * @since 3.0-Alpha20171210-Rewrite
 * @author heisluft
 */
public interface IClientLanguageManager<T extends ILocale> extends ILanguageManager<T> {

	/**
	 * Gets the {@link T ILocale} implementation for the specified {@link Player}
	 *
	 * @param player
	 *            The player to get the ILocale implementation for
	 * @return the ILocale implementation for the specified Player
	 * @author heisluft
	 */
	public T getLanguageForPlayer(Player player);

	/**
	 * Sets the {@link T ILocale} implementation for the specified {@link Player}
	 *
	 * @param player
	 *            The player to set the ILocale implementation for
	 * @param locale
	 *            An instance of the ILocale implementation for the specified Player
	 * @author heisluft
	 */
	public void setLanguageForPlayer(Player player, T locale);
	
	/**
	 * Translates the unlocalized String to the specified {@link Player}s {@link T
	 * ILocale} implementation
	 *
	 * @param unlocalized
	 *            The unlocalized String to translate
	 * @param translateTo
	 *            The Player to translate the unlocalized String to
	 * @param args
	 *            Optional arguments used to replace variables
	 * @return the correctly formatted, localized String
	 * @author heisluft
	 */
	public String translate(String unlocalized, Player translateTo, Object... args);
}
