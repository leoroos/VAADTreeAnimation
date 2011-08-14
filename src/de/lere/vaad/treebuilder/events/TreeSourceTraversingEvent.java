package de.lere.vaad.treebuilder.events;

import java.util.Hashtable;

/**
 * common class for all source traversing events. Subclasses using enums to
 * indicate their concrete code traversing position should put them into the
 * sourceCodePosition Map, s.t. other classes can find out about their logical
 * position.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <T>
 */
public abstract class TreeSourceTraversingEvent<T extends Comparable<T>>
		extends TreeEvent<T> {

	/**
	 * Workaround to use enums as a identifier to from a unknown subclass to
	 * know about it's logical code position
	 */
	private static Hashtable<Object, Integer> sourceCodePosition = new Hashtable<Object, Integer>();

	protected static void putEnumPos(Object enumel, Integer pos) {
		sourceCodePosition.put(enumel, pos);
	}

	/**
	 * @param sourceTraversingEnum
	 * @return get position (not line) of the involved enum. The position start
	 *         with one not zero.
	 */
	public static Integer getSourceCodePosition(Object sourceTraversingEnum) {
		return sourceCodePosition.get(sourceTraversingEnum);
	}
	
	/**
	 * @return the object that indicates the code position. Can be used in {@link #getSourceCodePosition(Object)}
	 */
	public abstract Object getCodePosition();

}
