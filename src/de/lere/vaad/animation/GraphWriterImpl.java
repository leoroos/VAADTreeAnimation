package de.lere.vaad.animation;

import static de.lere.vaad.animation.Timings.NOW;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import algoanim.exceptions.IllegalDirectionException;
import algoanim.primitives.Graph;
import algoanim.primitives.Polyline;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Hidden;
import algoanim.util.TicksTiming;
import algoanim.util.Timing;
import de.lere.vaad.treebuilder.BinaryTreeLayout;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.utils.MathHelper;

public class GraphWriterImpl<T extends Comparable<T>> implements GraphWriter<T> {

	private @Nonnull
	Graph lastCreatedGraph;
	private Language language;
	private int polylineId;
	private BinaryTreeLayout layout;

	public GraphWriterImpl(Language lang, BinaryTreeLayout layout) {
		this(lang, new NullGraph(), layout);
	}

	public GraphWriterImpl(Language lang, @Nonnull Graph lastCreatedGraph,
			BinaryTreeLayout layout) {
		this.language = lang;
		this.lastCreatedGraph = lastCreatedGraph;
		polylineId = 0;
		if (layout == null)
			this.layout = BinaryTreeLayout.DEFAULT;
		else
			this.layout = layout;
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
//		if (lastCreatedGraph != null) {
			lastCreatedGraph.hide(delay);
//			lastCreatedGraph = new NullGraph();
//		}
		if (model.size() < 1) {
			// drawing empty graph will lead to error
			return;
		}
		algoanim.util.Node[] nodes = getAnimalNodes(model.getNodesInOrder(),
				layout);
		lastCreatedGraph = language.newGraph(layout.getGraphName(),
				infos.matrix, nodes, infos.animalLabels, null,
				layout.getGraphProperties());
		lastCreatedGraph.hide(Timings.NOW);
		lastCreatedGraph.show(delay);
	}

	@Override
	public void highlightNode(BinaryTreeModel<T> model, Node<T> endNode,
			Timing startWhen, Timing howLong) {
		Integer index = getNodeIndex(model, endNode);
		if (index != null) {
			lastCreatedGraph.highlightNode(index, startWhen, howLong);
		}
	}

	@Override
	public void unhighlightNode(BinaryTreeModel<T> model, Node<T> endNode,
			Timing startWhen, Timing howLong) {
		Integer index = getNodeIndex(model, endNode);
		if (index != null) {
			lastCreatedGraph.unhighlightNode(index, startWhen, howLong);
		}
	}

	@Override
	public void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing startWhen, Timing howLong) {
		Integer startIndex = getNodeIndex(model, startNode);
		Integer endIndex = getNodeIndex(model, endNode);

		if (startIndex != null && endIndex != null) {
			lastCreatedGraph.highlightEdge(startIndex, endIndex, startWhen,
					howLong);
		}
	}

	@Override
	public void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			de.lere.vaad.treebuilder.Node<T> endNode, Timing startWhen,
			Timing howLong) {
		Integer startIndex = getNodeIndex(model, startNode);
		Integer endIndex = getNodeIndex(model, endNode);

		if (startIndex != null && endIndex != null) {
			lastCreatedGraph.unhighlightEdge(startIndex, endIndex, startWhen,
					howLong);
		}
	}

	private Integer getNodeIndex(BinaryTreeModel<T> model, Node<T> node) {
		OrderedGraphInformation<T> graphInfos = graphInfos(model);
		Integer index = graphInfos.indexedNodes.get(node);
		return index;
	}

	static <T extends Comparable<T>> List<Coordinates> generateCoordinates(
			List<de.lere.vaad.treebuilder.Node<T>> list, BinaryTreeLayout layout) {
		List<Coordinates> lst = new ArrayList<Coordinates>();
		for (de.lere.vaad.treebuilder.Node<T> node : list) {
			int position = node.getPosition();
			Point location = MathHelper.getLocation(layout.getRootPoint(),
					position, layout.getFirstLevelWidth(), layout.getVerticalGap());
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

		if (startIndex != null && endIndex != null) {
			lastCreatedGraph.hideEdge(startIndex, endIndex, when, howLong);
		}
	}

	@Override
	public void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo, Timing when, Timing howLong) {
		OrderedGraphInformation<T> origInfos = graphInfos(initialPosition);
		for (Node<T> original : origInfos.nodes) {
			Node<T> moved = BinaryTreeModel.lookupNodeByID(positionToMoveTo,
					original);
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
					+ polylineId, new Hidden());
			polyline.hide(Timings.NOW);
			try {
				lastCreatedGraph.moveVia(null, "translate #" + (oldIndex + 1),
						polyline, when, howLong);
			} catch (IllegalDirectionException e) {
				throw new IllegalArgumentException("Laber");
			}
			polylineId++;
		}
	}

	private Point getAbsoluteLocationForTreePosition(int newPosition) {
		Point newLocation = MathHelper.getLocation(layout.getRootPoint(),
				newPosition, layout.getFirstLevelWidth(), layout.getVerticalGap());
		return newLocation;
	}

	@Override
	public void buildGraph(BinaryTreeModel<T> model) {
		this.buildGraph(model, NOW);
	}

	@Override
	public void highlightNode(BinaryTreeModel<T> model, Node<T> node) {
		this.highlightNode(model, node, NOW, NOW);
	}

	@Override
	public void unhighlightNode(BinaryTreeModel<T> model, Node<T> node) {
		this.unhighlightNode(model, node, NOW, NOW);

	}

	@Override
	public void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode) {
		this.highlightEdge(model, startNode, endNode, NOW, NOW);

	}

	@Override
	public void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode) {
		this.unhighlightEdge(model, startNode, endNode, NOW, NOW);
	}

	@Override
	public void hideEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode) {
		this.hideEdge(model, startNode, endNode, NOW, NOW);
	}

	@Override
	public void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo) {
		this.translateNodes(initialPosition, positionToMoveTo, NOW, NOW);
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

	public void hideCurrent() {
		this.lastCreatedGraph.hide();
	}

	public void hideCurrent(Timing t) {
		this.lastCreatedGraph.hide(t);
	}

	public void showCurrent() {
		this.lastCreatedGraph.show();
	}

	public void showCurrent(Timing t) {
		this.lastCreatedGraph.show(t);
	}
}
