package de.lere.vaad.treebuilder.events;

import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.binarysearchtree.BinaryTreeSetup;

public class TreeModelchangeEventListenerForGenericTreeAnimations<T extends Comparable<T>> extends
		DefaultTreeModelChangeEventListener<T> {

	public TreeModelchangeEventListenerForGenericTreeAnimations(
			BinaryTreeSetup<T> setup) {
		super(setup);		
	}
	
	@Override
	public void animateDelete(TreeDeleteEvent<T> event) {
		super.animateDelete(event);
		Timings t = new Timings();
		GraphWriter<T> writer = getWriter();
		writer.highlightNode(event.afterChange, event.nodeOfModification.getParent(), t.NOW , t.DEFAULT_ANIMATION);
		//writer.unhighlightNode(event.afterChange, event.nodeOfModification.getParent(), t.DEFAULT_ANIMATION, t.DEFAULT_ANIMATION);
		getLang().nextStep();
	}
}
