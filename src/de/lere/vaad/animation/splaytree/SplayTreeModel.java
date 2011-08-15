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
		splayWithEvents(insertedNode);
		return insertedNode;
	}

	@Override
	public Node<T> delete(T v) {
		Node<T> deleted = super.delete(v);
		if (deleted != null) {
			splayWithEvents(deleted.getParent());
		}
		return deleted;
	}

	@Override
	public Node<T> search(T v) {
		Node<T> found = super.search(v);
		splayWithEvents(found);
		return found;
	}

	private void splayWithEvents(Node<T> x) {
		this.fireChange(new SplayStartedEvent<T>());
		splay(x);
		this.fireChange(new SplayEndedEvent<T>());
	}

	private void splay(Node<T> x) {
		if (x == null) {
			return;
		}
		Node<T> p = x.getParent();
		if (x.equals(this.getRoot())) {
			return;
		} else if (p.equals(this.getRoot())) {
			fireChange(new ZigStartedEvent<T>());
			if (x.isLeftChild()) {
				rightRotate(this.getRoot());
			} else {
				leftRotate(this.getRoot());
			}
			fireChange(new ZigEndedEvent<T>());
		} else {
			Node<T> g = p.getParent();
			if (x.equals(p.getLeft()) && p.equals(g.getLeft())) {
				fireChange(new ZigZigStartedEvent<T>());
				rightRotate(g);
				rightRotate(p);
				fireChange(new ZigZigEndedEvent<T>());
			} else if (x.equals(p.getRight()) && p.equals(g.getRight())) {
				fireChange(new ZigZigStartedEvent<T>());
				leftRotate(g);
				leftRotate(p);
				fireChange(new ZigZigEndedEvent<T>());
			} else if (x.equals(p.getRight()) && p.equals(g.getLeft())) {
				fireChange(new ZigZagStartedEvent<T>());
				leftRotate(p);
				rightRotate(g);
				fireChange(new ZigZagEventEnded<T>());
			} else if (x.equals(p.getLeft()) && p.equals(g.getRight())) {
				fireChange(new ZigZagStartedEvent<T>());
				rightRotate(p);
				leftRotate(g);
				fireChange(new ZigZagEventEnded<T>());
			}
		}
		splay(x);
	}

}
