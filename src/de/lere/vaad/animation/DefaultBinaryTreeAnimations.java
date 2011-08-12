package de.lere.vaad.animation;

import javax.annotation.Nonnull;

import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeModelChangeEvent;


public class DefaultBinaryTreeAnimations<T extends Comparable<T>> implements
		TreeAnimator<T> {

	private GraphWriter<T> writer;

	public @Nonnull DefaultBinaryTreeAnimations(GraphWriter<T> writer) {
		if (writer == null) {
			throw new IllegalArgumentException("Writer must not be null");
		}
		this.writer = writer;
	}

	@Override
	public void animate(TreeEvent<T> event) {
		if (event instanceof TreeModelChangeEvent<?>) {
			writer.buildGraph(((TreeModelChangeEvent<T>) event).afterChange);
		}
	}
}
