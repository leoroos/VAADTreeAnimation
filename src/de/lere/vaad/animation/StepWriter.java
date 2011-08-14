package de.lere.vaad.animation;

import algoanim.primitives.generators.Language;

public class StepWriter {

	private final Language lang;
	private static int stepCounter = 0;

	public StepWriter(Language lang) {
		this.lang = lang;
	}

	public void step() {
		doStep(null);
	}

	public void step(String string) {
		doStep(string);
	}

	private void doStep(String string) {
		stepCounter++;
		if (string == null) {
			lang.nextStep();
		} else {
			lang.nextStep(string /*+ stepCounter*/);
		}
	}

	public static int getStepCounter() {
		return stepCounter;
	}
}
