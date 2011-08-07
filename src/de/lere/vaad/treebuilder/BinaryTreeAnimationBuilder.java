package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import de.lere.vaad.MathHelper;

public class BinaryTreeAnimationBuilder<T extends Comparable<T>> implements
		BinaryTreeModelListener<T> {
	public static final BinaryTreeLayout DEFAULT_LAYOUT = new BinaryTreeLayout(
			new Coordinates(240, 0), 120, 30, Color.WHITE, "DefaultGraphName");

	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;
	private Graph lastCreatedGraph;

	private Timing now = new TicksTiming(0);

	public BinaryTreeAnimationBuilder(Language lang) {
		this.language = lang;
		this.model = new BinaryTreeModel<T>();
		this.layout = DEFAULT_LAYOUT;
		this.lastCreatedGraph = null;
	}

	public void setModel(BinaryTreeModel<T> model) {
		if (this.model != null) {
			this.model.removeListener(this);
		}
		this.model = model;
		this.model.addListener(this);
		buildCurrentGraph();
	}

	public void setLayout(BinaryTreeLayout layout) {
		this.layout = layout;
	}

	public BinaryTreeLayout getLayout() {
		return layout;
	}

	void buildCurrentGraph() {
		List<de.lere.vaad.treebuilder.Node<T>> treeNodes = model
				.getNodesInOrder();
		int[][] matrix = getAdjancencyMatrix(treeNodes);
		algoanim.util.Node[] nodes = generatePositions(treeNodes).toArray(
				new Coordinates[0]);
		String labels[] = getLabelsFromModel(treeNodes).toArray(new String[0]);
		GraphProperties gps = new GraphProperties();
		gps.set("fillColor", layout.bgColor);
		if (lastCreatedGraph != null) {
			lastCreatedGraph.hide(now);
		}

		lastCreatedGraph = language.newGraph(layout.graphName, matrix, nodes,
				labels, null, gps);
	}

	int[][] getAdjancencyMatrix(List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		int nodes = treeNodes.size();
		HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> nodeToIndex = new HashMap<de.lere.vaad.treebuilder.Node<T>, Integer>();
		for (int i = 0; i < treeNodes.size(); i++) {
			nodeToIndex.put(treeNodes.get(i), i);
		}
		int[][] adjacencyMatrix = new int[nodes][nodes];
		Set<Entry<de.lere.vaad.treebuilder.Node<T>, Integer>> entrySet = nodeToIndex
				.entrySet();
		for (Entry<de.lere.vaad.treebuilder.Node<T>, Integer> entry : entrySet) {
			de.lere.vaad.treebuilder.Node<T> node = entry.getKey();
			Integer index = entry.getValue();
			if (node.hasLeftChild()) {
				adjacencyMatrix[index][nodeToIndex.get(node.getLeft())] = 1;
			}
			if (node.hasRightChild()) {
				adjacencyMatrix[index][nodeToIndex.get(node.getRight())] = 1;
			}
		}
		return adjacencyMatrix;
	}

	private List<String> getLabelsFromModel(
			List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		ArrayList<String> result = new ArrayList<String>();
		for (de.lere.vaad.treebuilder.Node<T> node : treeNodes) {
			result.add(node.getValue().toString());
		}
		return result;
	}

	List<String> getLabelsFromModel() {
		return getLabelsFromModel(model.getNodesInOrder());
	}

	private List<Coordinates> generatePositions(
			List<de.lere.vaad.treebuilder.Node<T>> list) {
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
		return generatePositions(model.getNodesInOrder());
	}

	@Override
	public void updateOnInsert(TreeEvent<T> event) {

	}
}
