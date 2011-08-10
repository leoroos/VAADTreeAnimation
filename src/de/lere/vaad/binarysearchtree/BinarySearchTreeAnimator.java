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
import de.lere.vaad.treebuilder.Timings;
import de.lere.vaad.treebuilder.TreeEvent;
import de.lere.vaad.treebuilder.TreeEventListener;
import de.lere.vaad.treebuilder.GraphWriterImpl;
import de.lere.vaad.treebuilder.TreeDeleteEvent;
import de.lere.vaad.treebuilder.TreeInsertEvent;
import de.lere.vaad.treebuilder.TreeLeftRotateEvent;
import de.lere.vaad.treebuilder.TreeNewEvent;
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
		TreeEventListener<T> {
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

	public void update(TreeDeleteEvent<T> event) {
		if (event.nodeOfModification == null)
			return;

		OrderedGraphInformation<T> oldinfos = infosForNodes(event.beforeChange
				.getNodesInOrder());

		Integer deletee = oldinfos.indexedNodes.get(event.nodeOfModification);
		de.lere.vaad.treebuilder.Node<T> successorNode = event.successor;
		

		lastCreatedGraph.highlightNode(deletee, NOW,
				DELETE_NODE_HIGHLIGHT_DURATION);
		language.nextStep();

		GraphWriterImpl<T> impl = new GraphWriterImpl<T>(language,
				lastCreatedGraph);

		if (successorNode != null) {
			Integer successor = oldinfos.indexedNodes.get(successorNode);
			de.lere.vaad.treebuilder.Node<T> oldSuccParent = BinaryTreeModel.lookupNodeByID(event.beforeChange, successorNode).getParent();
			// impl.hideEdge(event.beforeChange,
			// successor2,successor2.getParent(), Timings.NOW,
			// Timings._25_TICKS);
			Integer successorParentIndex = oldinfos.getIndexOf(oldSuccParent);
			if (successorParentIndex != null) {
				lastCreatedGraph.hideEdge(successorParentIndex, successor, NOW,
						HIGHLIGHT_NODE_DURATION);
				impl.highlightNode(model, successorNode, NOW,
						DELETE_NODE_HIGHLIGHT_DURATION);
				language.nextStep();
			}
			impl.translateNodes(event.beforeChange, event.afterChange,
					DEFAULT_LAYOUT, NOW, HIGHLIGHT_NODE_DURATION);
			language.nextStep();
		}

		buildCurrentGraph();
	}

	private OrderedGraphInformation<T> infosForNodes(
			List<de.lere.vaad.treebuilder.Node<T>> nodes) {
		return new OrderedGraphInformation<T>(nodes, this.layout);
	}

	public void update(TreeLeftRotateEvent<T> event) {
		buildCurrentGraph();
	}

	public void update(TreeRightRotateEvent<T> event) {
		buildCurrentGraph();
	}

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

	@Override
	public void update(TreeEvent<T> event) {
		if (event instanceof TreeInsertEvent<?>) {
			update((TreeInsertEvent<T>) event);
		} else if (event instanceof TreeDeleteEvent<?>) {
			update((TreeDeleteEvent<T>) event);
		} else if (event instanceof TreeLeftRotateEvent<?>) {
			update((TreeLeftRotateEvent<T>) event);
		} else if (event instanceof TreeRightRotateEvent<?>) {
			update((TreeRightRotateEvent<T>) event);
		} else if (event instanceof TreeNewEvent<?>) {
			update((TreeNewEvent<T>) event);
		} else if (event instanceof TreeSearchEvent<?>) {
			update((TreeSearchEvent<T>) event);
		}

	}

}
