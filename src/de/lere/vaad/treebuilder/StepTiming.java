package de.lere.vaad.treebuilder;

import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class StepTiming {
	private TicksTiming now;
	private TicksTiming interval;

	public StepTiming(TicksTiming start, TicksTiming interval) {
		this.now = start;
		this.interval = interval;
	}

	public TicksTiming now() {
		return now;
	}

	public TicksTiming getInterval() {
		return interval;
	}

	public Timing newInterval(int interval) {
		now = new TicksTiming(now.getDelay() + interval);
		return new TicksTiming(interval);
	}
}
