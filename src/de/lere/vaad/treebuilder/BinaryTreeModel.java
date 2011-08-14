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
import static de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition.FinalReturnSearchResult;
import static de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition.GoOnSearchingAlongLeftChild;
import static de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition.GoOnSearchingAlongRightChild;
import static de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition.TestIfGoOnSearching;
import static de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition.TestIfSearchAlongLeftChild;

import static de.lere.vaad.binarysearchtree.TreeDeleteSourceTraversingEvent.DeleteTraversingPosition.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.annotation.Nullable;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.omg.PortableInterceptor.SUCCESSFUL;

import de.lere.vaad.binarysearchtree.TreeDeleteSourceTraversingEvent;
import de.lere.vaad.binarysearchtree.TreeDeleteSourceTraversingEvent.DeleteTraversingPosition;
import de.lere.vaad.treebuilder.events.TreeDeleteEvent;
import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeHideEvent;
import de.lere.vaad.treebuilder.events.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing;
import de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;
import de.lere.vaad.treebuilder.events.TreeSearchCodeTraversingEvent.SearchTraversingPosition;
import de.lere.vaad.treebuilder.events.TreeLeftRotateEvent;
import de.lere.vaad.treebuilder.events.TreeRightRotateEvent;
import de.lere.vaad.treebuilder.events.TreeSearchEvent;
import de.lere.vaad.treebuilder.events.TreeShowEvent;
import de.lere.vaad.treebuilder.events.TreeVisibilityEvent;

/**
 * @author Leo Roos, Rene Hertling Represents the logical structure of a binary
 *         tree. Contains the root and convience-method for child-node access.
 */
public class BinaryTreeModel<T extends Comparable<T>> {

	public static class DeleteStatisticResult extends StatisticResult {
		public int transplantations = 0;
	}

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
	public static class StatisticResult {

		public int numberOfComparisons = 0;
		public boolean successful = false;
	}

	public Node<T> insert(T value) {
		StatisticResult insertionResult = new StatisticResult();
		BinaryTreeModel<T> before = this.copy();
		Node<T> y = null;
		Node<T> x = getRoot();
		fireTreeInsertSource(Init, x, value);
		while (!ifIsNullIncrCompsFireEvent(x, insertionResult, value,
				CheckingIfInsertionPossible)) {
			y = x;
			fireTreeInsertSource(TestingIfWhereToFromCurrent, x, value);
			insertionResult.numberOfComparisons++;
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

		/*
		 * Since duplicates allowed always true
		 */
		insertionResult.successful = true;
		fireChange(new TreeInsertEvent<T>(before, this.copy(), finalToInsert,
				insertionResult));
		return finalToInsert;
	}

	private boolean ifIsNullIncrCompsFireEvent(Node<T> nullCheckOn,
			StatisticResult insertionResult, T value,
			InsertSourceCodePosition eventToFire) {
		insertionResult.numberOfComparisons++;
		fireTreeInsertSource(eventToFire, nullCheckOn, value);
		return nullCheckOn == null;
	}

	private boolean checkWhetherLeftChildAndFireAccording(
			InsertSourceCodePosition pos, Node<T> current, T value,
			StatisticResult insertionResult) {
		boolean leftChild = NodeOrder.isEqualChildConsideredLeft(
				current.getValue(), value);
		insertionResult.numberOfComparisons++;
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
		deleted = nodeToDelete;
		successor = delete(nodeToDelete);
		BinaryTreeModel<T> current = copy();
		fireChange(new TreeDeleteEvent<T>(old, current, deleted, successor,
				currentDeleteStatistics, v));
		return deleted;
	}

	private DeleteStatisticResult currentDeleteStatistics;

	private void transplant(Node<T> old, Node<T> newnode) {
		currentDeleteStatistics.transplantations++;
		fireDeleteTraversing(StartingTransplant, old, newnode);
		if (fireDeleteTraversing(TestTransplantIfOldHasParent, old,
				!old.hasParent())) {
			fireDeleteTraversing(TransplantReplacesRoot, root, newnode);
			root = newnode;
			if (newnode != null) {
				newnode.setParent(null);
			}
		} else if (fireDeleteTraversing(TestIfOldWasLeftChild, old,
				old.isLeftChild())) {
			fireDeleteTraversing(SettingNewNodeAsLeftToParentOfOldNode,
					old.getParent());
			old.getParent().setLeft(newnode);
		} else if (fireDeleteTraversing(TestIfOldWasRightChild, old,
				old.isRightChild())) {
			fireDeleteTraversing(SettingNewNodeAsRightToParentOfOldNode,
					old.getParent());
			old.getParent().setRight(newnode);
		} else {
			throw new IllegalStateException();
		}
		fireDeleteTraversing(TransplantSetsParentOfOldToNew, old.getParent(),
				newnode, true /* to conform the algorithm */);
	}

	private boolean fireDeleteTraversing(
			DeleteTraversingPosition transplantsetsparentofoldtonew,
			Node<T> one, Node<T> two, boolean b) {
		this.currentDeleteStatistics.numberOfComparisons++;
		fireDeleteTraversing(transplantsetsparentofoldtonew, one, two);
		return b;
	}

	private void fireDeleteTraversing(
			DeleteTraversingPosition traversingPosition, Node<T> one,
			Node<T> two) {
		fireChange(new TreeDeleteSourceTraversingEvent<T>(this.copy(), one,
				traversingPosition, currentDelVal, two));
	}

	private @Nullable
	T currentDelVal;

	/**
	 * @param deletee
	 *            node to delete
	 * @return the node with which the deleted node has been replaced. Maybe
	 *         <code>null</code> for root or leaf.
	 */
	public Node<T> delete(Node<T> deletee) {
		currentDeleteStatistics = new DeleteStatisticResult();
		if (deletee == null) {
			currentDeleteStatistics.successful = false;
			return null;
		} else {
			currentDeleteStatistics.successful = true;
		}
		currentDelVal = deletee.getValue();
		fireDeleteTraversing(DeleteInit, deletee);
		if (fireDeleteTraversing(TestIfLeftChildNull, deletee.getLeft(),
				!deletee.hasLeftChild())) {
			fireDeleteTraversing(NoLeftChild, deletee.getRight());
			Node<T> transplantwith = deletee.getRight();
			transplant(deletee, transplantwith);
			fireDeleteTraversing(FinishAfterTransplantWithRightChild,
					transplantwith);
			return deletee.getRight();
		} else if (fireDeleteTraversing(TestIfRightChildNull,
				deletee.getRight(), !deletee.hasRightChild())) {
			fireDeleteTraversing(NoRightChild, deletee.getLeft());
			Node<T> transplantwith = deletee.getLeft();
			transplant(deletee, transplantwith);
			fireDeleteTraversing(FinishAfterTransplantWithLeftChild,
					transplantwith);
			return deletee.getLeft();
		} else {
			Node<T> successor = deletee.getRight().getMinimum();
			fireDeleteTraversing(GetMinimumOfRight, successor);
			if (fireDeleteTraversing(TestIfParentOfMinNotDeletee,
					successor.getParent(),
					!successor.getParent().equals(deletee))) {
				fireDeleteTraversing(TransplantingSuccessorWithItsRightChild,
						successor.getRight());
				transplant(successor, successor.getRight());
				fireDeleteTraversing(
						SettingDeleteesRightToSuccessorsRightAndSettingNewRightsParent,
						deletee.getRight());
				successor.setRight(deletee.getRight());
			}
			fireDeleteTraversing(TransplantingDeleteeWithSuccessor, successor);
			transplant(deletee, successor);
			fireDeleteTraversing(
					SettingSuccessorLeftWithDeleteeLeftAndSettingsNewLeftsParent,
					deletee.getLeft());
			successor.setLeft(deletee.getLeft());
			return successor;
		}
	}

	private boolean fireDeleteTraversing(
			TreeDeleteSourceTraversingEvent.DeleteTraversingPosition traversingPosition,
			Node<T> currentPos, boolean b) {
		return fireDeleteTraversing(traversingPosition, currentPos, null, b);
	}

	private void fireDeleteTraversing(
			DeleteTraversingPosition traversingPosition, Node<T> currentPos) {
		fireDeleteTraversing(traversingPosition, currentPos, null);
	}

	private StatisticResult runningSearchStatistics;

	public Node<T> search(T k) {
		this.runningSearchStatistics = new StatisticResult();
		Node<T> x = getRoot();
		fireSearchTraversing(SearchTraversingPosition.Init, x, k);
		while (fireSearchTraversing(TestIfGoOnSearching, x, k,
				(x != null && k != x.getValue()))) {
			if (fireSearchTraversing(TestIfSearchAlongLeftChild, x, k,
					NodeOrder.isEqualChildConsideredLeft(x.getValue(), k))) {
				x = x.getLeft();
				fireSearchTraversing(GoOnSearchingAlongLeftChild, x, k);
			} else {
				x = x.getRight();
				fireSearchTraversing(GoOnSearchingAlongRightChild, x, k);
			}
		}
		if (x != null)
			runningSearchStatistics.successful = true;
		fireSearchTraversing(FinalReturnSearchResult, x, k);
		fireChange(new TreeSearchEvent<T>(this.copy(), this.copy(), x,
				this.runningSearchStatistics, k));
		return x;
	}

	private boolean fireSearchTraversing(
			SearchTraversingPosition traversingPosition, Node<T> currentPos,
			T searchVal, boolean b) {
		this.runningSearchStatistics.numberOfComparisons++;
		fireSearchTraversing(traversingPosition, currentPos, searchVal);
		return b;
	}

	private void fireSearchTraversing(
			SearchTraversingPosition traversingPosition, Node<T> currentPos,
			T searchVal) {
		if (currentPos != null)
			currentPos = currentPos.copy();
		fireChange(new TreeSearchCodeTraversingEvent<T>(traversingPosition,
				this.copy(), currentPos, searchVal));
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

	public Node<T> getNodeByID(Node<T> node) {
		List<Node<T>> nodes = getNodesInOrder();
		int index = nodes.indexOf(node);
		if (index < 0) {
			return null;
		}
		return nodes.get(index);
	}

	// FIXME is this still relevant?
	/**
	 * @param <T>
	 * @param model
	 * @param node
	 * @return try using {@link #lookupNodeByID(BinaryTreeModel, Node)} instead
	 */
	@Deprecated
	public static <T extends Comparable<T>> Node<T> lookupNodeByID(
			BinaryTreeModel<T> model, Node<T> node) {
		List<Node<T>> nodes = model.getNodesInOrder();
		int index = nodes.indexOf(node);
		if (index < 0) {
			return null;
		}
		return nodes.get(index);
	}

	public void hide() {
		fireVisibilityChange(new TreeHideEvent<T>(this.copy()));
	}

	private void fireVisibilityChange(TreeVisibilityEvent<T> event) {
		fireChange(event);
	}

	public void show() {
		fireVisibilityChange(new TreeShowEvent<T>(this.copy()));
	}

	/**
	 * @return height of the root or -1 if tree is empty
	 */
	public int height() {
		if (root == null) {
			return -1;
		} else {
			return root.height();
		}
	}
}
