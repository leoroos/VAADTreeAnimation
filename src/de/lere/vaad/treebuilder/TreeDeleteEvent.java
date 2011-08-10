package de.lere.vaad.treebuilder;

/**
 * Event fired when a node in the tree has been deleted. It carries the deleted
 * node and it's successor.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public class TreeDeleteEvent<T extends Comparable<T>> extends TreeEvent<T> {

	/**
	 * Node with which the original node has been replaced. May be null
	 */
	public final Node<T> successor;

	public TreeDeleteEvent(BinaryTreeModel<T> old, BinaryTreeModel<T> current,
			Node<T> deleted, Node<T> successor) {
		super(old, current, deleted);
		this.successor = successor;
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}
}
