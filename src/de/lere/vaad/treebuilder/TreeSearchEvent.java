package de.lere.vaad.treebuilder;

public class TreeSearchEvent<T extends Comparable<T>> extends TreeModelChangeEvent<T> {

	public TreeSearchEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after,
			Node<T> nodeOfModification) {
		super(before, after, nodeOfModification);
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
