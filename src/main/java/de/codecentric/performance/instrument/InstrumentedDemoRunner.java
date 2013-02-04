package de.codecentric.performance.instrument;

import org.aspectj.lang.Aspects;

import de.codecentric.performance.DemoRunner;
import de.codecentric.performance.util.Time;

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
		long startMain = System.nanoTime();
		TraceAspect trace = Aspects.aspectOf(TraceAspect.class);
		trace.init();

		long startCode = System.nanoTime();

		runDemo(type);

		long endCode = System.nanoTime();
		System.out.printf("%s Demo completed in %dms%n", type,
				Time.nsToMs(endCode - startCode));

		trace.printStatistics();
		long endMain = System.nanoTime();
		System.out.printf("Agent Overhead %dms%n",
				Time.nsToMs((endMain - startMain) - (endCode - startCode)));
	}
}