package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;


public interface TreeLeftRotationAnimator<T extends Comparable<T>> {
	
	void animate(Language lang, TreeLeftRotateEvent<T> event, BinaryTreeLayout layout);	
}
