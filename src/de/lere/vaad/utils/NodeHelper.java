package de.lere.vaad.utils;

import java.awt.Point;

import algoanim.util.Coordinates;
import algoanim.util.Offset;

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