package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;

import de.lere.vaad.BinaryTreeProperties;

import edu.umd.cs.findbugs.annotations.NonNull;

import algoanim.util.Coordinates;

public class BinaryTreeLayout {

	/**
	 * A default {@link BinaryTreeLayout} instance.
	 */
	public static final BinaryTreeLayout DEFAULT = createDefaultLayout();
	public static final Color DEFAULT_BG_COLOR = Color.WHITE;
	/**
	 * The absolute location of the graph root.
	 */
	public Point rootLocation;
	/**
	 * the horizontal distance of the root to its left and its right child.
	 */
	public double firstLevelWidth;
	/**
	 * The vertical gap between node of level n and n+1
	 */
	public double verticalGap;
	public Color bgColor;
	public String graphName;
	public BinaryTreeProperties aps;

	/**
	 * Constructor to initialize a Layout for the
	 * {@link TreeEventListenerAggregator}.
	 * <p>
	 * 
	 * @param rootLocation
	 *            the location of the graph root
	 * @param firstLevelWidth
	 *            the horizontal distance of the root to one of its children
	 * @param verticalGap
	 *            the vertical distance from any level n to level n+1
	 * @param bgColor
	 *            background color of nodes
	 * @param graphName
	 *            TODO
	 */
	public BinaryTreeLayout(@NonNull Point rootLocation, int firstLevelWidth,
			int verticalGap, Color bgColor, String graphName) {
		if (rootLocation == null)
			throw new IllegalArgumentException("root must be given");
		this.rootLocation = rootLocation;
		this.firstLevelWidth = firstLevelWidth;
		this.verticalGap = verticalGap;
		this.bgColor = bgColor;
		this.graphName = graphName;
		this.aps = new BinaryTreeProperties();
	}

	private static BinaryTreeLayout createDefaultLayout() {
		BinaryTreeLayout layout = new BinaryTreeLayout(
				new Point(320, 0), 120, 30, Color.WHITE, "DefaultGraphName");
		return layout;
	}

	public BinaryTreeLayout(Point graphRootPoint, int firstLevelWidth2,
			int verticalSpacing) {
		this(graphRootPoint, firstLevelWidth2, verticalSpacing,
				DEFAULT_BG_COLOR, "DefaultGraphName");
	}

	public void setAnimationProperties(BinaryTreeProperties aps) {
		if (aps != null)
			this.aps = aps;
	}

	Point getRootPoint() {
		return this.rootLocation;
	}

	public Point getNWBoundary() {

		int xBoundary = this.rootLocation.x - (int) firstLevelWidth * 2;
		return new Point(xBoundary, this.rootLocation.y);
	}

	public Point getNEBoundary() {
		int xBoundary = this.rootLocation.x + (int) firstLevelWidth * 2;
		return new Point(xBoundary, rootLocation.y);
	}
}