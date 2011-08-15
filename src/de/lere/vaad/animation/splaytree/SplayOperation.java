package de.lere.vaad.animation.splaytree;

import java.util.ArrayList;
import java.util.List;

final class SplayOperation<T extends Comparable<T>> {
	private List<SplayStep<T>> operationSteps;

	public SplayOperation() {
		operationSteps = new ArrayList<SplayStep<T>>();
	}

	public void addAll(List<SplayStep<T>> steps) {
		operationSteps.addAll(steps);
	}
	
	public List<SplayStep<T>> getOperationSteps() {
		return operationSteps;
	}
}