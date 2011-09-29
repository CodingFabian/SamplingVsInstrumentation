package de.codecentric.performance.sample;

import de.codecentric.performance.DemoRunner;

public class SampledDemoRunner extends DemoRunner {

	private static final int SAMPLING_INTERVAL_10MS = 10;

	public static void main(final String[] args) {
		sampleDemo(DemoType.MIXED);
		sampleDemo(DemoType.MASS);
	}

	private static void sampleDemo(final DemoType type) {
		long startMain = System.currentTimeMillis();
		Sampler sampler = new Sampler("main", SAMPLING_INTERVAL_10MS);

		long startCode = System.currentTimeMillis();
		runDemo(type);
		long endCode = System.currentTimeMillis();
		System.out.printf("%s Demo completed in %dms%n", type, endCode - startCode);

		sampler.printStatistics();
		long endMain = System.currentTimeMillis();
		System.out.printf("Agent Overhead %dms%n", (endMain - startMain) - (endCode - startCode));
	}

}
