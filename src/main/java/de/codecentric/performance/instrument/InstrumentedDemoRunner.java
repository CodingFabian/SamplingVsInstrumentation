package de.codecentric.performance.instrument;

import org.aspectj.lang.Aspects;

import de.codecentric.performance.DemoRunner;

/**
 * Note, this requires an aspectjweaver as javaagent:
 * -javaagent:C:\Users\fabian.lange\.m2\repository\org\aspectj\aspectjweaver\1.6.11\aspectjweaver-1.6.11.jar
 */
public class InstrumentedDemoRunner extends DemoRunner {

	public static void main(final String[] args) {
		instrumentDemo(DemoType.MIXED);
		instrumentDemo(DemoType.MASS);
	}

	private static void instrumentDemo(final DemoType type) {
		long startMain = System.currentTimeMillis();
		TraceAspect trace = Aspects.aspectOf(TraceAspect.class);
		trace.init();

		long startCode = System.currentTimeMillis();

		runDemo(type);

		long endCode = System.currentTimeMillis();
		System.out.printf("%s Demo completed in %dms%n", type, endCode - startCode);

		trace.printStatistics();
		long endMain = System.currentTimeMillis();
		System.out.printf("Agent Overhead %dms%n", (endMain - startMain) - (endCode - startCode));
	}
}