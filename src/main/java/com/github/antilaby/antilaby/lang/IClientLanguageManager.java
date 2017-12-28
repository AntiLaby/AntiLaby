package com.github.antilaby.antilaby.lang;

import org.bukkit.entity.Player;

/**
 * A {@link Player}-specific Translator Interface holding an {@link ILocale} implementation
 *
 * @param <T>
 * 		The {@link ILocale} implementation
 *
 * @author heisluft
 * @since 2.0
 */
public interface IClientLanguageManager<T extends ILocale> extends ILanguageManager<T> {
	
	/**
	 * Gets the {@link T ILocale} implementation for the specified {@link Player}
	 *
	 * @param player
	 * 		The player to get the ILocale implementation for
	 *
	 * @return the ILocale implementation for the specified Player
	 *
	 * @author heisluft
	 */
	T getLanguageForPlayer(Player player);
	
	/**
	 * Sets the {@link T ILocale} implementation for the specified {@link Player}
	 *
	 * @param player
	 * 		The player to set the ILocale implementation for
	 * @param locale
	 * 		The name of the ILocale instance
	 *
	 * @author heisluft
	 */
	void setLanguageForPlayer(Player player, String locale);
	
	/**
	 * Sets the {@link T ILocale} implementation for the specified {@link Player}
	 *
	 * @param player
	 * 		The player to set the ILocale implementation for
	 * @param locale
	 * 		An instance of the ILocale implementation for the specified Player
	 *
	 * @author heisluft
	 */
	void setLanguageForPlayer(Player player, T locale);
	
	/**
	 * Translates the unlocalized String to the specified {@link Player}s {@link T ILocale} implementation
	 *
	 * @param unlocalized
	 * 		The unlocalized String to translate
	 * @param translateTo
	 * 		The Player to translate the unlocalized String to
	 * @param args
	 * 		Optional arguments used to replace variables
	 *
	 * @return the correctly formatted, localized String
	 *
	 * @author heisluft
	 */
	String translate(String unlocalized, Player translateTo, Object... args);
}
