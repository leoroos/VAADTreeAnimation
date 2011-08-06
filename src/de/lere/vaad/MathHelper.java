package de.lere.vaad;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class MathHelper {
	/**
	 * @param rootLocation
	 *            absolute location of root node, i.e. node #1
	 * @param nodeIndex
	 *            the node for which to calculate the position, see Figure 1 how
	 *            to interpret it. Begins with 1.
	 * @param firstLevelHorizontalDist
	 *            the horizontal distance from the root to one of its children
	 * @param absVerticalDistance
	 *            the vertical distance between nodes of a level n and n+1
	 * @return
	 * 
	 *         Absolute Graph Coordinate Definitions
	 * 
	 *         <pre>
	 *                          1
	 *                      /       \
	 *                   2              3
	 *                /     \         /    \
	 *               4       5       6      7
	 *              / \     / \     / \    / \
	 *             8   9   10  11  12 13  14 15
	 * Figure 1: Absolute indices of nodes in a binary tree
	 * </pre>
	 */
	public static Point2D getLocation(Point2D rootLocation, int nodeIndex,
			double firstLevelHorizontalDist, double absVerticalDistance) {
		assert nodeIndex > 0;
		double level = Math.floor(lg(nodeIndex));
		if (level == 0)
			return rootLocation;

		double totalVerticalDistance = rootLocation.getY()
				+ (level * absVerticalDistance);
//		double levelHorizontalDist = firstLevelHorizontalDist / level;
//
//		int relNodeFromHalfPos = getRelNodeFromHalfPos(nodeIndex);
//		double totalHorizontalDistance = rootLocation.getX()
//				+ (2 * levelHorizontalDist * getRelNodeFromHalfPos(nodeIndex))
//				- (Math.signum(relNodeFromHalfPos) * levelHorizontalDist);
//
//		return new Point2D.Double(totalHorizontalDistance,
//				totalVerticalDistance);
		double minXBoundary = (rootLocation.getX() - ( 2* firstLevelHorizontalDist));
		double parentNodeHorizontalDistance =  ( firstLevelHorizontalDist / (Math.pow(2,(level-1))) );
		double internodeDistance = 2*parentNodeHorizontalDistance;
		double levelRelativeNodePosition = ( nodeIndex - ( Math.pow(2, level) - 1) );
		double minBoundaryRelativeHDistance = parentNodeHorizontalDistance + ( ( levelRelativeNodePosition - 1 ) * internodeDistance );
		double positionX = minXBoundary + minBoundaryRelativeHDistance;
		return new Point2D.Double(positionX, totalVerticalDistance);
	}

	/**
	 * This is a convenience method working on {@link Point} instead of
	 * {@link Point2D}.
	 * 
	 * @see #getLocation(Point2D, int, double, double)
	 */
	public static Point getLocation(Point rootLocation, int nodeIndex,
			double firstLevelHorizontalDist, double absVerticalDistance) {
		Double dpoint = new Point2D.Double(rootLocation.x, rootLocation.y);
		Point2D location = getLocation(dpoint, nodeIndex,
				firstLevelHorizontalDist, absVerticalDistance);
		int x = (int) location.getX();
		int y = (int) location.getY();
		validateInIntegerRange(x, location.getX());
		validateInIntegerRange(y, location.getY());
		return new Point(x, y);
	}

	private static void validateInIntegerRange(int x, double x2) {
		if (x < Math.floor(x2)) {
			throw new IllegalArgumentException(
					"computed value exceeds Integer.MaxValue originally: " + x2
							+ " asInteger: " + x);
		}
	}

	static int getRelNodeFromHalfPos(int nodeIndex) {

		double level = Math.floor(MathHelper.lg(nodeIndex));

		int numberNodesOnLevel = (int) Math.pow(2, level);
		int numberNodesOnPrevAllLevels = numberNodesOnLevel - 1;

		int relNodeIndex = nodeIndex - numberNodesOnPrevAllLevels;

		int halfNodesOnLevel = numberNodesOnLevel / 2;

		int relNodeFromHalf;
		if (relNodeIndex <= halfNodesOnLevel)
			relNodeFromHalf = relNodeIndex - halfNodesOnLevel - 1;
		else
			relNodeFromHalf = relNodeIndex - halfNodesOnLevel;

		return relNodeFromHalf;
	}

	/**
	 * The logarithm to base 2
	 * 
	 * @param x
	 * @return
	 */
	public static double lg(double x) {
		return Math.log10(x) / Math.log10(2.0d);
	}

}
