package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public class DefaultBinaryTreeAnimations<T extends Comparable<T>> implements
		TreeAnimator<T> {

	private GraphWriter<T> writer;

	public DefaultBinaryTreeAnimations(GraphWriter<T> writer) {
		if (writer == null) {
			throw new IllegalArgumentException("Writer must not be null");
		}
		this.writer = writer;
	}

	@Override
	public void animate(Language lang, TreeEvent<T> event,
			BinaryTreeLayout layout) {
		if (event instanceof TreeModelChangeEvent<?>) {
			writer.buildGraph(lang,
					((TreeModelChangeEvent<T>) event).afterChange, layout,
					Timings.NOW);
		}
	}
}
