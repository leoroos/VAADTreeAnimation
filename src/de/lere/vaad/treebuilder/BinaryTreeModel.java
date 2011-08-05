package de.lere.vaad.treebuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author leo Represents the logical structure of a binary tree. Contains the
 *         root and convience-method for child-node access.
 */
public class BinaryTreeModel<T extends Comparable<T>> {

	private Node<T> root;

	public Node<T> getRoot() {
		return root;
	}

	public int size() {
		if (root == null)
			return 0;
		return root.size();
	}

	/**
	 * Initializes this tree model with the passed tree structure. The passed
	 * node is interpreted as root node.
	 * 
	 * @param rootnode
	 * @return
	 */
	public BinaryTreeModel<T> init(Node<T> rootNode) {
		this.root = rootNode;
		return this;
	}

	public List<Node<T>> getNodes() {
		List<Node<T>> nodes = new ArrayList<Node<T>>();
		listNodes(nodes, root);
		return nodes;
	}

	private void listNodes(List<Node<T>> nodes, Node<T> node) {
		if (node != null) {
			listNodes(nodes, node.getLeft());
			nodes.add(node);
			listNodes(nodes, node.getRight());
		}
	}

	public int getNodePosition(Node<T> node) {
		Node<T> current = root;
		int position = 1;
		while (current != null) {
			int compareValue = current.compareValue(node);
			if (compareValue > 0) // current is greater than node
			{
				current = current.getLeft();
				position = position * 2;
			} else if (compareValue < 0) // current is smaller than node
			{
				current = current.getRight();
				position = position * 2 + 1;
			} else // current equals node
			{
				return position;
			}
		}
		return -1;
	}

	public void insert(T value) {
		if (root == null) {
			root = new Node<T>(value);
		} else {
			root.insert(value);
		}
	}

	public static class Edge<T extends Comparable<T>> {
		Edge(Node<T> parentPosition, Node<T> childPosition) {
			parentPos = parentPosition;
			childPos = childPosition;
		}

		final Node<T> parentPos;
		final Node<T> childPos;
	}

	public List<Edge<T>> getEdgeList() {
		if (this.root == null) {
			return Collections.emptyList();
		} else {
			Queue<Node<T>> toWork = new LinkedList<Node<T>>();
			toWork.add(root);
			LinkedList<Edge<T>> elist = new LinkedList<Edge<T>>();
			while (!toWork.isEmpty()) {

				Node<T> nextNode = toWork.remove();
				if (nextNode.hasLeftChild()) {
					Node<T> left = nextNode.getLeft();
					toWork.add(left);
					elist.add(new Edge<T>(nextNode, left));
				}
				
				if (nextNode.hasRightChild()){
					Node<T> right = nextNode.getRight();
					toWork.add(right);
					elist.add(new Edge<T>(nextNode, right));
				}
			}
			return elist;
		}
	}
}
