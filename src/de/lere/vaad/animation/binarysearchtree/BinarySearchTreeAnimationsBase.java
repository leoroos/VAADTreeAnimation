package de.lere.vaad.animation.binarysearchtree;

import javax.annotation.Nullable;

import de.lere.vaad.animation.GraphWriter;
import de.lere.vaad.animation.SourceCodeWriter;
import de.lere.vaad.animation.StepWriter;
import de.lere.vaad.animation.Timings;
import de.lere.vaad.animation.locationhandler.LocationDirector;
import de.lere.vaad.animation.locationhandler.LocationHandler;
import de.lere.vaad.treebuilder.events.TreeEvent;
import de.lere.vaad.treebuilder.events.TreeEventListener;
import de.lere.vaad.treebuilder.events.TreeModelChangeEvent;
import de.lere.vaad.treebuilder.events.TreeSearchEvent;
import de.lere.vaad.treebuilder.events.TreeSourceTraversingEvent;
import de.lere.vaad.utils.TextLoaderUtil;

public abstract class BinarySearchTreeAnimationsBase<T extends Comparable<T>, ModelChangeEvent extends TreeModelChangeEvent<T>, SourceTraversingEvent extends TreeSourceTraversingEvent<T>>
		implements TreeEventListener<T> {

	protected Timings ts;
	protected SourceCodeWriter sourceWriter;
	protected StepWriter stepper;
	protected LocationDirector<?> coarseDescription;
	protected LocationDirector<?> fineDescription;
	protected LocationHandler lh;
	protected GraphWriter<T> graphWriter;

	public BinarySearchTreeAnimationsBase(BinarySearchTreeSetup<T> p) {
		p.validate();
		this.ts = p.getBinaryTreeProperties().getTimings();
		SourceCodeWriter scw = p.getSourceCodeWriter();
		TextLoaderUtil loaderUtil = new TextLoaderUtil(getClass(), "resources");
		scw.newSourceCode(loaderUtil.getText(animatedAlgorithm()), false);
		scw.setAutomaticLineUnhighlighting(true);
		sourceWriter = scw;
		stepper = p.getStepWriter();
		coarseDescription = p.getCoarseDescription();
		fineDescription = p.getFineDescription();
		lh = p.getLocationHandler();
		graphWriter = p.getWriter();
	}

	protected abstract String animatedAlgorithm();

	protected void fine(String string) {
		this.lh.nextStateOnLocation(string, fineDescription);
	}

	protected void coarse(String string) {
		this.lh.nextStateOnLocation(string, coarseDescription);
	}

	protected void step() {
		stepper.step();
	}

	protected void step(String string) {
		stepper.step(string);
	}

	protected void animationStepper(TreeEvent<T> event) {
		if (getSourceTraversingClass().isInstance(event)) {
			SourceTraversingEvent ie = getSourceTraversingClass().cast(event);
			step(steptextOnSourceTraversingEvent(ie));
		} else if (getModelChangeEventClass().isInstance(event)) {
			ModelChangeEvent ie = getModelChangeEventClass().cast(event);
			step(steptextOnModificationEvent(ie));
		}
	}

	protected void sourceCodeVisibility(TreeEvent<T> event) {
		if (getSourceTraversingClass().isInstance(event)) {
			SourceTraversingEvent ie = getSourceTraversingClass().cast(event);
			// FIXME(Leo) could (should) be better solved by using different
			// modification events which would 
			// indicate whether an algorithm execution started or finished.
			Object codePosition = ie.getCodePosition();
			Integer sourceCodePosition = TreeSourceTraversingEvent
					.getSourceCodePosition(codePosition);
			if (sourceCodePosition < 2) {
				this.sourceWriter.show(ts.DEFAULT_ANIMATION);
			}
		} else if (getModelChangeEventClass().isInstance(event)) {
			this.sourceWriter.hide(ts.DEFAULT_ANIMATION);
		}
	}

	@Override
	public void update(TreeEvent<T> event) {
		sourceCodeVisibility(event);

		if (getModelChangeEventClass().isInstance(event)) {
			modelModificationHandling(getModelChangeEventClass().cast(event));
		}
		if (getSourceTraversingClass().isInstance(event)) {
			sourceCodeTraversion(getSourceTraversingClass().cast(event));
		}

		/* stepper last works best */
		animationStepper(event);
	}

	protected abstract void sourceCodeTraversion(SourceTraversingEvent event);

	protected abstract void modelModificationHandling(ModelChangeEvent event);

	abstract protected @Nullable
	String steptextOnSourceTraversingEvent(SourceTraversingEvent ie);

	abstract protected String steptextOnModificationEvent(ModelChangeEvent ie);

	protected abstract Class<? extends ModelChangeEvent> getModelChangeEventClass();

	protected abstract Class<? extends SourceTraversingEvent> getSourceTraversingClass();

}