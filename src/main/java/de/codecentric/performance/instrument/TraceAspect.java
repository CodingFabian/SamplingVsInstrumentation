package de.codecentric.performance.instrument;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import de.codecentric.performance.util.MethodStatistics;
import de.codecentric.performance.util.MethodStatisticsHelper;
import de.codecentric.performance.util.MoreMethodStatistics;
import de.codecentric.performance.util.Time;

/**
 * Note: this Aspect is for demo purpose only and is not threadsafe!
 */
@Aspect
public final class TraceAspect {

    private Map<String, MethodStatistics> methodStatistics;
    private long overhead;
    private ThreadMXBean threadMXBean;

    public void init() {
        methodStatistics = new HashMap<>();
        overhead = 0;
        threadMXBean = ManagementFactory.getThreadMXBean();
    }

    /**
     * Note: This Advice is for demo only. It ignores return values and does not handle Exceptions correctly.
     */
    @Around("   call(void de.codecentric.performance.Demo.* (..)) ")
    public void aroundDemoMethodCall(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
        long cpuStart = threadMXBean.getCurrentThreadCpuTime();
        long start = System.nanoTime();
        thisJoinPoint.proceed();
        long end = System.nanoTime();
        long cpuEnd = threadMXBean.getCurrentThreadCpuTime();
        String currentMethod = thisJoinPoint.getSignature().toString();

        MethodStatistics statistics = methodStatistics.get(currentMethod);
        if (statistics == null) {
            statistics = new MoreMethodStatistics(currentMethod);
            methodStatistics.put(currentMethod, statistics);
        }
        statistics.addTime(end - start);
        statistics.addCPUTime(cpuEnd - cpuStart);
        overhead += System.nanoTime() - end;
    }

    public void printStatistics() {
        System.out.printf("Trace Aspect recorded following results:%n");
        List<MethodStatistics> statistics = MethodStatisticsHelper.sortDescendingByTime(methodStatistics.values());

        for (MethodStatistics statistic : statistics) {
            System.out.println(statistic);
        }

        System.out.printf("Code Execution Path:%n");
        for (String method : methodStatistics.keySet()) {
            System.out.println(method);
        }

        System.out.printf("Agent internal Overhead %dms%n", Time.nsToMs(overhead));
    }

}
