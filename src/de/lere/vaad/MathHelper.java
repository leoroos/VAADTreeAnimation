package de.lere.vaad;

import java.awt.geom.Point2D;

public class MathHelper {
	/**
	 * @param rootLocation
	 *            absolute location of root node, i.e. node #1
	 * @param nodeIndex
	 *            the node for which to calculate the position, see Figure 1 how
	 *            to interpret it. Begins with 1.
	 * @param absVerticalDistance
	 * @param firstLevelHorizontalDist the horizontal distance from a parent to one of its children
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
	public static Point2D getLocation(Point2D rootLocation,
			int nodeIndex, double absVerticalDistance,
			double firstLevelHorizontalDist) {
		assert nodeIndex > 0;
		double level = Math.floor(lg(nodeIndex));
		if(level == 0)
			return rootLocation;
		
		double totalVerticalDistance = rootLocation.getY() + (level * absVerticalDistance);
		double levelHorizontalDist = firstLevelHorizontalDist / level;
		
		double totalHorizontalDistance = rootLocation.getX() + levelHorizontalDist * getRelNodeFromHalfPos(nodeIndex);
		
		return new Point2D.Double(totalHorizontalDistance, totalVerticalDistance);
	}
	
	 static int getRelNodeFromHalfPos(int nodeIndex) {
		
		double level = Math.floor(MathHelper.lg(nodeIndex));
		
		int numberNodesOnLevel = (int) Math.pow(2, level);
		int numberNodesOnPrevAllLevels = numberNodesOnLevel - 1 ;

		int relNodeIndex = nodeIndex - numberNodesOnPrevAllLevels;
		
		int halfNodesOnLevel = numberNodesOnLevel / 2;
		
		int relNodeFromHalf;		
		if(relNodeIndex <= halfNodesOnLevel)
			relNodeFromHalf = relNodeIndex -halfNodesOnLevel -1;
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
