package de.lere.vaad.utils;

import algoanim.util.Coordinates;
import algoanim.util.Offset;

public interface OffsetFactory {

	Offset getOffset(Coordinates aCoordinate, int x, int y);

}