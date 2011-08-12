package de.lere.vaad.animation;

import algoanim.util.TicksTiming;
import algoanim.util.Timing;

/**
 * Helper class to handle complicated animations use multiple steps.
 * 
 * @author Leo Roos, Rene Hertling
 * 
 */
public class ProgressTiming {
	private TicksTiming now;
	private final TicksTiming interval;

	public ProgressTiming(TicksTiming start, TicksTiming interval) {
		this.now = start;
		this.interval = interval;
	}

	public TicksTiming now() {
		return now;
	}

	public TicksTiming getInterval() {
		return interval;
	}

	/**
	 * @param interval progress by special interval
	 * @return
	 */
	public TicksTiming progress(int interval) {
		now = new TicksTiming(now.getDelay() + interval);
		return new TicksTiming(interval);
	}
	
	/**
	 * @param interval progress by special interval
	 * @return
	 */
	public TicksTiming progress(TicksTiming interval) {
		return this.progress(interval.getDelay());
	}

	/**
	 * increase progress by one additional interval step and return the interval
	 * amount by which the timing has progressed.
	 * 
	 * @return the progressed interval, i.e. the interval with which it has been initialized.
	 */
	public TicksTiming progress() {
		return progress(interval.getDelay());
	}
}
