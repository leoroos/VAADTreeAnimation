package de.lere.vaad.locationhandler;

import algoanim.util.Offset;

public class AnimationLocationDirector implements LocationDirector {

	private final Offset location;
	Action currentstate;

	public AnimationLocationDirector(Offset myPos) {
		this.location = myPos;
	}

	public void nextState(Action s) {
		if (currentstate != null) {
			currentstate.deactivate();
		}
		s.activateOn(this);
		this.currentstate = s;
	}

	public Offset getLocation() {
		return location;
	}

}
