package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public interface NewTreeAnimator<T extends Comparable<T>> {

	public void animate(Language lang, BinaryTreeModel<T> model, BinaryTreeLayout layout);
}
