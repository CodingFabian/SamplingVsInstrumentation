package de.codecentric.performance.instrument;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.codecentric.performance.util.MethodStatistics;
import de.codecentric.performance.util.MethodStatisticsHelper;
import de.codecentric.performance.util.MoreMethodStatistics;

/**
 * Note: this Aspect is for demo purpose only and is not threadsafe!
 */
@Aspect
public final class TraceAspect {

	private static final int MAX_EXECUTION_PATH = 100;
	private Map<String, MethodStatistics> methodStatistics;
	private LinkedList<String> executionPath;
	private long overhead;

	public void init() {
		methodStatistics = new HashMap<String, MethodStatistics>();
		executionPath = new LinkedList<String>();
		overhead = 0;
	}

	/**
	 * Note: This Advice is for demo only. It ignores return values and does not handle Exceptions correctly.
	 */
	@Around("   call(void de.codecentric.performance.Demo.method* (..)) ")
	public void aroundDemoMethodCall(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		thisJoinPoint.proceed();
		long end = System.currentTimeMillis();

		String currentMethod = thisJoinPoint.getSignature().toString();
		if (executionPath.size() < MAX_EXECUTION_PATH) {
			executionPath.add(currentMethod);
		}
		MethodStatistics statistics = methodStatistics.get(currentMethod);
		if (statistics == null) {
			statistics = new MoreMethodStatistics(currentMethod);
			methodStatistics.put(currentMethod, statistics);
		}
		statistics.addTime(end - start);
		overhead += System.currentTimeMillis() - end;
	}

	public void printStatistics() {
		System.out.printf("Trace Aspect recorded following results:%n");
		List<MethodStatistics> statistics = MethodStatisticsHelper.sortDescendingByTime(methodStatistics.values());

		for (MethodStatistics statistic : statistics) {
			System.out.println(statistic);
		}
		System.out.printf("Code Execution Path:%n");
		for (String method : executionPath) {
			System.out.println(method);
		}
		if (executionPath.size() == MAX_EXECUTION_PATH) {
			System.out.println("Execution Path incomplete!");
		}

		System.out.printf("Agent internal Overhead %dms%n", overhead);
	}

}
