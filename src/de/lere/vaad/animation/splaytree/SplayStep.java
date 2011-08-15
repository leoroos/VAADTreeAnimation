package de.lere.vaad.animation.splaytree;

import java.util.ArrayList;
import java.util.List;

import de.lere.vaad.treebuilder.events.TreeEvent;

public final class SplayStep<T extends Comparable<T>> {
	private List<TreeEvent<T>> eventsInStep;
	private SplayTreeEvent<T> cause;

	public SplayStep(SplayTreeEvent<T> cause) {
		if (cause == null) {
			throw new IllegalArgumentException("cause must not be null.");
		}
		this.cause = cause;
		this.eventsInStep = new ArrayList<TreeEvent<T>>();
	}
	
	public void addAll(List<TreeEvent<T>> events){
		this.eventsInStep.addAll(events);
	}
	
	public SplayTreeEvent<T> getCause() {
		return cause;
	}
	
	public List<TreeEvent<T>> getEventsInStep() {
		return eventsInStep;
	}
}