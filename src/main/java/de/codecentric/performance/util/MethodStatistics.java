package de.codecentric.performance.util;

public class MethodStatistics implements Comparable<MethodStatistics> {

	private final String currentMethod;
	private long time = 0;
	private long cpuTime = 0;

	public MethodStatistics(final String method) {
		this.currentMethod = method;
	}

	public void addTime(final long timeSpent) {
		this.time += timeSpent;
	}

	public void addCPUTime(final long cpuTimeSpent) {
		this.cpuTime += cpuTimeSpent;
	}

	@Override
	public String toString() {
		return String.format("%s %dms %dms", currentMethod, Time.nsToMs(time), Time.nsToMs(cpuTime));
	}

	@Override
	public int compareTo(final MethodStatistics o) {
		return (Long.compare(time, o.time));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentMethod == null) ? 0 : currentMethod.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodStatistics other = (MethodStatistics) obj;
		if (currentMethod == null) {
			if (other.currentMethod != null)
				return false;
		} else if (!currentMethod.equals(other.currentMethod))
			return false;
		if (time != other.time)
			return false;
		return true;
	}

}
