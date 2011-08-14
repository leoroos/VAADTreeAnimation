package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;

/**
 * Marker class. All Events extending this class handle visibility of the model 
 * 
 * @author Leo Roos, Rene Hertling
 *
 * @param <T>
 */
public abstract class TreeVisibilityEvent<T extends Comparable<T>> extends TreeEvent<T> {

	public final BinaryTreeModel<T> copy;

	public TreeVisibilityEvent(BinaryTreeModel<T> copy) {
		this.copy = copy;
	}
	
}
