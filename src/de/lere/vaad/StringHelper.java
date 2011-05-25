package de.lere.vaad;

import java.util.Arrays;
import java.util.List;

public class StringHelper {
	private static final String delim = System.getProperty("line.separator");
	
	public static List<String> getStringToLinesAtDelimiter(String text) {
		return Arrays.asList(text.split(delim));
	}
}
