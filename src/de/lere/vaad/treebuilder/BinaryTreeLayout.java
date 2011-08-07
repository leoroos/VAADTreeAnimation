package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;

import algoanim.util.Coordinates;

public class BinaryTreeLayout {

	public static final Color DEFAULT_BG_COLOR = Color.WHITE;
	public final algoanim.util.Coordinates rootLocation;
	public final double firstLevelWidth;
	public final double verticalGaps;
	public final Color bgColor;
	public final String graphName;

	/**
	 * Constructor to initialize a Layout for the
	 * {@link BinaryTreeAnimationBuilder}.
	 * <p>
	 * The rootLocation must be a {@link Coordinates} object to assure that
	 * it can be used without causing any exceptions that can occur when
	 * using Offsets of the currently used Animal application (i.e. Version
	 * 2.3.27, 4-10-2011).
	 * 
	 * @param rootLocation
	 *            the location of the graph root
	 * @param firstLevelWidth
	 *            the horizontal distance of the root to one of its children
	 * @param verticalGap
	 *            the vertical distance from any level n to level n+1
	 * @param bgColor background color of nodes
	 * @param graphName TODO
	 */
	public BinaryTreeLayout(algoanim.util.Coordinates rootLocation,
			int firstLevelWidth, int verticalGap, Color bgColor, String graphName) {
		this.rootLocation = rootLocation;
		this.firstLevelWidth = firstLevelWidth;
		this.verticalGaps = verticalGap;
		this.bgColor = bgColor;
		this.graphName = graphName;
	}


	public BinaryTreeLayout(Coordinates builderGraphrootCoords,
			int firstLevelWidth2, int verticalSpacing) {
		this(builderGraphrootCoords, firstLevelWidth2, verticalSpacing, DEFAULT_BG_COLOR, "DefaultGraphName");
	}

	Point getRootPoint() {
		return new Point(rootLocation.getX(), rootLocation.getY());
	}
}