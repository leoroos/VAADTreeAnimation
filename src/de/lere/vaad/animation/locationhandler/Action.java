package de.lere.vaad.animation.locationhandler;

import algoanim.util.Node;

public interface Action<T extends Node> {

	void activateOn(LocationProvider<T> location);

	void deactivate();

}
