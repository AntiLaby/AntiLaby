package com.github.antilaby.antilaby.util;

/**
 * Provides all the other util methods
 *
 * @author heisluft
 */
public final class Miscellaneous {

	/**
	 * Converts boolean to int
	 *
	 * @param toConvert
	 * 		The boolean to convert
	 *
	 * @return 1 if {@code toConvert == true}
	 */
	public static int boolToInt(boolean toConvert) {return toConvert ? 1 : 0;}

	private Miscellaneous() {throw new UnsupportedOperationException();}
}