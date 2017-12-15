package com.github.nathannr.antilaby.update;

public enum VersionType {
	
	RELEASE("release"), PRE_RELEASE("pre"), BETA("beta"), ALPHA("alpha"), DEV("dev"), CUSTOM("custom");

	public static VersionType fromName(String name) {
		if (name == null || name.isEmpty()) return null;
		for (final VersionType type : values())
			if (name.contains(type.toString())) return type;
		return VersionType.RELEASE;
	}
	
	private final String name;

	private VersionType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
