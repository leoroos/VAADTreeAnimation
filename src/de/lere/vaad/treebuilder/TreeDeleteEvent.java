package de.lere.vaad.treebuilder;

public class TreeDeleteEvent<T extends Comparable<T>> extends TreeEvent<T> {

	public TreeDeleteEvent(BinaryTreeModel<T> old, BinaryTreeModel<T> current,
			Node<T> deleted) {
		super(old, current, deleted);
	}

	@Override
	public void notifyListener(BinaryTreeModelListener<T> listener) {
		listener.update(this);		
	}
}
