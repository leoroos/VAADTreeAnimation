package de.lere.vaad.animation.splaytree;

public class ZigZigStartedEvent<T extends Comparable<T>> extends
		SplayTreeStartEvent<T> {

	public ZigZigStartedEvent(
			de.lere.vaad.animation.splaytree.ROTATION_TYPE type) {
		super(type);
	}

}
