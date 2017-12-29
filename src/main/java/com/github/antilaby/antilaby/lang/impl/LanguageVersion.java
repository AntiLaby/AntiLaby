package com.github.antilaby.antilaby.lang.impl;

import java.util.ArrayList;
import java.util.List;

public class LanguageVersion implements Comparable<LanguageVersion> {
	
	private static final List<LanguageVersion> instances = new ArrayList<>();
	public static final LanguageVersion UNDEFINED = new LanguageVersion();
	public static final LanguageVersion CURRENT_VERSION = new LanguageVersion("1.0.0");
	
	public static void init() {}
	
	public static LanguageVersion getOrCreate(int major, int minor, int patch) {
		for(final LanguageVersion lv : instances)
			if(lv.major == major && lv.minor == minor && lv.patch == patch) return lv;
		return new LanguageVersion(major, minor, patch);
	}
	
	public static LanguageVersion getOrCreate(String name) {
		for(final LanguageVersion lv : instances)
			if(lv.toString().equals(name)) return lv;
		return new LanguageVersion(name);
	}
	
	@Override
	public String toString() {
		return major + "." + minor + '.' + patch;
	}
	
	private final int major, minor, patch;
	
	private LanguageVersion() {
		major = minor = patch = 0;
	}
	
	private LanguageVersion(int major, int minor, int patch) {
		if(major < 0 || minor < 0 || patch < 0 || major == 0 && minor == 0 && patch == 0)
			throw new IllegalArgumentException();
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		instances.add(this);
	}
	
	private LanguageVersion(String name) {
		if(name == null || name.isEmpty()) throw new IllegalArgumentException();
		final String[] prms = name.split("\\.", 3);
		switch(prms.length) {
			case 1:
				minor = patch = 0;
				try {
					major = Integer.parseInt(prms[0]);
				} catch(final NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				break;
			case 2:
				try {
					major = Integer.parseInt(prms[0]);
					minor = Integer.parseInt(prms[1]);
				} catch(final NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				patch = 0;
				break;
			case 3:
				try {
					major = Integer.parseInt(prms[0]);
					minor = Integer.parseInt(prms[1]);
					patch = Integer.parseInt(prms[2]);
				} catch(final NumberFormatException e) {
					throw new IllegalArgumentException();
				}
				break;
			default:
				throw new IllegalArgumentException();
		}
		instances.add(this);
	}
	
	@Override
	public int compareTo(LanguageVersion other) {
		if(other == null) throw new NullPointerException();
		if(other.major > major) return -1;
		if(major > other.major) return 1;
		if(other.minor > minor) return -1;
		if(minor > other.minor) return 1;
		if(other.patch > patch) return -1;
		if(patch > other.patch) return 1;
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !(obj instanceof LanguageVersion)) return false;
		final LanguageVersion v = (LanguageVersion) obj;
		return v.major == major && v.minor == minor && v.patch == patch;
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
