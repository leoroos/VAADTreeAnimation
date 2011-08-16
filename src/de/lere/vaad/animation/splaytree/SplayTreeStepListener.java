package de.lere.vaad.animation.splaytree;

import de.lere.vaad.treebuilder.events.TreeDeleteEvent;
import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeInsertEvent;
import de.lere.vaad.treebuilder.events.TreeSearchEvent;

public interface SplayTreeStepListener<T extends Comparable<T>> {

	void newOperation(SplayStartedEvent<T> operation);
	
	void operationEnded(SplayEndedEvent<T> operation);	
	
	void splayStepStarted(SplayTreeStartEvent<T> step);

	void splayStepEnded(SplayTreeEndEvent<T> event);
	
	void rotationHappend(TreeEvent<T> rotationEvent);	
	
	void nodeFound(TreeSearchEvent<T> event);
	
	void nodeDelete(TreeDeleteEvent<T> event);
	
	void nodeInserted(TreeInsertEvent<T> event);
}
