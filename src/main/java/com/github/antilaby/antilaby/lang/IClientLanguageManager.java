package com.github.antilaby.antilaby.lang;

import com.github.antilaby.antilaby.lang.impl.Locale;
import org.bukkit.entity.Player;

/**
 * A {@link Player}-specific Translator Interface
 *
 * @author heisluft
 * @since 2.0
 */
public interface IClientLanguageManager extends ILanguageManager {

	/**
	 * Gets the {@link Locale} for the specified {@link Player}
	 *
	 * @param player
	 * 		The player to get the ILocale implementation for
	 *
	 * @return the ILocale implementation for the specified Player
	 */
	Locale getLanguageForPlayer(Player player);

	/**
	 * Sets the {@link Locale} for the specified {@link Player}
	 *
	 * @param player
	 * 		The player to set the ILocale implementation for
	 * @param locale
	 * 		The name of the ILocale instance
	 */
	void setLanguageForPlayer(Player player, String locale);

	/**
	 * Sets the {@link Locale} for the specified {@link Player}
	 *
	 * @param player
	 * 		The player to set the Locale for
	 * @param locale
	 * 		The Locale for the specified Player
	 */
	void setLanguageForPlayer(Player player, Locale locale);

	/**
	 * Translates the unlocalized String to the specified {@link Player}s {@link Locale}
	 *
	 * @param unlocalized
	 * 		The unlocalized String to translate
	 * @param translateTo
	 * 		The Player to translate the unlocalized String to
	 * @param args
	 * 		Optional arguments used to replace variables
	 *
	 * @return the correctly formatted, localized String
	 */
	String translate(String unlocalized, Player translateTo, Object... args);
}
