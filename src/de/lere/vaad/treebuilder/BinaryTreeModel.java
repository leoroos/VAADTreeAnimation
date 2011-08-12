package de.lere.vaad.treebuilder;

import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.CheckingIfInsertionPossible;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.CheckingIfNewIsRoot;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.CheckingIfToSetLeftInFinalStep;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.FinalIsSettingAsLeftChildFrom;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.FinalIsSettingAsRightChildFrom;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.FinalIsSettingToRoot;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.Init;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.LookingAlongLeftChild;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.LookingAlongRightChild;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.SettingParentForNewCurrentNode;
import static de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition.TestingIfWhereToFromCurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang.builder.HashCodeBuilder;

import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;

/**
 * @author Leo Roos, Rene Hertling Represents the logical structure of a binary
 *         tree. Contains the root and convience-method for child-node access.
 */
public class BinaryTreeModel<T extends Comparable<T>> {

	private Node<T> root;

	private List<TreeEventListener<T>> listeners;

	public BinaryTreeModel() {
		this.listeners = new ArrayList<TreeEventListener<T>>();
	}

	public void addListener(TreeEventListener<T> listener) {
		listeners.add(listener);
	}

	public boolean removeListener(TreeEventListener<T> listener) {
		return listeners.remove(listener);
	}

	public Node<T> getRoot() {
		return root;
	}

	public int size() {
		if (root == null) {
			return 0;
		}
		return root.size();
	}

	public boolean isEmpty() {
		return size() == 0;
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

	/**
	 * @return a list of nodes. It is guaranteed that every call to the model
	 *         containing the same Tree will return a list of nodes in the same
	 *         order
	 */
	public List<Node<T>> getNodesInOrder() {
		List<Node<T>> nodes = new ArrayList<Node<T>>();
		listNodesInOrder(nodes, root);
		return nodes;
	}

	private void listNodesInOrder(List<Node<T>> nodes, Node<T> node) {
		if (node != null) {
			listNodesInOrder(nodes, node.getLeft());
			nodes.add(node);
			listNodesInOrder(nodes, node.getRight());
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

	/**
	 * carries information of interest about the insertion algorithm
	 * 
	 * @author Leo Roos, Rene Hertling
	 * 
	 */
	public class InsertionResult {

		public int numOfComparisons = 0;

	}

	public Node<T> insert(T value) {
		InsertionResult insertionResult = new InsertionResult();
		BinaryTreeModel<T> before = this.copy();
		Node<T> y = null;
		Node<T> x = getRoot();
		fireTreeInsertSource(Init, x, value);
		while (!ifIsNullIncrCompsFireEvent(x, insertionResult, value,
				CheckingIfInsertionPossible)) {
			y = x;
			fireTreeInsertSource(TestingIfWhereToFromCurrent, x, value);
			insertionResult.numOfComparisons++;
			if (NodeOrder.isEqualChildConsideredLeft(x.getValue(), value)) {
				x = x.getLeft();
				fireTreeInsertSource(LookingAlongLeftChild, x, value);
			} else {
				x = x.getRight();
				fireTreeInsertSource(LookingAlongRightChild, x, value);
			}
		}
		fireTreeInsertSource(SettingParentForNewCurrentNode, y, value);
		Node<T> finalToInsert = new Node<T>(value);
		finalToInsert.setParent(y);
		if (ifIsNullIncrCompsFireEvent(y, insertionResult, value,
				CheckingIfNewIsRoot)) {
			this.root = finalToInsert;
			fireTreeInsertSource(FinalIsSettingToRoot, finalToInsert, value);
		} else if (checkWhetherLeftChildAndFireAccording(
				CheckingIfToSetLeftInFinalStep, y, value, insertionResult)) {
			fireTreeInsertSource(FinalIsSettingAsLeftChildFrom, y, value);
			y.setLeft(finalToInsert);
		} else {
			fireTreeInsertSource(FinalIsSettingAsRightChildFrom, finalToInsert,
					value);
			y.setRight(finalToInsert);
		}

		fireChange(new TreeInsertEvent<T>(before, this.copy(), finalToInsert,
				insertionResult));
		return finalToInsert;
	}

	private boolean ifIsNullIncrCompsFireEvent(Node<T> nullCheckOn,
			InsertionResult insertionResult, T value,
			InsertSourceCodePosition eventToFire) {
		insertionResult.numOfComparisons++;
		fireTreeInsertSource(eventToFire, nullCheckOn, value);
		return nullCheckOn == null;
	}

	private boolean checkWhetherLeftChildAndFireAccording(
			InsertSourceCodePosition pos, Node<T> current, T value,
			InsertionResult insertionResult) {
		boolean leftChild = NodeOrder.isEqualChildConsideredLeft(
				current.getValue(), value);
		insertionResult.numOfComparisons++;
		fireTreeInsertSource(pos, current, value);
		return leftChild;
	}

	private void fireTreeInsertSource(InsertSourceCodePosition pos, Node<T> x,
			T value) {
		fireChange(new TreeInsertSourceCodeTraversing<T>(pos, this.copy(), x,
				value));
	}

	void fireChange(TreeEvent<T> event) {
		for (TreeEventListener<T> l : listeners) {
			event.notifyListener(l);
		}
	}

	public static class Edge<T extends Comparable<T>> {
		Edge(Node<T> parentPosition, Node<T> childPosition) {
			parentPos = parentPosition;
			childPos = childPosition;
		}

		public final Node<T> parentPos;
		public final Node<T> childPos;
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

				if (nextNode.hasRightChild()) {
					Node<T> right = nextNode.getRight();
					toWork.add(right);
					elist.add(new Edge<T>(nextNode, right));
				}
			}
			return elist;
		}
	}

	public int[][] getAdjancencyMatrix() {
		int size = size();
		int[][] matrix = new int[size][size];
		List<Edge<T>> edgeList = getEdgeList();
		for (Edge<T> edge : edgeList) {
			int parentLocation = edge.parentPos.getPosition() - 1;
			int childLocation = edge.childPos.getPosition() - 1;
			matrix[parentLocation][childLocation] = 1;
		}
		return matrix;
	}

	@Override
	public String toString() {
		if (root == null) {
			return "EmptyTree";
		} else {
			return root.structureToString();
		}
	}

	/**
	 * Provides a copy of this BinaryTreeModel. Only copies the logical
	 * structure of the tree. Listeners are not copied. the resulting tree is a
	 * deep copy of the original tree (see {@link Node#copy()}).
	 * 
	 * @return a copy of this {@link BinaryTreeModel}
	 */
	public BinaryTreeModel<T> copy() {
		BinaryTreeModel<T> copy = new BinaryTreeModel<T>();
		Node<T> copiedRoot = null;
		if (root != null) {
			copiedRoot = root.copy();
		}
		return copy.init(copiedRoot);
	}

	/**
	 * Only compares the structure of the root nodes of the two models.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == this.getClass()) {
			BinaryTreeModel<?> other = (BinaryTreeModel<?>) obj;
			Node<T> root = this.root;
			boolean rootsEqual = false;
			if (root == null) {
				rootsEqual = other.root == null;
			} else {
				rootsEqual = this.root.compareStructure(other.root);
			}
			return rootsEqual;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	private boolean hasEqualListener(BinaryTreeModel<?> other) {
		if (this.listeners.size() != other.listeners.size()) {
			return false;
		}

		boolean thiscontainsAll = this.listeners.containsAll(other.listeners);
		if (thiscontainsAll) {
			if (other.listeners.containsAll(this.listeners)) {
				return true;
			}
		}

		return true;
	}

	public static <T extends Comparable<T>> BinaryTreeModel<T> createTreeByInsert(
			List<T> args) {
		BinaryTreeModel<T> binaryTreeModel = new BinaryTreeModel<T>();
		for (T t : args) {
			binaryTreeModel.insert(t);
		}
		return binaryTreeModel;
	}

	public static <T extends Comparable<T>> BinaryTreeModel<T> createTreeByInsert(
			T... args) {
		return createTreeByInsert(Arrays.asList(args));
	}

	/**
	 * @param v
	 *            the value for which a node has to be deleted next. The first
	 *            node with the given value will be returned.
	 * @return the deleted node or <code>null</code>
	 */
	public Node<T> delete(T v) {
		BinaryTreeModel<T> old = copy();
		Node<T> nodeToDelete = internalSearch(v);
		Node<T> deleted = null;
		Node<T> successor = null;
		if (nodeToDelete != null) {
			deleted = nodeToDelete;
			successor = delete(nodeToDelete);
		}
		BinaryTreeModel<T> current = copy();
		fireChange(new TreeDeleteEvent<T>(old, current, deleted, successor));
		return deleted;
	}

	private void transplant(Node<T> old, Node<T> newnode) {
		if (!old.hasParent()) {
			root = newnode;
			if (newnode != null) {
				newnode.setParent(null);
			}
		} else if (old.isLeftChild()) {
			old.getParent().setLeft(newnode);
		} else if (old.isRightChild()) {
			old.getParent().setRight(newnode);
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * @param deletee
	 *            node to delete
	 * @return the node with which the deleted node has been replaced. Maybe
	 *         <code>null</code> for root or leaf.
	 */
	Node<T> delete(Node<T> deletee) {
		if (!deletee.hasLeftChild()) {
			transplant(deletee, deletee.getRight());
			return deletee.getRight();
		} else if (!deletee.hasRightChild()) {
			transplant(deletee, deletee.getLeft());
			return deletee.getLeft();
		} else {
			Node<T> successor = deletee.getRight().getMinimum();
			if (!successor.getParent().equals(deletee)) {
				transplant(successor, successor.getRight());
				successor.setRight(deletee.getRight());
			}
			transplant(deletee, successor);
			successor.setLeft(deletee.getLeft());
			return successor;
		}
	}

	public Node<T> search(T v) {
		Node<T> found = internalSearch(v);
		fireChange(new TreeSearchEvent<T>(this.copy(), this.copy(), found));
		return found;
	}

	private Node<T> internalSearch(T v) {
		Node<T> found;
		if (this.root == null) {
			found = null;
		} else {
			found = root.search(v);
		}
		return found;
	}

	/**
	 * the root of the rotated subtree. The will usually be different to
	 * <code>x</code>, but may be the same if the rotation is performed on a
	 * leaf
	 * 
	 * @param x
	 *            the node to rotate around
	 * @return the root of the rotated subtree which is defined by x.
	 */
	public Node<T> leftRotate(Node<T> x) {
		BinaryTreeModel<T> before = this.copy();
		Node<T> nodeOfInterest;
		if (x.hasRightChild()) {
			nodeOfInterest = doLeftRotate(x);
		} else {
			nodeOfInterest = x;
		}
		fireChange(new TreeLeftRotateEvent<T>(before, this.copy(),
				nodeOfInterest));
		return nodeOfInterest;
	}

	private Node<T> doLeftRotate(Node<T> x) {
		Node<T> y = x.getRight();
		x.setRight(y.getLeft());
		if (y.hasLeftChild()) {
			y.getLeft().setParent(x);
		}
		y.setParent(x.getParent());
		if (!x.hasParent()) {
			this.root = y;
		} else if (x.equals(x.getParent().getLeft())) {
			x.getParent().setLeft(y);
		} else {
			x.getParent().setRight(y);
		}
		y.setLeft(x);
		return y;
	}

	public Node<T> rightRotate(Node<T> x) {
		BinaryTreeModel<T> before = this.copy();
		Node<T> nodeOfInterest;
		if (x.hasLeftChild()) {
			nodeOfInterest = doRightRotate(x);
		} else {
			nodeOfInterest = x;
		}
		fireChange(new TreeRightRotateEvent<T>(before, this.copy(),
				nodeOfInterest));
		return nodeOfInterest;
	}

	private Node<T> doRightRotate(Node<T> x) {
		Node<T> y = x.getLeft();
		x.setLeft(y.getRight());
		if (y.hasRightChild()) {
			y.getRight().setParent(x);
		}
		y.setParent(x.getParent());
		if (!x.hasParent()) {
			this.root = y;
		} else if (x.equals(x.getParent().getRight())) {
			x.getParent().setRight(y);
		} else {
			x.getParent().setLeft(y);
		}
		y.setRight(x);
		return y;
	}

	// FIXME is this still relevant?
	public static <T extends Comparable<T>> Node<T> lookupNodeByID(
			BinaryTreeModel<T> model, Node<T> node) {
		List<Node<T>> nodes = model.getNodesInOrder();
		int index = nodes.indexOf(node);
		if (index < 0) {
			return null;
		}
		return nodes.get(index);
	}
}
