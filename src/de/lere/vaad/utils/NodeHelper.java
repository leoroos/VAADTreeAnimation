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
	/**
	 * Expects an Offset object realative to a Coordinate
	 * 
	 * @param o
	 * @return
	 */
	public static Point convertOffsetToAWTPoint(Offset o) {
		Node node = o.getNode();
		if (node == null || !(node instanceof Coordinates))
			throw new IllegalArgumentException(
					"expected object relative to coordinate but was " + node);

		Coordinates ref = (Coordinates) node;
		Point point = new Point(ref.getX() + o.getX(), ref.getY() + o.getY());
		return point;

	}

	public static Point convertCoordinatesToAWTPoint(Coordinates o) {
		Point point = new Point(o.getX(), o.getY());
		return point;
	}

}