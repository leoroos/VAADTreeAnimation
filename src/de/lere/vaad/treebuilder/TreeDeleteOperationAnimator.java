package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public interface TreeDeleteOperationAnimator<T extends Comparable<T>> {
	void animate(Language lang, TreeDeleteEvent<T> event,
			BinaryTreeLayout layout);
}
