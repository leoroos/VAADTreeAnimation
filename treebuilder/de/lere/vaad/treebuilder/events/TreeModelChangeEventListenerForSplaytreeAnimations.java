package de.lere.vaad.treebuilder.events;

import de.lere.vaad.animation.binarysearchtree.BinaryTreeSetup;

public class TreeModelChangeEventListenerForSplaytreeAnimations<T extends Comparable<T>> extends
		DefaultTreeModelChangeEventListener<T> {

	public TreeModelChangeEventListenerForSplaytreeAnimations(
			BinaryTreeSetup<T> setup) {
		super(setup);		
	}
	
	@Override
	public void update(TreeSearchEvent<T> event) {
		//Do nothing on search
	}
}
