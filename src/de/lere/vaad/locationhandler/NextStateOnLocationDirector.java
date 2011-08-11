package de.lere.vaad.locationhandler;

import algoanim.util.Node;

public class NextStateOnLocationDirector<T extends Node>  implements LocationDirector<T> {

	private final T location;
	Action<T> currentstate;

	public NextStateOnLocationDirector(T myPos) {
		this.location = myPos;
	}
	
	@Override
	public void nextState(Action<T> s) {
		if (currentstate != null) {
			currentstate.deactivate();
		}
		s.activateOn(this);
		this.currentstate = s;
	}

	public T getLocation() {
		return location;
	}

	
}
