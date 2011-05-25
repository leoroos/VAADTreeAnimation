package de.lere.vaad;

import algoanim.util.Node;

public class AnimationLocationDirector implements LocationDirector {

	private final Node location;
	Action currentstate;

	public AnimationLocationDirector(Node myPos) {
		this.location = myPos;
	}

	public void nextState(Action s) {
		if (currentstate != null) {
			currentstate.deactivate();
		}
		s.activateOn(this);
		this.currentstate = s;
	}

	public Node getLocation() {
		return location;
	}

}
