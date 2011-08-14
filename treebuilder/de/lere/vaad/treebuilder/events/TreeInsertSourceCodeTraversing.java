package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.events.TreeInsertSourceCodeTraversing.InsertSourceCodePosition;
import edu.umd.cs.findbugs.annotations.NonNull;

public class TreeInsertSourceCodeTraversing<T extends Comparable<T>> extends
		TreeSourceTraversingEvent<T> {

	public enum InsertSourceCodePosition {
		/**
		 * node is root
		 */
		Init(1), //
		/**
		 * Insertion is possible when node becomes null<br>
		 * checks for null -> node maybe null
		 */
		CheckingIfInsertionPossible(2), //
		TestingIfWhereToFromCurrent(3), //
		/**
		 * node is new node to look in next step along
		 */
		LookingAlongLeftChild(4), //
		/**
		 * node is new node to look in next step along
		 */
		LookingAlongRightChild(5), //
		/**
		 * node null for root else new parent
		 */
		SettingParentForNewCurrentNode(6), //
		/**
		 * node is value node
		 */
		CheckingIfNewIsRoot(7), //
		/**
		 * node is value node
		 */
		FinalIsSettingToRoot(8), //
		/**
		 * node is parent from which will be inserted left or right
		 */
		CheckingIfToSetLeftInFinalStep(9), //		
		/**
		 * node is parent from which node will be inserted left
		 */
		FinalIsSettingAsLeftChildFrom(10), //
		/**
		 * node is parent from which node will be inserted right
		 */
		FinalIsSettingAsRightChildFrom(11), //

		;
		
		InsertSourceCodePosition(int i){
			putEnumPos(this, i);
		}
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
		assertNonNull(currentModel);
		assertNonNull(pos);

		this.position = pos;
		this.currentModel = currentModel;
		assertNonNull(insertionValue);
		this.insertionValue = insertionValue;
		if (curPos != null)
			this.currentPosition = curPos.copy();
		else
			this.currentPosition = null;
	}

	private void assertNonNull(Object insertionValue) {
		if (insertionValue == null)
			throw new IllegalArgumentException("null not allowed");
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

	@Override
	public InsertSourceCodePosition getCodePosition() {
		return this.position;
	}

}
