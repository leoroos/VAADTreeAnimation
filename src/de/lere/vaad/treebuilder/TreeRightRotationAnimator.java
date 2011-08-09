package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public interface TreeRightRotationAnimator<T extends Comparable<T>> {

	void animate(Language lang, TreeRightRotateEvent<T> event,
			BinaryTreeLayout layout);

}
