package de.lere.vaad.animation.splaytree;

import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;

public class SplayTreeEndEvent<T extends Comparable<T>> extends TreeEvent<T> {

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
