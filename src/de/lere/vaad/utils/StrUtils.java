package de.lere.vaad.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StrUtils {
	
	public static List<String> toLines(String aString) {
		Scanner scanner = new Scanner(aString);
		ArrayList<String> result = new ArrayList<String>();
		while(scanner.hasNextLine()){
			result.add(scanner.nextLine());
		}
		return result;
	}
	
	public static String getLastLine(String aStr) {
		return getNthLastLine(aStr, 1);
	}
	
	/**
	 * one based last line, i.e. #getNthLastLine(string,1) is equivalent to #getLastLine
	 * 
	 * @param aStr
	 * @param n
	 * @return
	 * 
	 */
	public static String getNthLastLine(String aStr, int n) {
		if(n < 1){
			throw new IllegalArgumentException("Expects n to be at least 1 was " + n);
		}
		
		List<String> lines = de.lere.vaad.utils.StrUtils.toLines(aStr);		
		if(lines.size() < n)
			return null;
		String lastLine = lines.get(lines.size() - n);
		return lastLine;
	}

}
