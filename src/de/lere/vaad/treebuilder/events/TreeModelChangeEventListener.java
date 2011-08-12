package de.lere.vaad.treebuilder.events;

import static de.lere.vaad.animation.Timings.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.ProgressTiming;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.TreeAnimator;
import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.Node;

import algoanim.primitives.generators.Language;
import algoanim.util.TicksTiming;

public class TreeModelChangeEventListener<T extends Comparable<T>> implements
		TreeAnimator<T>, TreeEventListener<T> {

	private GraphWriter<T> writer;
	private final Language lang;

	public TreeModelChangeEventListener(Language language, GraphWriter<T> writer) {
		this.lang = language;
		if (writer == null) {
			throw new IllegalArgumentException("Writer must not be null");
		}
		this.writer = writer;
	}

	public void animateRotateRight(TreeRightRotateEvent<T> event) {
		if (event.beforeChange.equals(event.afterChange)) {
			return;
		}
		writer.buildGraph(event.beforeChange);

		ProgressTiming stepTiming = createRotateTimings();

		Node<T> rotateAround = event.nodeOfModification;
		Node<T> nodeInOriginal = BinaryTreeModel.lookupNodeByID(
				event.beforeChange, rotateAround);
		Node<T> parentOfRotate = nodeInOriginal.getParent();
		Node<T> grandParentOfRotate = parentOfRotate != null ? parentOfRotate
				.getParent() : null;
		Node<T> parentOfDetach = nodeInOriginal;
		Node<T> nodeToDetach = parentOfDetach.getRight();
		writer.highlightEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.getInterval());
		writer.highlightEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.progress());
		writer.hideEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.getInterval());
		writer.hideEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.progress());
		writer.translateNodes(event.beforeChange, event.afterChange,
				stepTiming.now(), stepTiming.progress(LONG_ANIMATION));
		writer.buildGraph(event.afterChange, stepTiming.now());
		Node<T> right = event.nodeOfModification.getRight();
		Node<T> rightLeft = right != null ? right.getLeft() : null;
		writer.highlightEdge(event.afterChange, right, rightLeft,
				stepTiming.now(), stepTiming.getInterval());
		writer.unhighlightEdge(event.afterChange, right, rightLeft,
				stepTiming.now(), stepTiming.progress());
		lang.nextStep();
	}

	private ProgressTiming createRotateTimings() {
		return new ProgressTiming(Timings.NOW,
				Timings.DEFAULT_ANIMATION);
	}

	public void animateRotateLeft(TreeLeftRotateEvent<T> event) {
		if (event.beforeChange.equals(event.afterChange)) {
			return;
		}
		writer.buildGraph(event.beforeChange);

		ProgressTiming stepTiming = createRotateTimings();

		Node<T> rotateAround = event.nodeOfModification;
		Node<T> nodeInOriginal = BinaryTreeModel.lookupNodeByID(
				event.beforeChange, rotateAround);
		Node<T> parentOfRotate = nodeInOriginal.getParent();
		Node<T> grandParentOfRotate = parentOfRotate != null ? parentOfRotate
				.getParent() : null;
		Node<T> parentOfDetach = nodeInOriginal;
		Node<T> nodeToDetach = parentOfDetach.getLeft();
		writer.highlightEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.getInterval());
		writer.highlightEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.progress());
		writer.hideEdge(event.beforeChange, parentOfDetach, nodeToDetach,
				stepTiming.now(), stepTiming.getInterval());
		writer.hideEdge(event.beforeChange, grandParentOfRotate,
				parentOfRotate, stepTiming.now(), stepTiming.progress());
		writer.translateNodes(event.beforeChange, event.afterChange,
				stepTiming.now(), stepTiming.progress(LONG_ANIMATION.getDelay()));
		writer.buildGraph(event.afterChange, stepTiming.now());
		Node<T> left = event.nodeOfModification.getLeft();
		Node<T> leftRight = left != null ? left.getLeft() : null;
		writer.highlightEdge(event.afterChange, left, leftRight,
				stepTiming.now(), stepTiming.getInterval());
		writer.unhighlightEdge(event.afterChange, left, leftRight,
				stepTiming.now(), stepTiming.progress());
		lang.nextStep();
	}

	public void animateInsert(TreeInsertEvent<T> event) {
		if (event.nodeOfModification == null)
			return;

		writer.buildGraph(event.afterChange);

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

		writer.buildGraph(event.afterChange);

		lang.nextStep();
	}

	public void animateDelete(TreeDeleteEvent<T> event) {
		if (event.nodeOfModification == null)
			return;

		writer.highlightNode(event.beforeChange, event.nodeOfModification,
				Timings.NOW, Timings.SHORT_ANIMATION);

		writer.highlightNode(event.beforeChange, event.successor,
				Timings.SHORT_ANIMATION,
				Timings.SHORT_ANIMATION);

		lang.nextStep();

		writer.buildGraph(event.afterChange);

		lang.nextStep();
	}

	public void animateNew(Language lang, BinaryTreeModel<T> model) {
		writer.buildGraph(model);
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
	public void animate(TreeEvent<T> event) {
		if (event instanceof TreeModelChangeEvent<?>) {
			if (event instanceof TreeInsertEvent<?>) {
				animateInsert((TreeInsertEvent<T>) event);
			} else if (event instanceof TreeDeleteEvent<?>) {
				animateDelete((TreeDeleteEvent<T>) event);
			} else if (event instanceof TreeLeftRotateEvent<?>) {
				animateRotateLeft((TreeLeftRotateEvent<T>) event);
			} else if (event instanceof TreeRightRotateEvent<?>) {
				animateRotateRight((TreeRightRotateEvent<T>) event);
			} else if (event instanceof TreeNewEvent<?>) {
				animateNew(lang, ((TreeNewEvent<T>) event).beforeChange);
			} else if (event instanceof TreeSearchEvent<?>) {
				update((TreeSearchEvent<T>) event);
			}
		}
		// only handling model change events
	}

	@Override
	public void update(TreeEvent<T> event) {
		animate(event);
	}
}
