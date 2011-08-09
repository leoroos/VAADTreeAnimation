package de.lere.vaad.treebuilder;

import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public class StepTiming {
	private TicksTiming now; 
	private TicksTiming interval;
	
	public StepTiming(TicksTiming start, TicksTiming interval){
		this.now = start;
		this.interval = interval;
	}
	
	public TicksTiming now() {
		return now;
	}
	
	public TicksTiming getInterval() {
		return interval;
	}

	public TicksTiming newInterval() {
		now = new TicksTiming(now.getDelay() + interval.getDelay());
		return now;
	}	
}
