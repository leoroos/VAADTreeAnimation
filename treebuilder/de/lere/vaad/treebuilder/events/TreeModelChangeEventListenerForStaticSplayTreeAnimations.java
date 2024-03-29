package de.lere.vaad.treebuilder.events;

import de.lere.vaad.animation.binarysearchtree.BinaryTreeSetup;

public class TreeModelChangeEventListenerForStaticSplayTreeAnimations<T extends Comparable<T>>
		extends DefaultTreeModelChangeEventListener<T> {

	public TreeModelChangeEventListenerForStaticSplayTreeAnimations(
			BinaryTreeSetup<T> setup) {
		super(setup);
	}

	@Override
	public void update(TreeSearchEvent<T> event) {
		//super.update(event);
	}
}
