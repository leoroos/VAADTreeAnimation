package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;
import de.lere.vaad.treebuilder.BinaryTreeModel.InsertionResult;

public class TreeInsertEvent<T extends Comparable<T>> extends TreeModelChangeEvent<T> {

	public final InsertionResult insertionResult;

	public TreeInsertEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after,
			Node<T> insertedNode, InsertionResult insertionResult) {
		super(before, after, insertedNode);
		this.insertionResult = insertionResult;
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}
}
