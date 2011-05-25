package de.lere.vaad;

public interface LocationDirectorProvider {

	public void addDirector(String key, LocationDirector director);
	
	public LocationDirector getDirector(String key);
	
}
