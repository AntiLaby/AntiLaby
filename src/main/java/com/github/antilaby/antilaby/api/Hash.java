package com.github.antilaby.antilaby.api;

import java.security.MessageDigest;

public class Hash {
	
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
			return String.format("%064x", new java.math.BigInteger(1, digest));
		} catch(Exception e) {
		}
		return null;
	}
	
}
