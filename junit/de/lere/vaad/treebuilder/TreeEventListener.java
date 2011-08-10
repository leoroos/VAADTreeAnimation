package de.lere.vaad.treebuilder;

public interface TreeEventListener<T extends Comparable<T>> {
	
	public void update(TreeEvent<T> event);

}
