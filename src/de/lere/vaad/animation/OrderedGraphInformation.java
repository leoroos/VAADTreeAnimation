package de.lere.vaad.animation;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import de.lere.vaad.utils.MathHelper;

import algoanim.properties.GraphProperties;
import algoanim.util.Coordinates;
import algoanim.util.Node;

public class OrderedGraphInformation<T extends Comparable<T>> {

	OrderedGraphInformation(List<de.lere.vaad.treebuilder.Node<T>> orderednodes)	 {
		if(orderednodes == null){
			throw new IllegalArgumentException("Nodes must not be null");			
		}	
		nodes = orderednodes;
		indexedNodes = nodeListToIndexedMap(nodes);
		matrix = getAdjancencyMatrix(nodes);		
		String labels[] = getLabelsFromModel(nodes).toArray(new String[0]);
		animalLabels = labels;		
	}
	public final String[] animalLabels;
	public final int[][] matrix;
	public final HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> indexedNodes;
	public final List<de.lere.vaad.treebuilder.Node<T>> nodes;
	
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

	static <T extends Comparable<T>> HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> nodeListToIndexedMap(
			List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		HashMap<de.lere.vaad.treebuilder.Node<T>, Integer> nodeToIndex = new HashMap<de.lere.vaad.treebuilder.Node<T>, Integer>();
		for (int i = 0; i < treeNodes.size(); i++) {
			nodeToIndex.put(treeNodes.get(i), i);
		}
		return nodeToIndex;
	}

	static <T extends Comparable<T>> List<String> getLabelsFromModel(
			List<de.lere.vaad.treebuilder.Node<T>> treeNodes) {
		ArrayList<String> result = new ArrayList<String>();
		for (de.lere.vaad.treebuilder.Node<T> node : treeNodes) {
			result.add(node.getValue().toString());
		}
		return result;
	}	
}