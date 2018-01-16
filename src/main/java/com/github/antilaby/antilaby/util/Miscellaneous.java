package com.github.antilaby.antilaby.util;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Provides all the other util methods
 *
 * @author heisluft
 */
public final class Miscellaneous {

	/**
	 * Get a string as SHA-256 hash
	 *
	 * @param s
	 * 		The string
	 *
	 * @return The string as hash
	 */
	public static String getAsSHA256(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(s.getBytes("UTF-8"));
			byte[] digest = md.digest();
			return String.format("%064x", new BigInteger(1, digest));
		} catch(Exception e) {
			// ignore
		}
		return null;
	}

	/**
	 * Converts boolean to int
	 *
	 * @param toConvert
	 * 		The boolean to convert
	 *
	 * @return 1 if {@code toConvert == true}
	 */
	public static int boolToInt(boolean toConvert) {
		return toConvert ? 1 : 0;
	}

	private Miscellaneous() {throw new UnsupportedOperationException();}
}
