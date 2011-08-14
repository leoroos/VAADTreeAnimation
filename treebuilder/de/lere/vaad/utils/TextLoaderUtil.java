package de.lere.vaad.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;

public class TextLoaderUtil {

	private Class<?> relLoader;
	private final String additionalRelativePath;

	public TextLoaderUtil(Class<?> relLoader, String additionalRelativePath) {
		this.relLoader = relLoader;
		this.additionalRelativePath = additionalRelativePath;
	}

	public String getText(String name) {
		File relPath;
		if (additionalRelativePath != null) {
			relPath = new File(additionalRelativePath, name);
		} else
			relPath = new File(name);

		InputStream resourceAsStream = relLoader
				.getResourceAsStream(relPath.getPath());
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(resourceAsStream, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			reader = new InputStreamReader(resourceAsStream);
		}
		String result;
		try {
			result = IOUtils.toString(reader);
		} catch (IOException e) {
			result = e.toString();
		}
		return result;
	}

}
