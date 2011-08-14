package de.lere.vaad.animation.locationhandler;

import algoanim.util.Node;

public interface LocationProvider<T extends Node> {
	
	T getLocation();

}
