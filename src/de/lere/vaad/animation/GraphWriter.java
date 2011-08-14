package de.lere.vaad.animation;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import algoanim.util.Timing;

/**
 * Supports graph writing methods. When a method is provided with and without timings,
 * the default behavior is to use immediate change.
 * 
 * @author Leo Roos, Rene Hertling
 *
 * @param <T>
 */
public interface GraphWriter<T extends Comparable<T>> {
	void buildGraph(BinaryTreeModel<T> model, Timing delay);
	
	void buildGraph(BinaryTreeModel<T> model);

	void highlightNode(BinaryTreeModel<T> model, Node<T> node, Timing now,
			Timing howLong);
	
	void highlightNode(BinaryTreeModel<T> model, Node<T> node);

	void unhighlightNode(BinaryTreeModel<T> model, Node<T> node,
			Timing when, Timing howLong);

	void unhighlightNode(BinaryTreeModel<T> model, Node<T> node);
	
	
	void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing now, Timing howLong);
	
	void highlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode);

	void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode, Timing when, Timing howLong);
	
	void unhighlightEdge(BinaryTreeModel<T> model, Node<T> startNode,
			Node<T> endNode);

	void hideEdge(BinaryTreeModel<T> model, Node<T> startNode, Node<T> endNode,
			Timing when, Timing howLong);
	
	void hideEdge(BinaryTreeModel<T> model, Node<T> startNode, Node<T> endNode);

	void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo, Timing when,
			Timing howLong);
	
	void translateNodes(BinaryTreeModel<T> initialPosition,
			BinaryTreeModel<T> positionToMoveTo);
	
	void blinkNode(BinaryTreeModel<T> model,
			Node<T> node, Timing when, Timing howLong);

	public abstract void showCurrent(Timing t);

	public abstract void showCurrent();

	public abstract void hideCurrent(Timing t);

	public abstract void hideCurrent();

	public abstract void highlightNode(BinaryTreeModel<T> model, Timing startWhen,
			Timing howLong, Node<T>... nodes);

	void setAutomaticUnhighlightNodes(boolean unhighlight);

	boolean isAutomaticUnhighlight();
}
