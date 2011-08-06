package de.lere.vaad.treebuilder;

/**
 * The sacred class of Node ordering.
 * 
 * @author Leo Roos, Rene Hertling
 *
 */
public class NodeOrder {

	public static <T extends Comparable<T>> boolean isLeftChild(T current, T child) {
		return current.compareTo(child) >= 0;
	}
	
}
