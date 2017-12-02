package de.codecentric.performance.util;

public class MoreMethodStatistics extends MethodStatistics {

	private long count = 0;

	private long min = Long.MAX_VALUE;
	private long max = Long.MIN_VALUE;

	public MoreMethodStatistics(final String currentMethod) {
		super(currentMethod);
	}

	@Override
	public void addTime(final long timeSpent) {
		min = Math.min(min, timeSpent);
		max = Math.max(max, timeSpent);
		super.addTime(timeSpent);
		count++;
	}

	@Override
	public String toString() {
		return super.toString() + String.format(" (min: %dms, max: %dms) - %d invocations", Time.nsToMs(min), Time.nsToMs(max), count);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (count ^ (count >>> 32));
		result = prime * result + (int) (max ^ (max >>> 32));
		result = prime * result + (int) (min ^ (min >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoreMethodStatistics other = (MoreMethodStatistics) obj;
		if (count != other.count)
			return false;
		if (max != other.max)
			return false;
		if (min != other.min)
			return false;
		return true;
	}

}
