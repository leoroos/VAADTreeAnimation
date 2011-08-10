package de.lere.vaad.treebuilder;

import algoanim.primitives.generators.Language;
import algoanim.util.Timing;

public class ExtractedBinaryTreeAnimations<T extends Comparable<T>> implements
		TreeDeleteOperationAnimator<T>, TreeInsertOperationAnimator<T>,
		TreeLeftRotationAnimator<T>, TreeRightRotationAnimator<T>,
		NewTreeAnimator<T> {

	private GraphWriter<T> writer;

	public ExtractedBinaryTreeAnimations(GraphWriter<T> writer) {
		if (writer == null) {
			throw new IllegalArgumentException("Writer must not be null");
		}
		this.writer = writer;
	}

	@Override
	public void animate(Language lang, TreeRightRotateEvent<T> event,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, event.beforeChange, layout, Timings.NOW);

		StepTiming stepTiming = new StepTiming(Timings.NOW, Timings._25_TICKS);

		Node<T> rotateAround = event.nodeOfModification;
		Node<T> nodeInOriginal = BinaryTreeModel.lookupNodeByID(
				event.beforeChange, rotateAround);
		Node<T> parentOfDetach = nodeInOriginal;
		Node<T> nodeToDetach = parentOfDetach.getRight();
		writer.highlightEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.newInterval(25));
		writer.hideEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.newInterval(25));
		writer.translateNodes(event.beforeChange, event.afterChange, layout,
				stepTiming.now(), stepTiming.newInterval(100));
		writer.buildGraph(lang, event.afterChange, layout, stepTiming.now());
		writer.highlightEdge(event.afterChange, event.nodeOfModification
				.getRight(), event.nodeOfModification.getRight().getLeft(),
				stepTiming.now(), stepTiming.newInterval(25));
		writer.unhighlightEdge(event.afterChange, event.nodeOfModification
				.getRight(), event.nodeOfModification.getRight().getLeft(),
				stepTiming.now(), stepTiming.newInterval(25));
		lang.nextStep();
	}

	@Override
	public void animate(Language lang, TreeLeftRotateEvent<T> event,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, event.beforeChange, layout, Timings.NOW);

		StepTiming stepTiming = new StepTiming(Timings.NOW, Timings._25_TICKS);

		Node<T> rotateAround = event.nodeOfModification;
		Node<T> nodeInOriginal = BinaryTreeModel.lookupNodeByID(
				event.beforeChange, rotateAround);
		Node<T> parentOfDetach = nodeInOriginal;
		Node<T> nodeToDetach = parentOfDetach.getLeft();
		writer.highlightEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.newInterval(25));
		writer.hideEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.newInterval(25));
		writer.translateNodes(event.beforeChange, event.afterChange, layout,
				stepTiming.now(), stepTiming.newInterval(100));
		writer.buildGraph(lang, event.afterChange, layout, stepTiming.now());
		writer.highlightEdge(event.afterChange, event.nodeOfModification
				.getLeft(), event.nodeOfModification.getLeft().getRight(),
				stepTiming.now(), stepTiming.newInterval(25));
		writer.unhighlightEdge(event.afterChange, event.nodeOfModification
				.getLeft(), event.nodeOfModification.getLeft().getRight(),
				stepTiming.now(), stepTiming.newInterval(25));
		lang.nextStep();
	}

	@Override
	public void animate(Language lang, TreeInsertEvent<T> event,
			BinaryTreeLayout layout) {
		if (event.nodeOfModification == null)
			return;

		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);

		de.lere.vaad.treebuilder.Node<T> nodeOfModification = event.nodeOfModification;
		Node<T> parentNode = nodeOfModification.getParent();

		writer.highlightNode(event.afterChange, nodeOfModification,
				Timings.NOW, Timings._25_TICKS);
		writer.unhighlightNode(event.afterChange, nodeOfModification,
				Timings._25_TICKS, Timings._25_TICKS);
		writer.highlightEdge(event.afterChange, parentNode, nodeOfModification,
				Timings.NOW, Timings._25_TICKS);
		writer.unhighlightEdge(event.afterChange, parentNode,
				nodeOfModification, Timings._25_TICKS, Timings._25_TICKS);
		lang.nextStep();

		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);

		lang.nextStep();
	}

	@Override
	public void animate(Language lang, TreeDeleteEvent<T> event,
			BinaryTreeLayout layout) {
		if (event.nodeOfModification == null)
			return;

		writer.highlightNode(event.beforeChange, event.nodeOfModification,
				Timings.NOW, Timings.DELETE_NODE_HIGHLIGHT_DURATION);

		writer.highlightNode(event.beforeChange, event.successor,
				Timings.DELETE_NODE_HIGHLIGHT_DURATION,
				Timings.DELETE_NODE_HIGHLIGHT_DURATION);

		lang.nextStep();

		//
		// lastCreatedGraph.unhighlightNode(
		// indexedNodes, DELETE_NODE_HIGHLIGHT_DURATION,
		// DELETE_NODE_HIGHLIGHT_DURATION);
		// infos.graph.highlightNode(endNode, NOW, HIGHLIGHT_EDGE_DURATION);
		// infos.graph.highlightEdge(startNode, endNode, NOW,
		// HIGHLIGHT_EDGE_DURATION);
		//
		writer.buildGraph(lang, event.afterChange, layout, Timings.NOW);

		lang.nextStep();
	}

	@Override
	public void animate(Language lang, BinaryTreeModel<T> model,
			BinaryTreeLayout layout) {
		writer.buildGraph(lang, model, layout, Timings.NOW);
		lang.nextStep();
	}
}
