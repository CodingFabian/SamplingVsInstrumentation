package de.codecentric.performance.util;

/**
 * Thread Finder original code from: http://www.exampledepot.com/egs/java.lang/ListThreads.html - Cleaned and modified.
 * The code contains a few quirks of the thread API. activeCount should not be used as number of threads, but the return
 * value from enumerate.
 */
public class ThreadFinder {

	private final ThreadGroup threadRoot;

	public ThreadFinder() {
		this.threadRoot = getRootThreadGroup();
	}

	public Thread findThread(final String threadName) {
		Thread thread = searchThreadsInGroup(threadName, threadRoot, 0);
		if (thread == null) {
			throw new RuntimeException("Agent cannot find thread " + threadName);
		}
		return thread;
	}

	private Thread searchThreadsInGroup(final String threadName, final ThreadGroup group, final int level) {
		Thread thread = null;

		Thread[] threads = new Thread[group.activeCount() * 2];
		int numThreads = group.enumerate(threads, false);

		for (int i = 0; i < numThreads; i++) {
			if (threads[i].getName().equals(threadName)) {
				thread = threads[i];
			}
		}

		if (thread == null) {
			thread = searchThreadsInSubGroup(threadName, group, level);
		}
		return thread;
	}

	private Thread searchThreadsInSubGroup(final String threadName, final ThreadGroup group, final int level) {
		ThreadGroup[] groups = new ThreadGroup[group.activeGroupCount() * 2];
		int numGroups = group.enumerate(groups, false);

		Thread thread = null;
		for (int i = 0; i < numGroups && thread == null; i++) {
			thread = searchThreadsInGroup(threadName, groups[i], level + 1);
		}
		return thread;
	}

	private static ThreadGroup getRootThreadGroup() {
		ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
		while (root.getParent() != null) {
			root = root.getParent();
		}
		return root;
	}
}
