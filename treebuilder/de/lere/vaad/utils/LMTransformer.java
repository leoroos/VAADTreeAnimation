package de.lere.vaad.utils;

/**
 * Transformer class used by {@link ListMap}. This interface is implemented by
 * users of {@link ListMap} to provide the desired transformation functionality
 * for each list element.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 * @param <A>
 *            transform from this type
 * @param <B>
 *            transform to this type
 */
public interface LMTransformer<A, B> {

	B transform(A input);
}
