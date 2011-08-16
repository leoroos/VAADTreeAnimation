package de.lere.vaad.animation.splaytree;

public class ZigZagStartedEvent<T extends Comparable<T>> extends SplayTreeStartEvent<T> {

	public ZigZagStartedEvent(
			de.lere.vaad.animation.splaytree.ROTATION_TYPE type) {
		super(type);
	}

}
