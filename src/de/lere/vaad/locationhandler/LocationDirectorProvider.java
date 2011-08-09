package de.lere.vaad.locationhandler;

public interface LocationDirectorProvider {

	public void addDirector(String key, LocationDirector director);
	
	public LocationDirector getDirector(String key);

	/**
	 * Puts the a new action on a location
	 * 
	 * @param action
	 * @param location
	 */
	public void nextStateOnLocation(Action action, String directorName);
	
}
