package de.lere.vaad.treebuilder;

import static de.lere.vaad.treebuilder.Timings.NOW;
import static de.lere.vaad.treebuilder.Timings.SHORT_ANIMATION;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import algoanim.primitives.generators.Language;
import algoanim.util.TicksTiming;

public class ExtractedBinaryTreeAnimations<T extends Comparable<T>> implements
		TreeAnimator<T> {

	private GraphWriter<T> writer;

	public ExtractedBinaryTreeAnimations(GraphWriter<T> writer) {
		if (writer == null) {
			throw new IllegalArgumentException("Writer must not be null");
		}
		this.writer = writer;
	}

	public void animateRotateRight(Language lang,
			TreeRightRotateEvent<T> event, BinaryTreeLayout layout) {
		if (event.beforeChange.equals(event.afterChange)) {
			return;
		}
		writer.buildGraph(event.beforeChange, Timings.NOW);

		StepTiming stepTiming = new StepTiming(Timings.NOW,
				Timings.SHORT_ANIMATION);

		Node<T> rotateAround = event.nodeOfModification;
		Node<T> nodeInOriginal = BinaryTreeModel.lookupNodeByID(
				event.beforeChange, rotateAround);
		Node<T> parentOfRotate = nodeInOriginal.getParent();
		Node<T> grandParentOfRotate = parentOfRotate != null ? parentOfRotate
				.getParent() : null;
		Node<T> parentOfDetach = nodeInOriginal;
		Node<T> nodeToDetach = parentOfDetach.getRight();
		writer.highlightEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), new TicksTiming(25));
		writer.highlightEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.newInterval(25));
		writer.hideEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), new TicksTiming(25));
		writer.hideEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.newInterval(25));
		writer.translateNodes(event.beforeChange, event.afterChange,
				stepTiming.now(), stepTiming.newInterval(100));
		writer.buildGraph(event.afterChange, stepTiming.now());
		Node<T> right = event.nodeOfModification.getRight();
		Node<T> rightLeft = right != null ? right.getLeft() : null;
		writer.highlightEdge(event.afterChange, right, rightLeft,
				stepTiming.now(), Timings.SHORT_ANIMATION);
		writer.unhighlightEdge(event.afterChange, right, rightLeft,
				stepTiming.now(), stepTiming.newInterval(25));
		lang.nextStep();
	}

	public void animateRotateLeft(Language lang, TreeLeftRotateEvent<T> event,
			BinaryTreeLayout layout) {
		if (event.beforeChange.equals(event.afterChange)) {
			return;
		}
		writer.buildGraph(event.beforeChange, Timings.NOW);

		StepTiming stepTiming = new StepTiming(Timings.NOW,
				Timings.SHORT_ANIMATION);

		Node<T> rotateAround = event.nodeOfModification;
		Node<T> nodeInOriginal = BinaryTreeModel.lookupNodeByID(
				event.beforeChange, rotateAround);
		Node<T> parentOfRotate = nodeInOriginal.getParent();
		Node<T> grandParentOfRotate = parentOfRotate != null ? parentOfRotate
				.getParent() : null;
		Node<T> parentOfDetach = nodeInOriginal;
		Node<T> nodeToDetach = parentOfDetach.getLeft();
		writer.highlightEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.newInterval(25));
		writer.highlightEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.newInterval(25));
		writer.hideEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.newInterval(25));
		writer.hideEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.newInterval(25));
		writer.translateNodes(event.beforeChange, event.afterChange,
				stepTiming.now(), stepTiming.newInterval(100));
		writer.buildGraph(event.afterChange, stepTiming.now());
		Node<T> left = event.nodeOfModification.getLeft();
		Node<T> leftRight = left != null ? left.getLeft() : null;
		writer.highlightEdge(event.afterChange, left, leftRight,
				stepTiming.now(), Timings.SHORT_ANIMATION);
		writer.unhighlightEdge(event.afterChange, left, leftRight,
				stepTiming.now(), stepTiming.newInterval(25));
		lang.nextStep();
	}

	public void animateInsert(Language lang, TreeInsertEvent<T> event,
			BinaryTreeLayout layout) {
		if (event.nodeOfModification == null)
			return;

		writer.buildGraph(event.afterChange, Timings.NOW);

		de.lere.vaad.treebuilder.Node<T> nodeOfModification = event.nodeOfModification;
		Node<T> parentNode = nodeOfModification.getParent();

		writer.highlightNode(event.afterChange, nodeOfModification,
				Timings.NOW, Timings.SHORT_ANIMATION);
		writer.unhighlightNode(event.afterChange, nodeOfModification,
				Timings.SHORT_ANIMATION, Timings.SHORT_ANIMATION);
		writer.highlightEdge(event.afterChange, parentNode, nodeOfModification,
				Timings.NOW, Timings.SHORT_ANIMATION);
		writer.unhighlightEdge(event.afterChange, parentNode,
				nodeOfModification, Timings.SHORT_ANIMATION,
				Timings.SHORT_ANIMATION);
		lang.nextStep();

		writer.buildGraph(event.afterChange, Timings.NOW);

		lang.nextStep();
	}

	public void animateDelete(Language lang, TreeDeleteEvent<T> event,
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
		writer.buildGraph(event.afterChange, Timings.NOW);

		lang.nextStep();
	}

	public void animateNew(Language lang, BinaryTreeModel<T> model,
			BinaryTreeLayout layout) {
		writer.buildGraph(model, Timings.NOW);
		lang.nextStep();
	}

	public void update(TreeSearchEvent<T> event) {
		List<de.lere.vaad.treebuilder.Node<T>> listFromRootToNode = getListFromRootToNode(event.nodeOfModification);
		for (int i = 0; i < listFromRootToNode.size() - 1; i++) {
			de.lere.vaad.treebuilder.Node<T> next = listFromRootToNode.get(i);
			writer.blinkNode(event.beforeChange, next, NOW, SHORT_ANIMATION);
		}
		writer.highlightNode(event.beforeChange, event.nodeOfModification);
	}

	private List<de.lere.vaad.treebuilder.Node<T>> getListFromRootToNode(
			de.lere.vaad.treebuilder.Node<T> nodeOfModification) {
		List<de.lere.vaad.treebuilder.Node<T>> lineToRoot = new ArrayList<de.lere.vaad.treebuilder.Node<T>>();
		if (nodeOfModification == null)
			return lineToRoot;
		de.lere.vaad.treebuilder.Node<T> next = nodeOfModification;
		lineToRoot.add(next);
		while (next.hasParent()) {
			next = next.getParent();
			lineToRoot.add(next);
		}
		Collections.reverse(lineToRoot);
		return lineToRoot;
	}

	@Override
	public void animate(Language lang, TreeEvent<T> event,
			BinaryTreeLayout layout) {
		if (event instanceof TreeInsertEvent<?>) {
			animateInsert(lang, (TreeInsertEvent<T>) event, layout);
		} else if (event instanceof TreeDeleteEvent<?>) {
			animateDelete(lang, (TreeDeleteEvent<T>) event, layout);
		} else if (event instanceof TreeLeftRotateEvent<?>) {
			animateRotateLeft(lang, (TreeLeftRotateEvent<T>) event, layout);
		} else if (event instanceof TreeRightRotateEvent<?>) {
			animateRotateRight(lang, (TreeRightRotateEvent<T>) event, layout);
		} else if (event instanceof TreeNewEvent<?>) {
			animateNew(lang, ((TreeNewEvent<T>) event).beforeChange, layout);
		} else if (event instanceof TreeSearchEvent<?>) {
			update((TreeSearchEvent<T>) event);
		}

	}
}
