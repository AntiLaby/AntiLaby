package com.github.antilaby.antilaby.api;

/**
 * Provides static method for boolean to int conversion and vice-versa
 *
 * @author heisluft
 */
public final class BooleanIntConversion {

	/**
	 * Converts boolean to int
	 *
	 * @param toConvert
	 *            The boolean to convert
	 * @return 1 if {@code toConvert == true}
	 * @author NathanNr
	 */
	public static int convert(boolean toConvert) {
		return toConvert ? 1 : 0;
	}
	
	/**
	 * Converts an int to a boolean
	 *
	 * @param toConvert
	 *            The integer to convert
	 * @return a boolean value based on {@code toConvert != 0}
	 * @author heisluft
	 */
	public static boolean convert(int toConvert) {
		return toConvert != 0;
	}

}
