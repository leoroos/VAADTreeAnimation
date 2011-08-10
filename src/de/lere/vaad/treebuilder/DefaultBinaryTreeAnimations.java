package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;

public class DefaultBinaryTreeAnimations<T extends Comparable<T>> implements
		TreeDeleteOperationAnimator<T>, TreeInsertOperationAnimator<T>,
		TreeLeftRotationAnimator<T>, TreeRightRotationAnimator<T>,
		NewTreeAnimator<T> {

	private GraphWriter<T> writer;

	public DefaultBinaryTreeAnimations(GraphWriter<T> writer) {
		if (writer == null) {
			throw new IllegalArgumentException("Writer must not be null");
		}
		this.writer = writer;
	}

	@Override
	public void animate(Language lang, TreeRightRotateEvent<T> event,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);
	}

	@Override
	public void animate(Language lang, TreeLeftRotateEvent<T> event,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);
	}

	@Override
	public void animate(Language lang, TreeInsertEvent<T> event,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);
	}

	@Override
	public void animate(Language lang, TreeDeleteEvent<T> event,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);
	}

	@Override
	public void animate(Language lang, BinaryTreeModel<T> model,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, model, layout, Timings.NOW);
	}
}
