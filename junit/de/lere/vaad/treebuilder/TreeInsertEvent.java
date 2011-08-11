package de.lere.vaad.treebuilder;

public class TreeInsertEvent<T extends Comparable<T>> extends TreeModelChangeEvent<T> {

	public TreeInsertEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after,
			Node<T> insertedNode) {
		super(before, after, insertedNode);
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}
}
