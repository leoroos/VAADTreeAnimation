package de.lere.vaad.treebuilder;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.lere.vaad.utils.MathHelper;
import algoanim.primitives.Graph;
import algoanim.primitives.generators.Language;
import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import de.lere.vaad.treebuilder.Node;
import algoanim.util.Timing;

public class GraphWriterImpl<T extends Comparable<T>> implements GraphWriter<T> {

	private Graph lastCreatedGraph;

	public GraphWriterImpl() {
		lastCreatedGraph = new NullGraph();		
	}

	@Override
	public void buildGraph(Language language,
			BinaryTreeModel<T> model, BinaryTreeLayout layout) {
		OrderedGraphInformation<T> infos = graphInfos(model);
		writeGraph(language, infos, model, layout);		
	}

	public OrderedGraphInformation<T> graphInfos(BinaryTreeModel<T> model) {
		OrderedGraphInformation<T> infos = new OrderedGraphInformation<T>(
				model.getNodesInOrder());
		return infos;
	}

	public void writeGraph(Language language, OrderedGraphInformation<T> infos,
			BinaryTreeModel<T> model, BinaryTreeLayout layout) {
		if (lastCreatedGraph != null) {
			lastCreatedGraph.hide(Timings.NOW);
			lastCreatedGraph = new NullGraph();
		}
		if (model.size() < 1) {
			// don't draw empty graph
			return;
		}
		algoanim.util.Node[] nodes = getAnimalNodes(model.getNodesInOrder(),
				layout);
		GraphProperties graphProperties = from(layout);
		lastCreatedGraph = language.newGraph(layout.graphName, infos.matrix,
				nodes, infos.animalLabels, null, graphProperties);
	}

	private GraphProperties from(BinaryTreeLayout layout) {
		GraphProperties gps = new GraphProperties();
		gps.set("fillColor", layout.bgColor);
		gps.set("highlightColor", Color.RED);
		return gps;
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
			lastCreatedGraph.highlightNode(index, startWhen, howLong);
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

	private Integer getNodeIndex(BinaryTreeModel<T> model, Node<T> endNode) {
		OrderedGraphInformation<T> graphInfos = graphInfos(model);
		Integer index = graphInfos.indexedNodes.get(endNode);
		return index;
	}

	static <T extends Comparable<T>> List<Coordinates> generateCoordinates(
			List<de.lere.vaad.treebuilder.Node<T>> list, BinaryTreeLayout layout) {
		List<Coordinates> lst = new ArrayList<Coordinates>();
		for (de.lere.vaad.treebuilder.Node<T> node : list) {
			int position = node.getPosition();
			Point location = MathHelper.getLocation(layout.rootLocation,
					position, layout.firstLevelWidth, layout.verticalGaps);
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
			lastCreatedGraph.hideEdge(startIndex, endIndex, when,
					howLong);
		}
	}

	@Override
	public void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo, BinaryTreeLayout layout, Timing when, Timing howLong) {
		OrderedGraphInformation<T> initialPos = graphInfos(initialPosition);
		OrderedGraphInformation<T> movedPosition = graphInfos(positionToMoveTo);
		Set<Entry<Node<T>,Integer>> entrySet = initialPos.indexedNodes.entrySet();
		for(Entry<Node<T>,Integer> entry : entrySet){
			Node<T> node = entry.getKey();
			int oldIndex = entry.getValue();
			Integer newPos = movedPosition.indexedNodes.get(node);
			if(newPos != null){
				Point location = MathHelper.getLocation(layout.rootLocation, newPos, layout.firstLevelWidth, layout.verticalGaps);
				lastCreatedGraph.translateNode(oldIndex, algoanim.util.Node.convertToNode(location), when, howLong);
			}
		}
	}
}
