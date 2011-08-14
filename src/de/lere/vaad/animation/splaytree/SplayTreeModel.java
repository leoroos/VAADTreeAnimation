package de.lere.vaad.animation.splaytree;

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

	private void splay(Node<T> x) {
		if (x == null) {
			return;
		}
		Node<T> p = x.getParent();
		if (x.equals(this.getRoot())) {
			return;
		} else if (p.equals(this.getRoot())) {
			if (x.isLeftChild()) {
				rightRotate(this.getRoot());
			} else {
				leftRotate(this.getRoot());
			}
		} else {
			Node<T> g = p.getParent();
			if (x.equals(p.getLeft()) && p.equals(g.getLeft())) {
				rightRotate(g);
				rightRotate(p);
			} else if (x.equals(p.getRight()) && p.equals(g.getRight())) {
				leftRotate(g);
				leftRotate(p);
			} else if (x.equals(p.getRight()) && p.equals(g.getLeft())) {
				leftRotate(p);
				rightRotate(g);
			} else if (x.equals(p.getLeft()) && p.equals(g.getRight())) {
				rightRotate(p);
				leftRotate(g);
			}
		}
		splay(x);
	}

}
