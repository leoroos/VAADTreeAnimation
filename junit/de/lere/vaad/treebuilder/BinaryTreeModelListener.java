package de.lere.vaad.treebuilder;

public interface BinaryTreeModelListener<T extends Comparable<T>> {

	public void updateOnInsert(TreeEvent<T> event);
}
