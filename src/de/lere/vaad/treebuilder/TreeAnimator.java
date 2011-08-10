package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public interface TreeAnimator<T extends Comparable<T>> {

	void animate(Language lang, TreeEvent<T> event,
			BinaryTreeLayout layout);

}
