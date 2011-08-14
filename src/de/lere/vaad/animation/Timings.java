package de.lere.vaad.animation;

import algoanim.util.TicksTiming;

public final class Timings {

	private static final double default_now = 0;
	private static final double default_short = 25;
	private static final double default_default = 50;
	private static final double default_long = 100;
	private final double multiplier;

	/**
	 * Uses default delay setting
	 */
	public Timings() {
		this(1);
	}

	/**
	 * @param multiplier
	 *            uses the multiplier to adjust the animations accordingly
	 */
	public Timings(double multiplier) {
		this.multiplier = multiplier;
		NOW = getWihtMultiplier(default_now);
		SHORT_ANIMATION = getWihtMultiplier(default_short);
		DEFAULT_ANIMATION = getWihtMultiplier(default_default);
		LONG_ANIMATION = getWihtMultiplier(default_long);

	}

	private TicksTiming getWihtMultiplier(double defaultNowDelay) {
		double delay = defaultNowDelay * multiplier;
		return new TicksTiming((int) delay);
	}

	public final TicksTiming NOW;
	public final TicksTiming SHORT_ANIMATION;
	public final TicksTiming DEFAULT_ANIMATION;
	public final TicksTiming LONG_ANIMATION;
}
