package de.lere.vaad.animation.splaytree;

import de.lere.vaad.treebuilder.events.TreeEventListener;

public class SplayEndedEvent<T extends Comparable<T>> extends SplayTreeEvent<T> {

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
