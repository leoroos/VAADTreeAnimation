package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;

import algoanim.util.Coordinates;

public class BinaryTreeLayout {

	private static final Color DEFAULT_BG_COLOR = Color.WHITE;
	final algoanim.util.Coordinates rootLocation;
	final double firstLevelWidth;
	final double verticalGaps;
	final Color bgColor;

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
	 */
	public BinaryTreeLayout(algoanim.util.Coordinates rootLocation,
			int firstLevelWidth, int verticalGap, Color bgColor) {
		this.rootLocation = rootLocation;
		this.firstLevelWidth = firstLevelWidth;
		this.verticalGaps = verticalGap;
		this.bgColor = bgColor;
	}

	public BinaryTreeLayout(BinaryTreeLayout layout) {
		this.rootLocation = layout.rootLocation;
		this.firstLevelWidth = layout.firstLevelWidth;
		this.verticalGaps = layout.verticalGaps;
		this.bgColor = layout.bgColor;
	}


	public BinaryTreeLayout(Coordinates builderGraphrootCoords,
			int firstLevelWidth2, int verticalSpacing) {
		this(builderGraphrootCoords, firstLevelWidth2, verticalSpacing, DEFAULT_BG_COLOR);
	}

	Point getRootPoint() {
		return new Point(rootLocation.getX(), rootLocation.getY());
	}
}