package de.lere.vaad.splaytree;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;

public class SplayTreeModel<T extends Comparable<T>> extends BinaryTreeModel<T> {

	public SplayTreeModel() {
		super();
	}

	public static <T extends Comparable<T>> SplayTreeModel<T> from(
			BinaryTreeModel<T> btree) {
		BinaryTreeModel<T> copy = btree.copy();
		SplayTreeModel<T> model = new SplayTreeModel<T>();
		model.init(copy.getRoot());
		return model;
	}

	@Override
	public Node<T> insert(T value) {
		Node<T> insertedNode = super.insert(value);
		splay(insertedNode);
		return insertedNode;
	}

	@Override
	public Node<T> delete(T v) {
		Node<T> deleted = super.delete(v);
		if (deleted != null) {
			splay(deleted.getParent());
		}
		return deleted;
	}

	@Override
	public Node<T> search(T v) {
		Node<T> found = super.search(v);
		splay(found);
		return found;
	}

	private void splay(Node<T> insertedNode) {

	}
}
