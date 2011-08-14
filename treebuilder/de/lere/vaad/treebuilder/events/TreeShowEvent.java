package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;

/**
 * Event indicating that model has received a show event.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public class TreeShowEvent<T extends Comparable<T>> extends
		TreeVisibilityEvent<T> {

	public TreeShowEvent(BinaryTreeModel<T> copy) {
		super(copy);
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
