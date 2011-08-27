package de.lere.vaad.treebuilder.events;

import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.binarysearchtree.BinaryTreeSetup;
import de.lere.vaad.treebuilder.Node;

public class TreeModelchangeEventListenerForGenericTreeAnimations<T extends Comparable<T>>
		extends DefaultTreeModelChangeEventListener<T> {

	public TreeModelchangeEventListenerForGenericTreeAnimations(
			BinaryTreeSetup<T> setup) {
		super(setup);
	}

	@Override
	public void animateDelete(TreeDeleteEvent<T> event) {
		super.animateDelete(event);
		if (event.nodeOfModification == null) {
			return;
		}
		Timings t = new Timings();
		GraphWriter<T> writer = getWriter();
		writer.highlightNode(event.afterChange,
				event.nodeOfModification.getParent(), t.NOW,
				t.DEFAULT_ANIMATION);
		getLang().nextStep();
	}
}
