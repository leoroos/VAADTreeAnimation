package de.lere.vaad.treebuilder;

import java.util.List;

import de.lere.vaad.treebuilder.BinaryTreeModel.Edge;

public class TreeEvent<T extends Comparable<T>> {
	
	public final BinaryTreeModel<T> beforeChange;
	public final BinaryTreeModel<T> afterChange;
	
	public TreeEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after) {
		this.beforeChange = before;
		this.afterChange = after;
	}
}
