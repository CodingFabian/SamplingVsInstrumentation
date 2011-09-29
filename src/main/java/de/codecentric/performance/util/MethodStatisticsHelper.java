package de.codecentric.performance.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public abstract class MethodStatisticsHelper {

	public static ArrayList<MethodStatistics> sortDescendingByTime(final Collection<MethodStatistics> collection) {
		ArrayList<MethodStatistics> list = new ArrayList<MethodStatistics>(collection);
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}

}
