package de.lere.vaad.treebuilder;

import algoanim.util.TicksTiming;
import algoanim.util.Timing;

public final class Timings {

	public static final TicksTiming NOW = new TicksTiming(0);
	public static final TicksTiming _25_TICKS = new TicksTiming(25);

	public static final TicksTiming DELETE_NODE_HIGHLIGHT_DURATION = new TicksTiming(
			25);
	
	public static final TicksTiming getNextIncrementalTimingFor(TicksTiming base){
		return new TicksTiming(base.getDelay() + _25_TICKS.getDelay());
	}
}