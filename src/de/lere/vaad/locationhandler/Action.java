package de.lere.vaad.locationhandler;

public interface Action {

	void activateOn(LocationProvider location);

	void deactivate();

}
