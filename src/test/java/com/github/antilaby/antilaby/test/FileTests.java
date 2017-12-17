package com.github.antilaby.antilaby.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.antilaby.antilaby.lang.impl.LangFileParser;
import com.github.antilaby.antilaby.lang.impl.LanguageVersion;

public class FileTests {
	
	@Test
	public void testVersionHeader() {
		assertEquals(1, LangFileParser.getVersion("File version: 20.20.3").compareTo(LanguageVersion.CURRENT_VERSION));
		assertEquals(0, LangFileParser.getVersion("0.0.0").compareTo(LanguageVersion.UNDEFINED));
	}
}
