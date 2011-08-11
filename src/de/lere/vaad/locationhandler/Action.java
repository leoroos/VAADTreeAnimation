package de.lere.vaad.locationhandler;

import algoanim.util.Node;

public interface Action<T extends Node> {

	void activateOn(LocationProvider<T> location);

	void deactivate();

}
