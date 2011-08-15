package de.lere.vaad.animation.splaytree;

public interface SplayTreeStepListener<T extends Comparable<T>> {

	void newStep(SplayStep<T> step);
}
