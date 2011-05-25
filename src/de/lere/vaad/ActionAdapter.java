package de.lere.vaad;

import java.util.HashSet;
import java.util.Set;

import algoanim.primitives.Primitive;

/**
 * Provides a default implementation for {@link #deactivate()} and a method to
 * add multiple objects to a set which should be hided.
 * 
 * @author leo
 * 
 */
public abstract class ActionAdapter implements Action {

	/**
	 * This implementation iterates over the objects accumulated with
	 * {@link #hideOnDeactivate(Primitive)} and hides them. <br>
	 * Subclasses that wish to overwrite this method should call super to ensure
	 * that all objects added to the <i>to hide</i> List are hidden.
	 * 
	 * @see de.lere.vaad.Action#deactivate()
	 */
	@Override
	public void deactivate() {
		for (Primitive hidable : toHide) {
			if (hidable != null) {
				hidable.hide();
			}
		}
	}

	private Set<Primitive> toHide = new HashSet<Primitive>();

	/**
	 * Adds a object of type {@link Primitive} to the list of objects that will
	 * be hided when this state is {@link #deactivate()} is called on this
	 * object.
	 * 
	 * @param hideme
	 *            the object to hide, should <b>not</b> be <code>null</code>.
	 */
	protected void hideOnDeactivate(Primitive hideme) {
		assert hideme != null;
		toHide.add(hideme);
	}

}
