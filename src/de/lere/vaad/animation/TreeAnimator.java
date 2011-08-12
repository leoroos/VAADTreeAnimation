package de.lere.vaad.animation;

import de.lere.vaad.treebuilder.events.TreeEvent;


public interface TreeAnimator<T extends Comparable<T>> {

	void animate(TreeEvent<T> event);

}
