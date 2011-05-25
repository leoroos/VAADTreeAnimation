package de.lere.vaad;

import algoanim.util.Coordinates;
import algoanim.util.Offset;

public class OffsetFactoryImpl implements OffsetFactory {

	public Offset getOffset(Coordinates aCoordinate, int x, int y) {
		return new Offset(x, y, aCoordinate, "");
	}

}
