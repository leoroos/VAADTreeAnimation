package de.lere.vaad.treebuilder.events;

import de.lere.vaad.treebuilder.BinaryTreeModel;
import de.lere.vaad.treebuilder.BinaryTreeModel.StatisticResult;
import de.lere.vaad.treebuilder.Node;

public class TreeSearchEvent<T extends Comparable<T>> extends TreeModelChangeEvent<T> {

	public final StatisticResult statistics;
	public final T searchVal;

	public TreeSearchEvent(BinaryTreeModel<T> before, BinaryTreeModel<T> after,
			Node<T> nodeOfModification, StatisticResult statistics, T searchVal) {
		super(before, after, nodeOfModification);
		this.statistics = statistics;
		this.searchVal = searchVal;
	}

	@Override
	public void notifyListener(TreeEventListener<T> listener) {
		listener.update(this);
	}

}
