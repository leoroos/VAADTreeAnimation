package de.lere.vaad.locationhandler;

import algoanim.util.Node;

public interface LocationProvider<T extends Node> {
	
	T getLocation();

}
