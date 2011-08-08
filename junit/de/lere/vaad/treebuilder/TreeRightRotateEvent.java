package de.lere.vaad.treebuilder;

public class TreeRightRotateEvent<T extends Comparable<T>> extends TreeEvent<T> {

	public TreeRightRotateEvent(BinaryTreeModel<T> before,
			BinaryTreeModel<T> after, Node<T> nodeOfModification) {
		super(before, after, nodeOfModification);
	}

	@Override
	public void notifyListener(BinaryTreeModelListener<T> listener) {
		listener.update(this);
	}

}
