package de.lere.vaad.treebuilder.events;

public interface TreeEventListener<T extends Comparable<T>> {
	
	public void update(TreeEvent<T> event);

}
