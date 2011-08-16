package de.lere.vaad.animation.splaytree;

import de.lere.vaad.treebuilder.events.TreeEventListener;

public class SplayTreeStartEvent<T extends Comparable<T>> extends SplayTreeEvent<T> {

	private ROTATION_TYPE rotationType;
	
	public SplayTreeStartEvent(ROTATION_TYPE type){
		this.rotationType = type;
	}
	
	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}
	
	public ROTATION_TYPE getRotationType() {
		return rotationType;
	}
}
