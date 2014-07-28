package com.eswaraj.tasks.bolt.extras;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author anuj
 * @data Jul 26, 2014
 */

public final class SlotBasedCounter implements Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<String, long[]> counters = new HashMap<String, long[]>();
	private final int numSlots;

	public SlotBasedCounter(int numSlots) {
		if (numSlots <= 0) {
			throw new IllegalArgumentException("Nothing to count. Please use a number greater than zero");
		}
		this.numSlots = numSlots;
	}

	public void incrementCount(String key, int slot) {
		long[] counts = counters.get(key);
		if (counts == null) {
			counts = new long[this.numSlots];
			counters.put(key, counts);
		}
		counts[slot]++;
	}

	public long getCount(String key, int slot) {
		long[] counts = counters.get(key);
		return (counts == null) ? 0 : counts[slot]; 
	}

	public Map<String, Long> getCounters() {
		//TODO need to change this
		Map<String, Long> result = new HashMap<String, Long>();
		for (String key : counters.keySet()) {
			result.put(key, getTotalCount(key));
		}
		return result;
	}

	private long getTotalCount(String key) {
		long[] curr = counters.get(key);
		long total = 0;
		for (long l : curr) {
			total += l;
		}
		return total;
	}

	public void clearCounters(int slot) {
		for (String key : counters.keySet()) {
			resetCounter(key, slot);
		}
	}

	private void resetCounter(String key, int slot) {
		long[] counts = counters.get(key);
		counts[slot] = 0;
	}

	private boolean shouldBeRemovedFromCounter(String key) {
		return getTotalCount(key) == 0;
	}

	public void removeCounters() {
		Set<String> counterKeysToBeRemoved = new HashSet<String>();
		for (String key : counters.keySet()) {
			if (shouldBeRemovedFromCounter(key)) {
				counterKeysToBeRemoved.add(key);
			}
		}
		for (String key : counterKeysToBeRemoved) {
			counters.remove(key);
		}
	}
}
