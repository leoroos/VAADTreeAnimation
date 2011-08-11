package de.lere.vaad.treebuilder;

/**
 * The <i><b>sacred</b></i> class of Node ordering.
 * 
 * @author Leo Roos, Rene Hertling
 *
 */
public class NodeOrder {

	public static <T extends Comparable<T>> boolean isChildConsideredLeft(T current, T child) {
		return current.compareTo(child) >= 0;
	}
	
}
