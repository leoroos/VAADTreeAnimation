package de.lere.vaad.utils;

import java.awt.Point;

import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.Offset;

/**
 * Utility class that provides simple operations on {@link Node}. Mostly
 * conversions from and to {@link Point}.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 */
public class NodeHelper {
	public static Point convertOffsetToAWTPoint(Offset o) {
		Point point = new Point(o.getX(), o.getY());
		return point;
	}

	public static Point convertCoordinatesToAWTPoint(Coordinates o) {
		Point point = new Point(o.getX(), o.getY());
		return point;
	}

}