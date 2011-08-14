package de.lere.vaad.animation.splaytree.resources.descriptions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * Wraps descriptive texts used in animation.<br>
 * The actual enums can be used as store for the texts but have to be initialized once using {@link #init()}.
 *  
 * 
 * @author Leo Roos, Rene Hertling
 *
 */
public enum SplayTreeResourceAccessor {
	
	DESCRIPTION("description"),
	INTRO("intro"),
	BEHAVIOUR("operationBehaviour"),
	ZIG_STEP("zigStep"),
	ZIGZAG_STEP("zigzagStep"),
	ZIGZIG_STEP("zigzigStep");
	
	SplayTreeResourceAccessor(String name){
		this.name = name;
	}
	
	private String name;
	private List<String> text;
	
	InputStreamReader getResource(){
		InputStream resourceAsStream = this.getClass().getResourceAsStream(name);
		try {
			return new InputStreamReader(resourceAsStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return new InputStreamReader(resourceAsStream);
		}
	}
	
	private void init(){
		try {
			text = getTextFromResource(this);
		} catch (IOException e) {
			throw new IllegalStateException("Expected Resource " + this + " not found");
		}
	}
	
	public List<String> getText() {
		if(!isInitialized())
			init();
		return text;
	}
	
	private boolean isInitialized(){
		return this.text != null;
	}
	
	private List<String> getTextFromResource(SplayTreeResourceAccessor resource) throws IOException {
		InputStreamReader file = resource.getResource();
		List<String> readLines = IOUtils.readLines(file);
		return readLines;
	}
}
