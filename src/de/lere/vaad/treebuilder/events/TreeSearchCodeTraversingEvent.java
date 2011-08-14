package de.lere.vaad.treebuilder.events;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import edu.umd.cs.findbugs.annotations.NonNull;

public class TreeSearchCodeTraversingEvent<T extends Comparable<T>> extends
		TreeSourceTraversingEvent<T> {

	public final @NonNull
	SearchTraversingPosition traversingPosition;
	public final @Nullable
	Node<T> currentPos;
	public final @Nonnull
	T searchVal;
	public final @Nonnull
	BinaryTreeModel<T> currentModel;

	public enum SearchTraversingPosition {

		/**
		 * Initialization i.e. node is root or null for empty tree 
		 */
		Init(1), //
		
		/**
		 * current is next to test node, maybe null
		 */
		TestIfGoOnSearching(2), //

		/**
		 * node should not be null in this context, is the current node along
		 * which is checked whether to go on left or right
		 */
		TestIfSearchAlongLeftChild(3), //

		/**
		 * node is the left child, maybe null
		 */
		GoOnSearchingAlongLeftChild(4), //

		/**
		 * node is the right child, maybe null
		 */
		GoOnSearchingAlongRightChild(5), //

		
		/**
		 * The found or not found node for searched value.
		 * node maybe null if no result has been found
		 */
		FinalReturnSearchResult(6),//
		
		;
		private SearchTraversingPosition(int i){
			putEnumPos(this, i);
		}
	}

	
	public TreeSearchCodeTraversingEvent(
			SearchTraversingPosition traversingPosition, BinaryTreeModel<T> model,
			Node<T> currentPos, T searchVal) {
		this.traversingPosition = traversingPosition;
		this.currentPos = currentPos;
		this.searchVal = searchVal;
		this.currentModel = model;
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

	@Override
	public  SearchTraversingPosition getCodePosition() {
		return this.traversingPosition;
	}

}
