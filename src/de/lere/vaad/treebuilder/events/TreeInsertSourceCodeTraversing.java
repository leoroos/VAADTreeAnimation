package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;
import edu.umd.cs.findbugs.annotations.NonNull;

public class TreeInsertSourceCodeTraversing<T extends Comparable<T>> extends
		TreeEvent<T> {

	public enum InsertSourceCodePosition {
		Init, //
		/**
		 * Insertion is possible when node becomes null<br>
		 * checks for null -> node maybe null
		 */
		CheckingIfInsertionPossible, //
		TestingIfWhereToFromCurrent, //
		/**
		 * node is new node to look in next step along
		 */
		LookingAlongLeftChild, //
		/**
		 * node is new node to look in next step along
		 */
		LookingAlongRightChild, //
		/**
		 * node null for root else new parent
		 */
		SettingParentForNewCurrentNode, //
		/**
		 * node is value node
		 */
		CheckingIfNewIsRoot, //
		/**
		 * node is value node
		 */
		FinalIsSettingToRoot, //
		/**
		 * node is parent from which node will be inserted left
		 */
		FinalIsSettingAsLeftChildFrom, //
		/**
		 * node is parent from which node will be inserted right
		 */
		FinalIsSettingAsRightChildFrom, //
		/**
		 * node is parent from which will be inserted left or right
		 */
		CheckingIfToSetLeftInFinalStep, //
		;
	}

	public final InsertSourceCodePosition position;
	public final Node<T> currentPosition;
	/**
	 * Depending on context additional node, maybe null
	 */
	public final T insertionValue;
	public final BinaryTreeModel<T> currentModel;

	public TreeInsertSourceCodeTraversing(
			@NonNull InsertSourceCodePosition pos,
			@NonNull BinaryTreeModel<T> currentModel, Node<T> curPos,
			@NonNull T insertionValue) {
		if (pos == null)
			throw new IllegalArgumentException("null not allowed");
		if (currentModel == null)
			throw new IllegalArgumentException("null not allowed");

		this.position = pos;
		this.currentModel = currentModel;
		if (insertionValue == null)
			throw new IllegalArgumentException("null not allowed");
		this.insertionValue = insertionValue;
		if (curPos != null)
			this.currentPosition = curPos.copy();
		else
			this.currentPosition = null;
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
