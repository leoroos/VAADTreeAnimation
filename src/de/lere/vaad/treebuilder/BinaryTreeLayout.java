package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;

import javax.annotation.Nonnull;

import de.lere.vaad.BinaryTreeProperties;

import edu.umd.cs.findbugs.annotations.NonNull;

import algoanim.properties.AnimationPropertiesKeys;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;

public class BinaryTreeLayout {

	/**
	 * A default {@link BinaryTreeLayout} instance.
	 */
	public static final BinaryTreeLayout DEFAULT = createDefaultLayout();

	private final Point rootLocation;
	/**
	 * the horizontal distance of the root to its left and its right child.
	 */
	private final double firstLevelWidth;

	private final String graphName;
	private final int verticalGap;
	private final GraphProperties graphProperties;

	/**
	 * <p>
	 * 
	 * @param rootLocation
	 *            the location of the graph root
	 * @param firstLevelWidth
	 *            the horizontal distance of the root to one of its children
	 * @param verticalGap
	 *            the vertical distance from any level n to level n+1
	 * @param graphProperties
	 *            background color of nodes
	 * @param graphName
	 *            TODO
	 */
	public @Nonnull
	BinaryTreeLayout(Point rootLocation, int firstLevelWidth, int verticalGap,
			GraphProperties graphProperties, String graphName) {
		if (rootLocation == null)
			throw new IllegalArgumentException("root must be given");
		this.rootLocation = rootLocation;
		this.firstLevelWidth = firstLevelWidth;
		this.verticalGap = verticalGap;
		this.graphProperties = graphProperties;
		this.graphName = graphName;
	}

	private static BinaryTreeLayout createDefaultLayout() {
		BinaryTreeLayout layout = new BinaryTreeLayout(new Point(320, 0), 120,
				30, getDefaultGraphProperties(), "DefaultGraphName");
		return layout;
	}

	public static GraphProperties getDefaultGraphProperties() {
		GraphProperties graphProperties = new GraphProperties("Default"
				+ BinaryTreeLayout.class.getName() + "Properties");
		/*
		 * The background Color
		 */
		graphProperties.set(AnimationPropertiesKeys.FILL_PROPERTY, Color.WHITE);
		graphProperties.set(AnimationPropertiesKeys.HIGHLIGHTCOLOR_PROPERTY,
				Color.RED);
		return graphProperties;
	}

	public BinaryTreeLayout(Point graphRootPoint, int firstLevelWidth2,
			int verticalSpacing, GraphProperties graphProperties) {
		this(graphRootPoint, firstLevelWidth2, verticalSpacing,
				graphProperties, "DefaultGraphName");
	}

	/**
	 * The absolute location of the graph root.
	 */
	public Point getRootPoint() {
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

	public String getGraphName() {
		return graphName;
	}

	/**
	 * the horizontal distance of the root to its left and its right child.
	 */
	public int getFirstLevelWidth() {
		return (int) firstLevelWidth;
	}

	public int getVerticalGap() {
		return verticalGap;
	}

	public GraphProperties getGraphProperties() {
		return graphProperties;
	}
}