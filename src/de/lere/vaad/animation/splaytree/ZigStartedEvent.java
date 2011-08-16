package de.lere.vaad.animation.splaytree;

public class ZigStartedEvent<T extends Comparable<T>> extends
		SplayTreeStartEvent<T> {

	public ZigStartedEvent(
			de.lere.vaad.animation.splaytree.ROTATION_TYPE type) {
		super(type);
	}

}
