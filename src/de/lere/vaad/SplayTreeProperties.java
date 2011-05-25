package de.lere.vaad;

/**
 * Container class that contains the configuration for the SplayTree. <br>
 * The properties all have a default value set and should if at all only be
 * changed during initialization.
 * 
 * @author leo
 * 
 */
public class SplayTreeProperties {

	int sourceCodeSize = 12;

	int textFontSize = 12;
	
	
	/**
	 * The vertical height of the text including a vertical gap to the next line of text  
	 */
	int textVerticalHeight = (int) (1.5 * textFontSize);

	/**
	 * The vertical height of the source including a vertical gap to the next line of source.
	 * (factor is merely guessed) and can not be used to adjust the gap in a {@code SourceCode} object.  
	 */
	int sourceVerticalHeight = (int) (1.5 * textFontSize);
}
