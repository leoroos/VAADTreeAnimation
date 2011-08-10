package de.lere.vaad.treebuilder;

import javax.swing.event.TreeModelListener;


public abstract class TreeEvent<T extends Comparable<T>> {
	
	public final BinaryTreeModel<T> beforeChange;
	public final BinaryTreeModel<T> afterChange;
	//May be null
	public final Node<T> nodeOfModification;
	
	public TreeEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after, Node<T> nodeOfModification) {
		this.beforeChange = before;
		this.afterChange = after;
		this.nodeOfModification = nodeOfModification;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;		
		if(obj.getClass() == this.getClass()){
			TreeEvent<?> o = (TreeEvent<?>) obj;
			boolean modificationNodeEqual;
			if(this.nodeOfModification == null)
				modificationNodeEqual = o.nodeOfModification == null;
			else
				modificationNodeEqual = this.nodeOfModification.equals(o.nodeOfModification);
			return o.beforeChange.equals(this.beforeChange) && o.afterChange.equals(this.afterChange) && modificationNodeEqual;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return 11 * beforeChange.hashCode() + afterChange.hashCode();
	}
	
	public abstract void notifyListener(TreeEventListener<T> listener);
}
