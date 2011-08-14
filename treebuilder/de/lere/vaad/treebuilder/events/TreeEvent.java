package de.lere.vaad.treebuilder.events;

public abstract class TreeEvent<T extends Comparable<T>> {

	public TreeEvent() {
		super();
	}

	public abstract void notifyListener(TreeEventListener<T> listener);

}