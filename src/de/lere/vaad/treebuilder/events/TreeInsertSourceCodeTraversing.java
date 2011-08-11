package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.TreeEvent;
import de.lere.vaad.treebuilder.TreeModelChangeEvent;
import de.lere.vaad.treebuilder.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;

public class TreeInsertSourceCodeTraversing<T extends Comparable<T>> extends
		TreeEvent<T> {

	public enum InsertSourceCodePosition {
		Init, //
		WhileNoInsertionPossible, //
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

	public TreeInsertSourceCodeTraversing(InsertSourceCodePosition pos,
			Node<T> curPos, T insertionValue) {
		this.position = pos;
		if(insertionValue == null)
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
