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

		StringBuffer append = new StringBuffer().append(getClass().getSimpleName()).append("[").append(getUid()).append("[")
				.append(value).append("][").append(left).append("]")
				.append("[").append(right).append("]").append("]");
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

	public boolean compareStructure(Node<?> inorderbuild) {
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

	private boolean compareNodes(Node<?> c1, Node<?> c2) {
		if (c1 == null)
			return c2 == null;
		else {
			return c1.compareStructure(c2);
		}
	}

	public int compareValue(Node<T> node) {
		return this.value.compareTo(node.value);
	}

	public Node<T> insert(T value) {
		if (NodeOrder.isEqualChildConsideredLeft(this.value, value)) {
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

	/**
	 * @return
	 */
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

	public boolean isRightChild() {
		if (!hasParent())
			return false;
		return this.getParent().hasRightChild()
				&& this.getParent().getRight().equals(this);
	}

	public boolean isLeftChild() {
		if (!hasParent())
			return false;
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
	 *         is not the same object. The same is true for it's children. <br>
	 *         The returned node won't copy it's parent, i.e. it's a newly
	 *         rooted tree.
	 */
	public Node<T> copy() {
		Node<T> node = new Node<T>(uid, value);
		// first Node does not carry on its parent to delete old references
		if (hasLeftChild()) {
			node.setLeft(leftNode.copy());
		}
		if (hasRightChild()) {
			node.setRight(rightNode.copy());
		}
		return node;
	}

	/**
	 * The nodes equals implementation simply compares the node uids. it does
	 * not check any further information like value or children.<br>
	 * Node a may be equal to node b although they have different structures,
	 * which can happen after the same node has been copied and changed.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() == this.getClass()) {
			return this.getUid().equals(((Node<?>) obj).getUid());
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return 11 * getUid().hashCode();
	}

	public Node<T> getMaximum() {
		Node<T> node = this;
		while (node.hasRightChild()) {
			node = node.getRight();
		}
		return node;
	}

	public Node<T> getMinimum() {
		Node<T> node = this;
		while (node.hasLeftChild()) {
			node = node.getLeft();
		}
		return node;
	}

	public Node<T> getSuccessor() {
		if (hasRightChild()) {
			Node<T> right = getRight();
			return right.getMinimum();
		} else {
			return null;
		}
	}

	public Node<T> getPredeccesor() {
		if (hasLeftChild()) {
			Node<T> left = getLeft();
			return left.getMaximum();
		} else {
			return null;
		}
	}

	public Node<T> search(T value) {
		int compareTo = this.getValue().compareTo(value);
		if (compareTo == 0) {
			return this;
		} else if (compareTo < 0) {
			if (this.hasRightChild()) {
				return this.getRight().search(value);
			} else {
				return null;
			}
		} else {
			if (this.hasLeftChild()) {
				return this.getLeft().search(value);
			} else {
				return null;
			}
		}
	}

	public int height() {
		int lh = 0;
		if(hasLeftChild()) {
			lh = getLeft().height() + 1 ;
		}
		int rh = 0;
		if(hasRightChild()) {
			rh = getRight().height() +1;
		}
		int max = Math.max(lh, rh);
		return max;
	}

}