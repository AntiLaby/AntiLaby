package com.github.nathannr.antilaby.api;

public final class BooleanIntCoversion {
	
	/**
	 * Converts boolean to int
	 *
	 * @param b
	 *            the boolean to convert
	 * @return int
	 * @author NathanNr
	 */
	public static int convert(boolean b) {
		return b ? 1 : 0;
	}

	/**
	 * Converts an int to a boolean
	 *
	 * @author heisluft
	 * @param i
	 *            the integer to convert
	 * @return a boolean value
	 */
	public static boolean convert(int i) {
		return i != 0;
	}
	
}
