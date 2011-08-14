package de.lere.vaad.animation;

import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeHideEvent;
import de.lere.vaad.treebuilder.events.TreeShowEvent;
import de.lere.vaad.treebuilder.events.TreeVisibilityEvent;

public final class DefaultVisibilityEventListener<T extends Comparable<T>>
		implements TreeEventListener<T> {
	private final GraphWriterImpl<T> writer;

	public DefaultVisibilityEventListener(GraphWriterImpl<T> writer) {
		this.writer = writer;
	}

	@Override
	public void update(TreeEvent<T> event) {
		if (event instanceof TreeVisibilityEvent<?>) {
			if (event instanceof TreeHideEvent<?>) {
				writer.hideCurrent();
			}
			if (event instanceof TreeShowEvent<?>) {
				writer.buildGraph(((TreeShowEvent<T>) event).copy);
			}
		}
	}
}