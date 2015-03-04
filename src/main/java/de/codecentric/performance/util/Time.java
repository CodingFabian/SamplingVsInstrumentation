package de.codecentric.performance.util;

public class Time {

	public static final long nsToMs(long nanoseconds) {
		return nanoseconds / 1_000_000;
	}

}
