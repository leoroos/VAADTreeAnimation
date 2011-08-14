package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;

public class TreeRightRotateEvent<T extends Comparable<T>> extends TreeModelChangeEvent<T> {

	public TreeRightRotateEvent(BinaryTreeModel<T> before,
			BinaryTreeModel<T> after, Node<T> nodeOfModification) {
		super(before, after, nodeOfModification);
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
