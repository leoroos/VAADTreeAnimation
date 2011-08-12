package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;

/**
 * model has received a hide request
 * 
 * @author Leo Roos, Rene Hertling
 *
 * @param <T>
 */
public class TreeHideEvent<T extends Comparable<T>> extends TreeVisibilityEvent<T> {

	public TreeHideEvent(BinaryTreeModel<T> copy) {
		super(copy);
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
