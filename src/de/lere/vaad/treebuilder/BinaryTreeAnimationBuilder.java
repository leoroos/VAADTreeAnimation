package de.lere.vaad.treebuilder;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.DisplayOptions;
import algoanim.util.Node;
import algoanim.util.Offset;
import de.lere.vaad.CorrectedOffset;
import de.lere.vaad.MathHelper;

public class BinaryTreeAnimationBuilder<T extends Comparable<T>> {
	public static class BinaryTreeLayout {

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
		 */
		public BinaryTreeLayout(algoanim.util.Coordinates rootLocation,
				int firstLevelWidth, int verticalGap) {
			this.rootLocation = rootLocation;
			this.firstLevelWidth = firstLevelWidth;
			this.verticalGaps = verticalGap;
		}

		public BinaryTreeLayout(BinaryTreeLayout layout) {
			this.rootLocation = layout.rootLocation;
			this.firstLevelWidth = layout.firstLevelWidth;
			this.verticalGaps = layout.verticalGaps;
		}

		algoanim.util.Coordinates rootLocation;
		double firstLevelWidth;
		double verticalGaps;

		Point getRootPoint() {
			return new Point(rootLocation.getX(), rootLocation.getY());
		}
	}

	public static final BinaryTreeLayout DEFAULT_LAYOUT = new BinaryTreeLayout(
			new Coordinates(240, 0), 120, 30);

	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;

	public BinaryTreeAnimationBuilder(Language lang) {
		this.language = lang;
		this.model = new BinaryTreeModel<T>();
		this.layout = DEFAULT_LAYOUT;
	}

	public void setModel(BinaryTreeModel<T> model) {
		this.model = model;
	}

	public void setLayout(BinaryTreeLayout layout) {
		this.layout = new BinaryTreeLayout(layout);
	}

	public void buildCurrentGraph() {
		int[][] matrix = model.getAdjancencyMatrix();
		 List<de.lere.vaad.treebuilder.Node<T>> treeNodes = model.getNodes();
		algoanim.util.Node [] nodes = generatePositions(treeNodes).toArray(new Coordinates[0]);
		 String labels [] = getLabelsFromModel(treeNodes).toArray(new String[0]);
		 Graph graph = language.newGraph("TBD", matrix, nodes, labels,
		 null);
	}

	private List<String> getLabelsFromModel(
			List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		ArrayList<String> result = new ArrayList<String>();
		for (de.lere.vaad.treebuilder.Node<T> node : treeNodes) {
			result.add(node.getValue().toString());
		}
		return result;
	}
	
	List<String> getLabelsFromModel(){
		return getLabelsFromModel(model.getNodes());
	}

	private class Edge {
		public Edge(Node parentPos, Node childPos) {
			ParentPos = parentPos;
			ChildPos = childPos;
		}

		private final Node ParentPos;
		private final Node ChildPos;
	}

	private List<Coordinates> generatePositions(List<de.lere.vaad.treebuilder.Node<T>> list) {
		List<Coordinates> lst = new ArrayList<Coordinates>();
		for (de.lere.vaad.treebuilder.Node<T> node : list) {
			int position = node.getPosition();
			Point location = MathHelper.getLocation(layout.getRootPoint(),
					position, layout.firstLevelWidth, layout.verticalGaps);
			lst.add(Node.convertToNode(location));
		}
		return lst;
	}
	
	List<Coordinates> generatePositions() {
		return generatePositions(model.getNodes());
	}
}
