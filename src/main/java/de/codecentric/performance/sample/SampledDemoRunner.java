package de.codecentric.performance.sample;

import de.codecentric.performance.DemoRunner;
import de.codecentric.performance.util.Time;

public class SampledDemoRunner extends DemoRunner {

	private static final int SAMPLING_INTERVAL_10MS = 10;

	public static void main(final String[] args) {
		sampleDemo(DemoType.MIXED);
		sampleDemo(DemoType.MASS);
	}

	private static void sampleDemo(final DemoType type) {
		long startMain = System.nanoTime();
		Sampler sampler = new Sampler("main", SAMPLING_INTERVAL_10MS);

		long startCode = System.nanoTime();
		runDemo(type);
		long endCode = System.nanoTime();
		System.out.printf("%s Demo completed in %dms%n", type, Time.nsToMs(endCode - startCode));

		sampler.printStatistics();
		long endMain = System.nanoTime();
		System.out.printf("Agent Overhead %dms%n", Time.nsToMs((endMain - startMain) - (endCode - startCode)));
	}

}
