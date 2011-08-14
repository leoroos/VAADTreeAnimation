package de.lere.vaad.treebuilder;

/**
 * <h1><span style="font-variant:small-caps">Order Of The Equal Nodes</span></h1>
 * <p>
 * The <i><b>sacred</b></i> class of the <em>holy</em> <u>decision</u> of
 * whether to put a Node that is equal, relative to its
 * <i><b><u>value</u></b></i>, to the <span
 * style="font-size:xx-small">left</span> or the <span
 * style="font-size:xx-large">right</span> of its <span
 * style="font-variant:small-caps">parent</span>.
 * </p>
 * 
 * @author Leo Roos, Rene Hertling
 * 
 */
public class NodeOrder {

	/**
	 * The usual behavior is to put equal nodes to the right. The reason for
	 * that is because that's the way Cormen et al. do it in <i>Introduction to
	 * Algorithms</i>
	 * 
	 * @param <T>
	 * @param parent the node underneath which the <code>child</code> should be inserted.
	 * @param child the node to insert beneath the <code>parent</code>.
	 * @return
	 */
	public static <T extends Comparable<T>> boolean isEqualChildConsideredLeft(
			T parent, T child) {
		return child.compareTo(parent) < 0;
	}

}
