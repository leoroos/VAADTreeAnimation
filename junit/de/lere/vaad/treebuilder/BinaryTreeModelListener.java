package de.lere.vaad.treebuilder;

public interface BinaryTreeModelListener<T extends Comparable<T>> {

	public void update(TreeInsertEvent<T> event);
	
	public void update(TreeSearchEvent<T> event);
	
	public void update(TreeDeleteEvent<T> event);
	
	public void update(TreeLeftRotateEvent<T> event);
	
	public void update(TreeRightRotateEvent<T> event);
}
