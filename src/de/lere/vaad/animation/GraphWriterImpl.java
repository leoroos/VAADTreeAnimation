package de.lere.vaad.animation;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import de.lere.vaad.BinaryTreeProperties;
import de.lere.vaad.animation.nullstubs.NullGraph;
import de.lere.vaad.binarysearchtree.BinaryTreeSetup;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.utils.MathHelper;

public class GraphWriterImpl<T extends Comparable<T>> implements GraphWriter<T> {

	static class GraphNameHandler {
		private static int overallGens = 1;
		public final String initname;
		private int nameCount = 1;
		private String next;
		private String markerStr = "-";

		public GraphNameHandler(String initname) {
			this.initname = initname;
			next = initname + markerStr + "Starter+all" + overallGens++;
		}

		public String name() {
			String retName = next;
			next = initname + markerStr + "Generation" + nameCount++ + "all"
					+ overallGens++;
			return retName;
		}
	}

	private @Nonnull
	Graph lastCreatedGraph;
	private final Language language;
	private final BinaryTreeLayout layout;
	private final GraphNameHandler gname;
	private final Timings ts;
	private int runningPolylineId;
	private boolean autoUnhighlight = false;
	@SuppressWarnings("unchecked")
	// empty array without generics shouldn't be to harmful
	private Node<T>[] previouslyHighlightedNodes = new Node[0];
	private BinaryTreeModel<T> previousHighlightModel = new BinaryTreeModel();

	public @Nonnull
	GraphWriterImpl(Language language, BinaryTreeLayout layout, Timings timings) {
		this(new NullGraph(), language, layout, timings);
	}

	public @Nonnull
	GraphWriterImpl(Graph lastCreatedGraph, Language language,
			BinaryTreeLayout layout, Timings timings) {
		this.language = language;
		this.lastCreatedGraph = lastCreatedGraph;
		runningPolylineId = 0;
		this.gname = new GraphNameHandler(layout.getGraphName());
		this.ts = timings;
		this.layout = layout;
	}

	/**
	 * For prototyping only
	 * 
	 * @param language2
	 * @param layout
	 */
	public GraphWriterImpl(Language language2, BinaryTreeLayout layout) {
		this(language2, layout, new Timings());
	}

	@Override
	public void buildGraph(BinaryTreeModel<T> model, Timing delay) {
		OrderedGraphInformation<T> infos = graphInfos(model);
		writeGraph(infos, model, layout, delay);
	}

	public OrderedGraphInformation<T> graphInfos(BinaryTreeModel<T> model) {
		OrderedGraphInformation<T> infos = new OrderedGraphInformation<T>(
				model.getNodesInOrder());
		return infos;
	}

	public void setLastGraph(Graph graph) {
		if (graph == null) {
			this.lastCreatedGraph = new NullGraph();
		} else
			this.lastCreatedGraph = graph;
	}

	public void writeGraph(OrderedGraphInformation<T> infos,
			BinaryTreeModel<T> model, BinaryTreeLayout layout, Timing delay) {
		lastCreatedGraph.hide(delay);
		if (model.size() < 1) {
			/* drawing empty graph will lead to error */
			return;
		}
		algoanim.util.Node[] nodes = getAnimalNodes(model.getNodesInOrder(),
				layout);
		lastCreatedGraph = language.newGraph(gname.name(), infos.matrix, nodes,
				infos.animalLabels, null, layout.getGraphProperties());
		lastCreatedGraph.hide(ts.NOW);
		lastCreatedGraph.show(delay);
	}

	@Override
	public void highlightNode(BinaryTreeModel<T> model, Node<T> nodes,
			Timing startWhen, Timing howLong) {
		highlightNode(model, startWhen, howLong, new Node[] { nodes });
	}

	@Override
	public void highlightNode(BinaryTreeModel<T> model, Timing startWhen,
			Timing howLong, Node<T>... nodes) {

		if (autoUnhighlight) {
			for (Node<T> node : previouslyHighlightedNodes) {
				Node<T> nodeByID = model.getNodeByID(node);
				try {
					unhighlightNode(previousHighlightModel, nodeByID, ts.NOW,
							startWhen);
				} catch (IllegalArgumentException e) {
					/*
					 * can probably happen because the current graph has changed
					 * in a way that not all previous node are still where they
					 * were or are even accessible
					 */
					e.printStackTrace();
				}
			}
		}
		previouslyHighlightedNodes = nodes;
		previousHighlightModel = model;

		for (Node<T> node : nodes) {
			internalHighlight(model, startWhen, howLong, node);
		}
	}

	private void internalHighlight(BinaryTreeModel<T> model, Timing startWhen,
			Timing howLong, Node<T> node) {
		Integer index = getNodeIndex(model, node);
		if (validateIndex(index)) {
			lastCreatedGraph.highlightNode(index, startWhen, howLong);
		} else
			System.out
					.println("GraphWriterImpl.highlightNode(): did not find index in model for node "
							+ node);
	}

	@Override
	public void setAutomaticUnhighlightNodes(boolean unhighlight) {
		this.autoUnhighlight = unhighlight;
	}

	@Override
	public boolean isAutomaticUnhighlight() {
		return autoUnhighlight;
	}

	private boolean validateIndex(Integer index) {
		/* If this validation fails you probably haven't drawn the graph yet */
		if (index != null) {
			int[][] adjacencyMatrix = lastCreatedGraph.getAdjacencyMatrix();
			if (adjacencyMatrix == null)
				throw new IllegalStateException(
						"graph has no adjacency matrix yet");
			else if (adjacencyMatrix.length < index) {
				StringBuffer aMasString = new StringBuffer();
				for (int[] is : adjacencyMatrix) {
					aMasString.append(Arrays.toString(is));
				}
				throw new IllegalStateException("index " + index
						+ " inconsistent to adjacencyMatrix of length "
						+ adjacencyMatrix.length + " and dimension "
						+ aMasString);
			} else
				return true;
		} else
			// Ignoring null
			return false;
	}

	@Override
	public void unhighlightNode(BinaryTreeModel<T> model, Node<T> endNode,
			Timing startWhen, Timing howLong) {
		Integer index = getNodeIndex(model, endNode);
		if (validateIndex(index)) {
			lastCreatedGraph.unhighlightNode(index, startWhen, howLong);
		} else
			System.out
					.println("GraphWriterImpl.unhighlightNode(): did not find index in model for node "
							+ endNode);
	}

	@Override
	public void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing startWhen, Timing howLong) {
		Integer startIndex = getNodeIndex(model, startNode);
		Integer endIndex = getNodeIndex(model, endNode);

		if (validateIndex(startIndex) && validateIndex(endIndex)) {
			lastCreatedGraph.highlightEdge(startIndex, endIndex, startWhen,
					howLong);
		} else
			System.out
					.println("GraphWriterImpl.highlightEdge(): did not find index in model for node "
							+ startNode + " or " + endNode);
	}

	@Override
	public void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			de.lere.vaad.treebuilder.Node<T> endNode, Timing startWhen,
			Timing howLong) {
		Integer startIndex = getNodeIndex(model, startNode);
		Integer endIndex = getNodeIndex(model, endNode);

		if (validateIndex(startIndex) && validateIndex(endIndex)) {
			lastCreatedGraph.unhighlightEdge(startIndex, endIndex, startWhen,
					howLong);
		} else
			System.out
					.println("GraphWriterImpl.unhighlightEdge(): did not find index in model for node "
							+ startNode + " or " + endNode);
	}

	private Integer getNodeIndex(BinaryTreeModel<T> model, Node<T> node) {
		Node<T> byID = model.getNodeByID(node);
		OrderedGraphInformation<T> graphInfos = graphInfos(model);
		Integer index = graphInfos.indexedNodes.get(byID);
		return index;
	}

	static <T extends Comparable<T>> List<Coordinates> generateCoordinates(
			List<de.lere.vaad.treebuilder.Node<T>> list, BinaryTreeLayout layout) {
		List<Coordinates> lst = new ArrayList<Coordinates>();
		for (de.lere.vaad.treebuilder.Node<T> node : list) {
			int position = node.getPosition();
			Point location = MathHelper.getLocation(layout.getRootPoint(),
					position, layout.getFirstLevelWidth(),
					layout.getVerticalGap());
			lst.add(algoanim.util.Node.convertToNode(location));
		}
		return lst;
	}

	algoanim.util.Node[] getAnimalNodes(
			List<de.lere.vaad.treebuilder.Node<T>> lst, BinaryTreeLayout layout) {
		return generateCoordinates(lst, layout).toArray(new Coordinates[0]);
	}

	@Override
	public void hideEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing when, Timing howLong) {
		Integer startIndex = getNodeIndex(model, startNode);
		Integer endIndex = getNodeIndex(model, endNode);

		if (validateIndex(startIndex) && validateIndex(endIndex)) {
			lastCreatedGraph.hideEdge(startIndex, endIndex, when, howLong);
		} else
			System.out
					.println("GraphWriterImpl.hideEdge(): did not find index in model for node "
							+ startNode + " or " + endNode);
	}

	@Override
	public void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo, Timing when, Timing howLong) {
		OrderedGraphInformation<T> origInfos = graphInfos(initialPosition);
		for (Node<T> original : origInfos.nodes) {
			Node<T> moved = positionToMoveTo.getNodeByID(original);
			if (moved == null)
				continue;

			int oldPosition = original.getPosition();
			int newPosition = moved.getPosition();
			Point oldLocation = getAbsoluteLocationForTreePosition(oldPosition);
			Point newLocation = getAbsoluteLocationForTreePosition(newPosition);

			Integer oldIndex = origInfos.indexedNodes.get(original);
			Polyline polyline = language.newPolyline(new Coordinates[] {
					algoanim.util.Node.convertToNode(oldLocation),
					algoanim.util.Node.convertToNode(newLocation) }, "vec"
					+ runningPolylineId, new Hidden());
			polyline.hide(ts.NOW);
			try {
				lastCreatedGraph.moveVia(null, "translate #" + (oldIndex + 1),
						polyline, when, howLong);
			} catch (IllegalDirectionException e) {
				throw new IllegalArgumentException("Laber");
			}
			runningPolylineId++;
		}
	}

	private Point getAbsoluteLocationForTreePosition(int newPosition) {
		Point newLocation = MathHelper.getLocation(layout.getRootPoint(),
				newPosition, layout.getFirstLevelWidth(),
				layout.getVerticalGap());
		return newLocation;
	}

	@Override
	public void buildGraph(BinaryTreeModel<T> model) {
		this.buildGraph(model, ts.NOW);
	}

	@Override
	public void highlightNode(BinaryTreeModel<T> model, Node<T> node) {
		this.highlightNode(model, node, ts.NOW, ts.NOW);
	}

	@Override
	public void unhighlightNode(BinaryTreeModel<T> model, Node<T> node) {
		this.unhighlightNode(model, node, ts.NOW, ts.NOW);

	}

	@Override
	public void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode) {
		this.highlightEdge(model, startNode, endNode, ts.NOW, ts.NOW);

	}

	@Override
	public void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode) {
		this.unhighlightEdge(model, startNode, endNode, ts.NOW, ts.NOW);
	}

	@Override
	public void hideEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode) {
		this.hideEdge(model, startNode, endNode, ts.NOW, ts.NOW);
	}

	@Override
	public void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo) {
		this.translateNodes(initialPosition, positionToMoveTo, ts.NOW, ts.NOW);
	}

	@Override
	public void blinkNode(BinaryTreeModel<T> model, Node<T> node, Timing when,
			Timing howLong) {

		int halfTheLongDelay = (int) (((double) howLong.getDelay()) / 2);
		Timing halfTheLong = new TicksTiming(halfTheLongDelay);
		this.highlightNode(model, node, when, halfTheLong);
		this.unhighlightNode(model, node,
				new TicksTiming(halfTheLong.getDelay() + when.getDelay()),
				halfTheLong);
	}

	@Override
	public void hideCurrent() {
		this.lastCreatedGraph.hide();
	}

	@Override
	public void hideCurrent(Timing t) {
		this.lastCreatedGraph.hide(t);
	}

	@Override
	public void showCurrent() {
		this.lastCreatedGraph.show();
	}

	@Override
	public void showCurrent(Timing t) {
		this.lastCreatedGraph.show(t);
	}
}
