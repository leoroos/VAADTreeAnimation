package de.lere.vaad.treebuilder;

import java.util.Scanner;

/**
 * @author Leo Roos, Rene Hertling Represents a Node in a Binary-Tree; contains
 *         the parent node of this node (null if its the root node) and left and
 *         right children (null if there are none)
 */
public class Node<T extends Comparable<T>> {

	private Node<T> leftNode, rightNode, parent;

	private final T value;

	private final NodeUID uid;

	public Node(T v) {
		this(new NodeUID(), v);
	}

	private Node(NodeUID uid, T v) {
		this.uid = uid;
		if (v == null)
			throw new IllegalArgumentException("Value must not be null");
		this.value = v;
	}

	@Override
	public String toString() {

		String left = "NIL";
		if (hasLeftChild()) {
			left = "LC[" + getLeft().getValue().toString() + "]";
		}
		String right = "NIL";
		if (hasRightChild()) {
			right = "RC[" + getRight().getValue().toString() + "]";
		}

		StringBuffer append = new StringBuffer().append(getUid()).append("[")
				.append(value).append("][").append(left).append("]")
				.append("[").append(right).append("]");
		return append.toString();
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

	public void setLeft(Node<T> left) {
		this.leftNode = left;
		if (hasLeftChild()) {
			this.leftNode.setParent(this);
		}
	}

	public void setRight(Node<T> right) {
		this.rightNode = right;
		if (hasRightChild()) {
			this.rightNode.setParent(this);
		}
	}

	protected void setParent(Node<T> node) {
		this.parent = node;
	}

	public Node<T> getParent() {
		return parent;
	}

	public Node<T> getLeft() {
		return this.leftNode;
	}

	public Node<T> getRight() {
		return this.rightNode;
	}

	public T getValue() {
		return value;
	}

	public boolean compareStructure(Node<T> inorderbuild) {
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

	private boolean compareNodes(Node<T> c1, Node<T> c2) {
		if (c1 == null)
			return c2 == null;
		else{
			return c1.compareStructure(c2);
		}
	}

	public int compareValue(Node<T> node) {
		return this.value.compareTo(node.value);
	}

	public Node<T> insert(T value) {
		if (NodeOrder.isLeftChild(this.value, value)) {
			if (this.hasLeftChild()) {
				return this.getLeft().insert(value);
			} else {
				Node<T> node = new Node<T>(value);
				this.setLeft(node);
				return node;
			}
		} else {
			if (this.hasRightChild()) {
				return this.getRight().insert(value);
			} else {
				Node<T> node = new Node<T>(value);
				this.setRight(node);
				return node;
			}
		}
	}

	public boolean hasRightChild() {
		return this.rightNode != null;
	}

	public boolean hasLeftChild() {
		return this.leftNode != null;
	}

	public int getPosition() {
		if (this.hasParent()) {

			if (isLeftChild()) {
				return (this.getParent().getPosition() * 2);
			} else if (isRightChild()) {
				return (this.getParent().getPosition() * 2) + 1;
			} else {
				throw new IllegalStateException(
						"Node must either be left or right child of its parent if it has a parent.");
			}
		} else {
			return 1;
		}
	}

	private boolean isRightChild() {
		return this.getParent().hasRightChild()
				&& this.getParent().getRight().equals(this);
	}

	private boolean isLeftChild() {
		return this.getParent().hasLeftChild()
				&& this.getParent().getLeft().equals(this);
	}

	public boolean hasParent() {
		return this.getParent() != null;
	}

	public static final String nl = "\n";

	public String structureToString() {

		StringBuilder csb = new StringBuilder();

		String left = "LNIL";
		if (hasLeftChild()) {
			left = getLeft().structureToString();
		}
		String right = "RNIL";
		if (hasRightChild()) {
			right = getRight().structureToString();
		}
		String indention = "    ";
		csb.append(left).append(nl).append(right);
		Scanner children = new Scanner(csb.toString());
		StringBuffer sb = new StringBuffer();
		sb.append(this.getValue()).append(nl);
		while (children.hasNextLine()) {
			sb.append(indention).append(children.nextLine()).append(nl);
		}
		return sb.toString();
	}

	/**
	 * @return a recursively copied node. I.e. it has the same Value and UID but
	 *         is not the same object. The same is true for it's children and
	 *         it's parent.
	 */
	public Node<T> copy() {
		Node<T> node = new Node<T>(uid, value);
		//first Node does not carry on its parent to delete old references
		if (hasLeftChild()) {
			node.setLeft(leftNode.copy());
		}
		if (hasRightChild()) {
			node.setRight(rightNode.copy());
		}
		return node;
	}

}
