package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public interface TreeInsertOperationAnimator<T extends Comparable<T>> {

	void animate(Language lang, TreeInsertEvent<T> event, BinaryTreeLayout layout);
}
