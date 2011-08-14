package de.lere.vaad.treebuilder.events;

import javax.annotation.Nullable;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;


public abstract class TreeModelChangeEvent<T extends Comparable<T>> extends TreeEvent<T> {
	
	public final BinaryTreeModel<T> beforeChange;
	public final BinaryTreeModel<T> afterChange;
	public final @Nullable Node<T> nodeOfModification;
	
	public TreeModelChangeEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after, Node<T> nodeOfModification) {
		this.beforeChange = before;
		this.afterChange = after;
		this.nodeOfModification = nodeOfModification;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;		
		if(obj.getClass() == this.getClass()){
			TreeModelChangeEvent<?> o = (TreeModelChangeEvent<?>) obj;
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
}
