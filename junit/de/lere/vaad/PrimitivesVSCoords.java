package de.lere.vaad;

import static org.junit.Assert.*;

import org.junit.Test;

import algoanim.animalscript.AnimalScript;
import algoanim.primitives.Text;
import algoanim.util.Coordinates;

public class PrimitivesVSCoords {

	@Test
	public void testCoordsWithText() throws Exception {
		AnimalScript animalScript = new AnimalScript("test", "tt", 10, 10);
		
		Coordinates aCoordinate = new Coordinates(10, 10);
		
		Text aTextBasedOnCoordinate = animalScript.newText(aCoordinate, "aText", "aTextName", null);
		
		
	}
	
}
