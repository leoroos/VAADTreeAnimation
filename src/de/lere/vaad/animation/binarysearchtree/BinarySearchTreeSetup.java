package de.lere.vaad.animation.binarysearchtree;


import javax.annotation.Nonnull;

import algoanim.util.Coordinates;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.animation.locationhandler.LocationHandler;

/**
 * Don't reuse objects of this class, the instantiated references might be used
 * by objects to which an instance of this class has been passed.
 * <p>
 * Null values are class wide not allowed.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
@Nonnull
public class BinarySearchTreeSetup<T extends Comparable<T>> extends BinaryTreeSetup<T> {
	private LocationDirector<?> coarseDescription;
	private LocationDirector<?> fineDescription;
	private SourceCodeWriter sourceCodeWriter;
	private LocationHandler locationHandler;
	public void setCoarseDescription(LocationDirector<?> coarseDescription) {
		bouncer(coarseDescription);
		this.coarseDescription = coarseDescription;
	}

	public void setFineDescription(LocationDirector<?> fineDescription) {
		bouncer(fineDescription);
		this.fineDescription = fineDescription;
	}

	public void setLh(LocationHandler lh) {
		bouncer(lh);
		this.locationHandler = lh;
	}

	public LocationDirector<?> getCoarseDescription() {
		bouncer(coarseDescription);
		return coarseDescription;
	}

	public LocationDirector<?> getFineDescription() {
		bouncer(fineDescription);
		return fineDescription;
	}

	public SourceCodeWriter getSourceCodeWriter() {
		bouncer(sourceCodeWriter);
		return sourceCodeWriter;
	}

	//FIXME rename
	public void setSourceCodeAnchor(SourceCodeWriter scw){
		bouncer(scw);
		this.sourceCodeWriter = scw;
	}

	//FIXME rename
	public LocationHandler getLocationHandler() {
		bouncer(locationHandler);
		return locationHandler;
	}


}