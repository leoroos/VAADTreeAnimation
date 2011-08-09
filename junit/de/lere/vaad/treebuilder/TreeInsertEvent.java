package de.lere.vaad.treebuilder;

public class TreeInsertEvent<T extends Comparable<T>> extends TreeEvent<T> {

	public TreeInsertEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after,
			Node<T> insertedNode) {
		super(before, after, insertedNode);
	}

	@Override
	public void notifyListener(BinaryTreeModelListener<T> listener) {
		listener.update(this);
	}
}