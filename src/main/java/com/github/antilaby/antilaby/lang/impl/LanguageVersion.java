package com.github.antilaby.antilaby.lang.impl;

import java.util.ArrayList;
import java.util.List;

public class LanguageVersion {

	private static final List<LanguageVersion> instances = new ArrayList<>();
	public static final LanguageVersion CURRENT_VERSION = new LanguageVersion("1.0.0");

	public static LanguageVersion getOrCreate(int major, int minor, int patch) {
		for (final LanguageVersion lv : instances)
			if (lv.major == major && lv.minor == minor && lv.patch == patch) return lv;
		return new LanguageVersion(major, minor, patch);
	}
	
	public static LanguageVersion getOrCreate(String name) {
		for (final LanguageVersion lv : instances)
			if ((lv.major + "." + lv.minor + "." + lv.patch).equals(name)) return lv;
		return new LanguageVersion(name);
	}

	private final int major, minor, patch;
	
	private LanguageVersion(int major, int minor, int patch) {
		if (major < 0 || minor < 0 || patch < 0 || major == 0 && minor == 0 && patch == 0)
			throw new IllegalArgumentException();
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		instances.add(this);
	}

	private LanguageVersion(String name) {
		if (name == null || name.isEmpty()) throw new IllegalArgumentException();
		final String[] prms = name.split(".", 3);
		switch (prms.length) {
			case 1:
				minor = patch = 0;
				try {
					major = Integer.parseInt(prms[0]);
				} catch (final NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				break;
			case 2:
				try {
					major = Integer.parseInt(prms[0]);
					minor = Integer.parseInt(prms[1]);
				} catch (final NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				patch = 0;
				break;
			case 3:
				try {
					major = Integer.parseInt(prms[0]);
					minor = Integer.parseInt(prms[1]);
					patch = Integer.parseInt(prms[2]);
				} catch (final NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				break;
			default:
				throw new IllegalArgumentException();
		}
		instances.add(this);
	}

	public int getMajorVersion() {
		return major;
	}

	public int getMinorVersion() {
		return minor;
	}

	public int getPatchVersion() {
		return patch;
	}
}
