package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;
import algoanim.util.Timing;

public interface GraphWriter<T extends Comparable<T>> {
	void buildGraph(Language language, BinaryTreeModel<T> model,
			BinaryTreeLayout layout);

	void highlightNode(BinaryTreeModel<T> model, Node<T> endNode, Timing now,
			Timing howLong);

	void unhighlightNode(BinaryTreeModel<T> model, Node<T> endNode,
			Timing when, Timing howLong);

	void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing now, Timing howLong);

	void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing when, Timing howLong);

	void hideEdge(BinaryTreeModel<T> model, Node<T> startNode, Node<T> endNode,
			Timing when, Timing howLong);

	void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo, BinaryTreeLayout layout,
			Timing when, Timing howLong);
}
