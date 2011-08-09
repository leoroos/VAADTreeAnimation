package de.lere.vaad.locationhandler;

import java.util.Hashtable;


public class LocationDirectorProviderImpl implements LocationDirectorProvider{
	
	
	private Hashtable<String, LocationDirector> directors = new Hashtable<String, LocationDirector>();
	
	
	/**
	 * Add a director for a specific location.
	 * 
	 * @param key the unique key to identify a director.
	 * @param director
	 */
	public void addDirector(String key, LocationDirector director){
		this.directors.put(key, director);		
	}
	
	public LocationDirector getDirector(String key){
		return this.directors.get(key);
	}
	
	public void nextStateOnLocation(Action action, String directorName) {
		getDirector(directorName).nextState(action);
	}

	

}
