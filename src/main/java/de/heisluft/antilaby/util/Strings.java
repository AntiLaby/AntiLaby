package de.heisluft.antilaby.util;

public final class Strings {

	public static String equalsFallback(String toCheck, String checkFor, boolean ignoreCase, String fallback) {
		boolean flag = false;
		if (ignoreCase) flag = toCheck.equalsIgnoreCase(checkFor);
		else flag = toCheck.equals(checkFor);
		return flag ? toCheck : fallback;
	}

	private Strings() {}
}
