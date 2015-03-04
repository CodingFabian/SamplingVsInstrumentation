package de.codecentric.performance;

import java.util.Arrays;

import de.codecentric.performance.util.Time;

public class DemoRunner {

	public static void main(final String[] args) {
		long start = System.nanoTime();
		mixedDemo();
		long end = System.nanoTime();
		System.out.printf("%s Demo completed in %dms%n", DemoType.MIXED, Time.nsToMs(end - start));

		start = System.nanoTime();
		massDemo();
		end = System.nanoTime();
		System.out.printf("%s Demo completed in %dms%n", DemoType.MASS, Time.nsToMs(end - start));
	}

	/**
	 * short exercise running methods with different lengths: 100, 1, 100, 500, 1, 100, 1, 50, 50
	 */
	protected static void mixedDemo() {
		int[] methods = new int[] { 100, 1, 100, 500, 1, 100, 1, 50, 50 };
		System.out.printf("Running Demo with %s methods%n", Arrays.toString(methods));
		new Demo().runCode(methods);
	}

	private static final int MASS_1MS_CALL_COUNT = 100000000;

	/**
	 * long exercise running fast method 100000000 times.
	 */
	protected static void massDemo() {
		System.out.printf("%nRunning Demo with %d 0ms methods%n", MASS_1MS_CALL_COUNT);
		new Demo().runFastCode(MASS_1MS_CALL_COUNT);
	}

	protected enum DemoType {
		MIXED, MASS
	}

	protected static void runDemo(final DemoType mixed) {
		switch (mixed) {
		case MIXED:
			mixedDemo();
			break;
		case MASS:
			massDemo();
			break;

		default:
			break;
		}
	}
}
