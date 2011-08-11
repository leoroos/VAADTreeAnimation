package de.lere.vaad.locationhandler;

import algoanim.util.Node;

public interface LocationDirector<T extends Node> extends LocationProvider<T>{

	public void nextState(Action<T> s);
	
}
