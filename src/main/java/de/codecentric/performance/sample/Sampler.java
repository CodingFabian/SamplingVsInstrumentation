package de.codecentric.performance.sample;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.codecentric.performance.util.MethodStatistics;
import de.codecentric.performance.util.MethodStatisticsHelper;
import de.codecentric.performance.util.ThreadFinder;

public class Sampler {

	private final Thread agentThread;
	private final Agent agent;

	public Sampler(final String threadName, final long sampling) {
		agent = new Agent(threadName, sampling);
		agentThread = new Thread(agent, "Sampling Agent");
		agentThread.setDaemon(true);
		agentThread.start();
	}

	public void printStatistics() {
		agent.stop();
	}

	private static class Agent implements Runnable {
		private static final int MAX_EXECUTION_PATH = 100;

		private final long interval;
		private final Thread monitoredThread;
		private final LinkedList<String> executionPath;
		private final Map<String, MethodStatistics> methodStatistics;

		private long overhead = 0;
		private long lastSample;
		private String lastMethod;

		public Agent(final String thread, final long samplingInterval) {
			System.out.printf("Agent monitoring thread %s with sampling interval of %dms%n", thread, samplingInterval);
			interval = samplingInterval;
			monitoredThread = new ThreadFinder().findThread(thread);
			executionPath = new LinkedList<String>();
			methodStatistics = new ConcurrentHashMap<String, MethodStatistics>();
		}

		public void stop() {
			System.out.printf("Agent stopped - Results:%n");
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

		@Override
		public void run() {
			lastSample = System.currentTimeMillis();
			while (true) {
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}

				String currentMethod = getCurrentMethod();
				long currentSample = System.currentTimeMillis();

				addMeasurementsIfStillInMethod(currentMethod, currentSample);

				lastMethod = currentMethod;
				lastSample = currentSample;

				overhead += System.currentTimeMillis() - currentSample;
			}
		}

		private void addMeasurementsIfStillInMethod(final String currentMethod, final long currentSample) {
			if (currentMethod.equals(lastMethod)) {
				MethodStatistics statistics = methodStatistics.get(currentMethod);
				if (statistics == null) {
					statistics = new MethodStatistics(currentMethod);
					methodStatistics.put(currentMethod, statistics);
				}
				statistics.addTime(currentSample - lastSample);
			} else {
				if (executionPath.size() < MAX_EXECUTION_PATH) {
					executionPath.add(getParentMethod() + " > " + currentMethod);
				}
			}
		}

		private String getCurrentMethod() {
			StackTraceElement topOfStack = monitoredThread.getStackTrace()[0];
			return formatStackElement(topOfStack);
		}

		private String getParentMethod() {
			StackTraceElement parentOfTopOfStack = monitoredThread.getStackTrace()[1];
			return formatStackElement(parentOfTopOfStack);
		}

		private String formatStackElement(final StackTraceElement topOfStack) {
			// match results of Aspect output for demo purpose
			return "void " + topOfStack.getClassName() + "." + topOfStack.getMethodName() + "()";
		}
	}

}
