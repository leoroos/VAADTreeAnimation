package de.lere.vaad.treebuilder;

import java.util.Scanner;

/**
 * @author leo Represents a Node in a Binary-Tree; contains the parent node of
 *         this node (null if its the root node) and left and right children
 *         (null if there are none)
 */
public class Node {

	private static final String nl = "\n";

	private Node leftNode, rightNode, parent;

	private final Object value;

	private final NodeUID uid;

	public Node(Object v) {
		this.uid = new NodeUID();
		if (v == null)
			throw new IllegalArgumentException("Value must not be null");
		this.value = v;
	}

	@Override
	public String toString() {
		StringBuilder csb = new StringBuilder();

		String left = "LNULL";
		if (this.leftNode != null) {
			left = this.leftNode.toString();
		}
		String right = "RNULL";
		if (this.rightNode != null) {
			right = this.rightNode.toString();
		}
		String indention = "    ";
		csb.append(left).append(nl).append(right);
		Scanner children = new Scanner(csb.toString());
		StringBuffer sb = new StringBuffer();
		sb.append(value).append(nl);
		while (children.hasNextLine()) {
			sb.append(indention).append(children.nextLine()).append(nl);
		}

		return sb.toString();
	}

	public int size() {
		int size = 1;
		if (leftNode != null)
			size += leftNode.size();
		if (rightNode != null)
			size += rightNode.size();

		return size;

	}

	public NodeUID getUid() {
		return this.uid;
	}

	public void setLeft(Node left) {
		this.leftNode = left;
	}

	public void setRight(Node right) {
		this.rightNode = right;
	}

	public Node getLeft() {
		return this.leftNode;
	}

	public Node getRight() {
		return this.rightNode;
	}

	public Object getValue() {
		return value;
	}

	public boolean compareStructure(Node inorderbuild) {
		if (inorderbuild == null)
			return false;
		boolean equals = this.value.equals(inorderbuild.value);
		if (equals) {
			boolean leftEquals = compareNodes(this.leftNode,
					inorderbuild.leftNode);
			if (leftEquals)
				return compareNodes(this.rightNode, inorderbuild.rightNode);
		}
		return false;
	}

	private boolean compareNodes(Node c1, Node c2) {
		if (c1 == null)
			return c2 == null;
		else
			return c1.value.equals(c2.value);
	}
}
