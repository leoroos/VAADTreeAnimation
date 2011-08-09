package de.lere.vaad;

import java.awt.Point;


/**
 * Container class that contains the configuration for the SplayTree. <br>
 * The properties all have a default value set and should if at all only be
 * changed during initialization.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 */
public class TreeAnimationProperties {

	public int sourceCodeSize = 12;

	public int textFontSize = 12;
	
	public int verticalTextGap = 5;
	
	/**
	 * The vertical height of the text including a vertical gap to the next line of text  
	 */
	public int textVerticalHeight = (int) (1.5 * textFontSize);

	/**
	 * The vertical height of the source including a vertical gap to the next line of source.
	 * (factor is merely guessed) and can not be used to adjust the gap in a {@code SourceCode} object.  
	 */
	public int sourceVerticalHeight = (int) (1.5 * textFontSize);

	public String authors = "NoAuthor";

	public String title = "NoTitle";
	
	/**
	 * Screen resolution in pixel
	 */
	public ScreenResolution screenResolution = new ScreenResolution(640, 480);

	
	public static class ScreenResolution{
		public ScreenResolution(int width, int height) {
			this.width = width;
			this.height = height;
		}
		public final int width;
		public final int height;
	}
	
}
