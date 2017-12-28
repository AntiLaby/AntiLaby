package com.github.antilaby.antilaby.lang.impl;

import com.github.antilaby.antilaby.lang.ILocale;
import com.github.antilaby.antilaby.main.AntiLaby;
import com.github.antilaby.antilaby.util.IOUtils;
import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class Locale implements ILocale {
	
	public static final Locale UNDEFINED = null;
	private static final List<Locale> values = new ArrayList<>();
	private static final String DEFAULT_FALLBACK_MESSAGE = "Unknown Error";
	public static final Locale EN_US = new Locale("en_us", DEFAULT_FALLBACK_MESSAGE);
	public static final Locale DE_DE = new Locale("de_de", DEFAULT_FALLBACK_MESSAGE);
	public static final Locale FR_FR = new Locale("fr_fr", DEFAULT_FALLBACK_MESSAGE);
	
	public static Locale byName(String name, Locale fallback) {
		for(final Locale language : values)
			if(language.name.equals(name)) return language;
		return fallback;
	}
	
	public static Locale getOrCreate(String name) {
		return getOrCreate(name, DEFAULT_FALLBACK_MESSAGE);
	}
	
	public static Locale getOrCreate(String name, String fallbackMessage) {
		if(!isLangNameValid(name)) return UNDEFINED;
		fallbackMessage = fallbackMessage != null && !fallbackMessage.isEmpty() ? fallbackMessage
				                  : DEFAULT_FALLBACK_MESSAGE;
		Locale l = byName(name, UNDEFINED);
		if(values.contains(l)) return l;
		l = new Locale(name, fallbackMessage);
		return l;
	}
	
	public static boolean isLangNameValid(String name) {
		final String[] nameParts = name.split("_");
		return nameParts.length == 2 && nameParts[0].length() == 2 && nameParts[1].length() == 2;
	}
	
	public static List<Locale> values() {
		return values;
	}
	
	private final String fallbackErrorMessage;
	private final String name;
	private final Map<String, String> translation = new HashMap<>();
	private boolean init = false;
	
	private Locale(String name, String le) {
		this.name = name;
		fallbackErrorMessage = le;
		values.add(this);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public Locale init(boolean overwrite) {
		if(init) return this;
		try {
			final File f = new File(LanguageManager.RESOURCE_PATH + File.separator + name + ".lang");
			if(!f.exists()) f.createNewFile();
			final LanguageVersion v = LangFileParser.getVersion(f);
			if(LanguageVersion.CURRENT_VERSION.compareTo(v) == 1)
				LanguageManager.LOG.info("Updating language resource " + name + ".lang from version " + v
						                         + " to version " + LanguageVersion.CURRENT_VERSION);
			if(!overwrite || LanguageVersion.CURRENT_VERSION.compareTo(v) == 1) {
				final JarFile file = new JarFile(AntiLaby.getInstance().getFile());
				final InputStream is = file.getInputStream(file.getJarEntry(name + ".lang"));
				IOUtils.copyStream(is, new FileOutputStream(f));
				file.close();
			}
			translation.putAll(LangFileParser.parse(f));
		} catch(final IOException e) {
			e.printStackTrace();
		}
		init = true;
		return this;
	}
	
	@Override
	public String translate(String toTranslate, Object... args) {
		if(!init) init(false);
		return translation.containsKey(toTranslate)
				       ? ChatColor.translateAlternateColorCodes('&', String.format(translation.get(toTranslate), args))
				       : translation.getOrDefault("translation.error", fallbackErrorMessage);
	}
}
