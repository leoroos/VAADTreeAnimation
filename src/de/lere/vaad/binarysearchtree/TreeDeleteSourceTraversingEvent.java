package de.lere.vaad.binarysearchtree;

import javax.annotation.Nullable;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeSourceTraversingEvent;

public class TreeDeleteSourceTraversingEvent<T extends Comparable<T>> extends
		TreeSourceTraversingEvent<T> {

	public enum DeleteTraversingPosition {

		/**
		 * node is the deletee could be null, though this would mean illegal
		 * interaction with the model
		 */
		DeleteInit(1), //

		/**
		 * node is the left child, may be null
		 */
		TestIfLeftChildNull(2), //

		/**
		 * node is the right child, i.e. the to replace with
		 */
		NoLeftChild(3), //

		/**
		 * node is the previously right child which has been transplanted
		 */
		FinishAfterTransplantWithRightChild(4), //

		/**
		 * node is the right child, may be null
		 */
		TestIfRightChildNull(5), //

		/**
		 * node is left child, maybe null
		 */
		NoRightChild(6), //

		/**
		 * node is the previously left child which has been transplanted
		 */
		FinishAfterTransplantWithLeftChild(7), //

		/**
		 * node is minimum of previously right child, not null
		 */
		GetMinimumOfRight(8), //

		/**
		 * node is parent of the minimum of the previously right child, i.e. the
		 * successor parent
		 */
		TestIfParentOfMinNotDeletee(9), //

		/**
		 * node is the right child of the successor, should not be null
		 */
		TransplantingSuccessorWithItsRightChild(10), //

		/**
		 * node is deletees right, can not be null
		 */
		SettingDeleteesRightToSuccessorsRightAndSettingNewRightsParent(11), //

		/**
		 * node is successor to transplant with
		 */
		TransplantingDeleteeWithSuccessor(12), //

		/**
		 * node is deletees left, can not be null
		 */
		SettingSuccessorLeftWithDeleteeLeftAndSettingsNewLeftsParent(13), //

		/**
		 * node is the old node to transplant with other <br>
		 * optional is the node that will be transplanted
		 */
		StartingTransplant(14), //

		/**
		 * node is old node that will be transplanted.
		 */
		TestTransplantIfOldHasParent(15), //

		/**
		 * node is root optinoal is node to transplant with
		 */
		TransplantReplacesRoot(16), //

		/**
		 * node is old node
		 */
		TestIfOldWasLeftChild(17), //

		/**
		 * node is parent of old, not null, since old was not root
		 * optional is replacement
		 */
		SettingNewNodeAsLeftToParentOfOldNode(18), //

		/**
		 * node is parent of old node, not null since old was not root when here
		 * optional is replacement
		 */
		SettingNewNodeAsRightToParentOfOldNode(19), //

		/**
		 * node is parent of old node, may be null if old was root<br>
		 * optional is successor node, may be null
		 * 
		 */
		TransplantSetsParentOfOldToNew(20), //
		;

		private DeleteTraversingPosition(int i) {
			putEnumPos(this, i);
		}
	}

	public final T delVal;
	public final DeleteTraversingPosition traversingPosition;
	public final BinaryTreeModel<T> model;
	public final @Nullable
	Node<T> currentPosition;
	public @Nullable
	Node<T> optional;

	public TreeDeleteSourceTraversingEvent(BinaryTreeModel<T> currentModel,
			Node<T> node, DeleteTraversingPosition travPos, T deleteValue) {
		model = currentModel.copy();
		if (node != null)
			node = node.copy();
		currentPosition = node;
		delVal = deleteValue;
		traversingPosition = travPos;
	}

	public TreeDeleteSourceTraversingEvent(BinaryTreeModel<T> currentModel,
			Node<T> node, DeleteTraversingPosition travPos, T deleteValue,
			Node<T> optional) {
		this(currentModel, node, travPos, deleteValue);
		if (optional != null)
			optional.copy();
		this.optional = optional;
	}

	@Override
	public DeleteTraversingPosition getCodePosition() {
		return this.traversingPosition;
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
