package de.lere.vaad.binarysearchtree;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
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
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.BinaryTreeModelListener;
import de.lere.vaad.treebuilder.TreeDeleteEvent;
import de.lere.vaad.treebuilder.TreeInsertEvent;
import de.lere.vaad.treebuilder.TreeLeftRotateEvent;
import de.lere.vaad.treebuilder.TreeRightRotateEvent;
import de.lere.vaad.treebuilder.TreeSearchEvent;
import de.lere.vaad.utils.MathHelper;
import de.lere.vaad.utils.NodeHelper;

/**
 * provides the animations for a model and it's changes
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public class BinarySearchTreeAnimator<T extends Comparable<T>> implements
		BinaryTreeModelListener<T> {
	public static final BinaryTreeLayout DEFAULT_LAYOUT = new BinaryTreeLayout(
			new Point(240, 0), 120, 30, Color.WHITE, "DefaultGraphName");

	private BinaryTreeModel<T> model;
	private BinaryTreeLayout layout;
	private Language language;
	private Graph lastCreatedGraph;

	private static final Timing NOW = new TicksTiming(0);
	private static final Timing HIGHLIGHT_EDGE_DURATION = new TicksTiming(50);
	private static final Timing HIGHLIGHT_NODE_DURATION = new TicksTiming(50);

	private static final TicksTiming DELETE_NODE_HIGHLIGHT_DURATION = new TicksTiming(
			25);

	public BinarySearchTreeAnimator(Language lang) {
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

	OrderedGraphInformation<T> buildCurrentGraph() {
		OrderedGraphInformation<T> infos = currentGraphInfos();
		writeGraph(infos);
		return infos;
	}

	private OrderedGraphInformation<T> currentGraphInfos() {
		OrderedGraphInformation<T> infos = new OrderedGraphInformation<T>(
				model.getNodesInOrder(), this.layout);
		return infos;
	}

	private void writeGraph(OrderedGraphInformation<T> infos) {
		if (lastCreatedGraph != null) {
			lastCreatedGraph.hide(NOW);
		}
		if (model.size() < 1) {
			// don't draw empty graph
			return;
		}
		lastCreatedGraph = language.newGraph(layout.graphName, infos.matrix,
				infos.animalNodes, infos.animalLabels, null,
				infos.graphProperties);
	}

	private static class OrderedGraphInformation<T extends Comparable<T>> {

		public OrderedGraphInformation(
				List<de.lere.vaad.treebuilder.Node<T>> orderednodes,
				BinaryTreeLayout layout) {
			nodes = orderednodes;
			indexedNodes = nodeListToIndexedMap(nodes);
			matrix = getAdjancencyMatrix(nodes);
			animalNodes = generateCoordinates(nodes, layout).toArray(
					new Coordinates[0]);
			String labels[] = getLabelsFromModel(nodes).toArray(new String[0]);
			animalLabels = labels;
			GraphProperties gps = new GraphProperties();
			gps.set("fillColor", layout.bgColor);
			gps.set("highlightColor", Color.RED);
			graphProperties = gps;
		}

		public GraphProperties graphProperties;
		public String[] animalLabels;
		public Node[] animalNodes;
		public int[][] matrix;
		public HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> indexedNodes;
		public List<de.lere.vaad.treebuilder.Node<T>> nodes;

		public Integer getIndexOf(de.lere.vaad.treebuilder.Node<T> node) {
			return this.indexedNodes.get(node);
		}

	}

	static <T extends Comparable<T>> int[][] getAdjancencyMatrix(
			List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> nodeToIndex = nodeListToIndexedMap(treeNodes);
		int nodes = nodeToIndex.size();
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

	private static <T extends Comparable<T>> HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> nodeListToIndexedMap(
			List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> nodeToIndex = new HashMap<de.lere.vaad.treebuilder.Node<T>, Integer>();
		for (int i = 0; i < treeNodes.size(); i++) {
			nodeToIndex.put(treeNodes.get(i), i);
		}
		return nodeToIndex;
	}

	private static <T extends Comparable<T>> List<String> getLabelsFromModel(
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

	private static <T extends Comparable<T>> List<Coordinates> generateCoordinates(
			List<de.lere.vaad.treebuilder.Node<T>> list, BinaryTreeLayout layout) {
		List<Coordinates> lst = new ArrayList<Coordinates>();
		for (de.lere.vaad.treebuilder.Node<T> node : list) {
			int position = node.getPosition();
			Point location = MathHelper.getLocation(layout.rootLocation,
					position, layout.firstLevelWidth, layout.verticalGaps);
			lst.add(Node.convertToNode(location));
		}
		return lst;
	}

	List<Coordinates> generatePositions() {
		return generateCoordinates(model.getNodesInOrder(), layout);
	}

	/**
	 * <pre>
	 * 	#remove edge to graph
	 * 	highlightEdge on  "Zig" (1,4) 
	 * 	hide "Zig" after 75 ticks	
	 * 	#new graph without edge to moved node	
	 * 	graph "Zig" size 5 highlightColor red nodes { "P" offset (0,0) from "loc1" , "X" offset (0,0) from "loc2" , "C" offset (0,0) from "loc3", "A" offset (0,0) from "loc4" , "B" offset (0,0) from "loc5" } edges { ( 0, 1) ( 0, 2) ( 1 , 3 ) }
	 * }
	 * 
	 * {	
	 * 	move "Zig" type "translate #1" via "vec1_3" within 150 ticks
	 * 	move "Zig" type "translate #2" via "vec2_1" within 150 ticks
	 * 	move "Zig" type "translate #3" via "vec3_7" within 150 ticks
	 * 	move "Zig" type "translate #4" via "vec4_2" within 150 ticks
	 * 	move "Zig" type "translate #5" via "vec5_6" within 150 ticks			
	 * }
	 * 
	 * {
	 * 	hide "Zig"		
	 * 	graph "Zig" size 5 highlightColor red nodes { "X" offset (0,0) from "loc1" ,  "A" offset (0,0) from "loc2" , "P" offset (0,0) from "loc3" , "B" offset (0,0) from "loc6",  "C" offset (0,0) from "loc7" } edges { ( 0, 1) ( 0, 2) ( 2 , 3 ) (2,4) }
	 * 	highlightEdge on "Zig" (2,3)
	 * 	unhighlightEdge on "Zig" (2,3) after 75 ticks
	 * </pre>
	 * */

	@Override
	public void update(TreeInsertEvent<T> event) {
		if (event.nodeOfModification == null)
			return;

		OrderedGraphInformation<T> infos = buildCurrentGraph();

		de.lere.vaad.treebuilder.Node<T> nodeOfModification = event.nodeOfModification;

		List<de.lere.vaad.treebuilder.Node<T>> lineToRoot = getListFromRootToNode(nodeOfModification);
		lineToRoot.remove(lineToRoot.size() - 1);
		for (de.lere.vaad.treebuilder.Node<T> node : lineToRoot) {
			int currentNode = infos.getIndexOf(node);
			writeBlinkNode(currentNode);
		}
		writeHighlightNode(infos.getIndexOf(nodeOfModification));
	}

	private List<de.lere.vaad.treebuilder.Node<T>> getListFromRootToNode(
			de.lere.vaad.treebuilder.Node<T> nodeOfModification) {
		List<de.lere.vaad.treebuilder.Node<T>> lineToRoot = new ArrayList<de.lere.vaad.treebuilder.Node<T>>();
		if (nodeOfModification == null)
			return lineToRoot;
		de.lere.vaad.treebuilder.Node<T> next = nodeOfModification;
		lineToRoot.add(next);
		while (next.hasParent()) {
			next = next.getParent();
			lineToRoot.add(next);
		}
		Collections.reverse(lineToRoot);
		return lineToRoot;
	}

	private void writeBlinkNode(Integer node) {
		if (node == null)
			return;
		writeHighlightNode(node);
		writeUnhighlightNode(node);
	}

	private void writeUnhighlightNode(Integer node) {
		lastCreatedGraph.unhighlightNode(node, HIGHLIGHT_NODE_DURATION,
				HIGHLIGHT_NODE_DURATION);
	}

	@Override
	public void update(TreeDeleteEvent<T> event) {
		if (event.nodeOfModification == null)
			return;

		OrderedGraphInformation<T> oldinfos = infosForNodes(event.beforeChange
				.getNodesInOrder());

		Integer deletee = oldinfos.indexedNodes.get(event.nodeOfModification);
		de.lere.vaad.treebuilder.Node<T> successor2 = event.successor;
		Integer successor = oldinfos.indexedNodes.get(successor2);

		lastCreatedGraph.highlightNode(deletee, NOW,
				DELETE_NODE_HIGHLIGHT_DURATION);
		language.nextStep();

		if (successor != null) {
			lastCreatedGraph.highlightNode(successor,
					DELETE_NODE_HIGHLIGHT_DURATION,
					DELETE_NODE_HIGHLIGHT_DURATION);
			language.nextStep();

			List<de.lere.vaad.treebuilder.Node<T>> nodesAfter = event.afterChange
					.getNodesInOrder();
			List<de.lere.vaad.treebuilder.Node<T>> nodesBefore = event.beforeChange
					.getNodesInOrder();
			nodesBefore.retainAll(nodesAfter);
			// for (int i = 0; i < 2/*nodesBefore.size() */; i ++) {

			// de.lere.vaad.treebuilder.Node<T> node =
			// nodesBefore.get(successor2);
			Integer originalIndex = oldinfos.getIndexOf(successor2);
			Integer newNodePosition = event.afterChange
					.getNodePosition(successor2);

			moveNodeOfIndexToNodePosition(originalIndex,
					newNodePosition);

			// }
			// }

			language.nextStep();
		}

		//
		// lastCreatedGraph.unhighlightNode(
		// indexedNodes, DELETE_NODE_HIGHLIGHT_DURATION,
		// DELETE_NODE_HIGHLIGHT_DURATION);
		// infos.graph.highlightNode(endNode, NOW, HIGHLIGHT_EDGE_DURATION);
		// infos.graph.highlightEdge(startNode, endNode, NOW,
		// HIGHLIGHT_EDGE_DURATION);
		//
		writeGraph(currentGraphInfos());
	}

	private void moveNodeOfIndexToNodePosition(Integer originalPos,
			int newNodePosition) {
		Point newLocation = MathHelper.getLocation(layout.rootLocation,
				newNodePosition, layout.firstLevelWidth, layout.verticalGaps);

		lastCreatedGraph.translateNodes(new int[] { originalPos +1 },
				NodeHelper.convertAWTPointToCoordinates(new Point(0,0)), NOW,
				HIGHLIGHT_NODE_DURATION);
	}

	private OrderedGraphInformation<T> infosForNodes(
			List<de.lere.vaad.treebuilder.Node<T>> nodes) {
		return new OrderedGraphInformation<T>(nodes, this.layout);
	}

	@Override
	public void update(TreeLeftRotateEvent<T> event) {
		buildCurrentGraph();
	}

	@Override
	public void update(TreeRightRotateEvent<T> event) {
		buildCurrentGraph();
	}

	@Override
	public void update(TreeSearchEvent<T> event) {
		OrderedGraphInformation<T> currentGraphInfos = buildCurrentGraph();
		List<de.lere.vaad.treebuilder.Node<T>> listFromRootToNode = getListFromRootToNode(event.nodeOfModification);
		for (int i = 0; i < listFromRootToNode.size() - 1; i++) {
			de.lere.vaad.treebuilder.Node<T> next = listFromRootToNode.get(i);
			writeBlinkNode(currentGraphInfos.getIndexOf(next));
		}
		Integer integer = currentGraphInfos.indexedNodes
				.get(event.nodeOfModification);
		writeHighlightNode(integer);
	}

	private void writeHighlightNode(Integer node) {
		lastCreatedGraph.highlightNode(node, NOW, HIGHLIGHT_NODE_DURATION);
	}
}
