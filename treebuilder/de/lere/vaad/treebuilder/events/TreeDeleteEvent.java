package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.BinaryTreeModel.DeleteStatisticResult;
import de.lere.vaad.treebuilder.BinaryTreeModel.StatisticResult;
import de.lere.vaad.treebuilder.Node;

/**
 * Event fired when a node in the tree has been deleted. It carries the deleted
 * node and it's successor.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public class TreeDeleteEvent<T extends Comparable<T>> extends TreeModelChangeEvent<T> {

	/**
	 * Node with which the original node has been replaced. May be null
	 */
	public final Node<T> successor;
	public final DeleteStatisticResult statistics;
	public final T deleteValue;

	public TreeDeleteEvent(BinaryTreeModel<T> old, BinaryTreeModel<T> current,
			Node<T> deleted, Node<T> successor, DeleteStatisticResult statistics,T deleteValue) {
		super(old, current, deleted);
		this.successor = successor;
		this.statistics = statistics;
		this.deleteValue = deleteValue;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean equals = super.equals(obj);
		if(equals){
			TreeDeleteEvent<?> other = (TreeDeleteEvent<?>) obj;
			return this.successor.equals(other.successor) && this.statistics.equals(other.statistics);
		}
		else
			return false;
	}
	
	@Override
	public int hashCode() {
		return 29 * (statistics.hashCode() + (super.hashCode() * 11 + 7 * successor.hashCode()));
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}
}
