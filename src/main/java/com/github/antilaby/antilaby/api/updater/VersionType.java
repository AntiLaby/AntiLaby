package com.github.antilaby.antilaby.api.updater;

/**
 * The version type of the current build
 *
 * @author NathanNr
 */
public enum VersionType {

	RELEASE("release"), PRE_RELEASE("pre"), BETA("beta"), ALPHA("alpha"), DEV("dev"), CUSTOM("custom");

	public static VersionType fromName(String name) {
		if (name == null || name.isEmpty()) return null;
		for (final VersionType type : values())
			if (name.contains(type.toString())) return type;
		return VersionType.RELEASE;
	}

	@Override
	public String toString() {
		return name;
	}

	private final String name;

	VersionType(String name) {
		this.name = name;
	}

}
