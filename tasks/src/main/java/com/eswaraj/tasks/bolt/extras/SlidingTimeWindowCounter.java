package com.eswaraj.tasks.bolt.extras;

import java.io.Serializable;
import java.util.Map;

/**
 * @author anuj
 * @data Jul 26, 2014
 */
public final class SlidingTimeWindowCounter implements Serializable {

	private static final long serialVersionUID = 1L;

	private SlotBasedCounter slotBasedCounter;
	private int headSlot;
	private int tailSlot;
	private int windowLengthInSlots;

	public SlidingTimeWindowCounter(int windowLengthInSlots) {
		if (windowLengthInSlots < 2) {
			throw new IllegalArgumentException( "Window length in slots must be at least two (you requested " + windowLengthInSlots + ")");
		}
		this.windowLengthInSlots = windowLengthInSlots;
		this.slotBasedCounter = new SlotBasedCounter(this.windowLengthInSlots);

		this.headSlot = 0;
		this.tailSlot = slotAfter(headSlot);
	}

	public void incrementCount(String key) {
		slotBasedCounter.incrementCount(key, headSlot);
	}

	public Map<String, Long> getCountsThenAdvanceWindow() {
		Map<String, Long> counts = slotBasedCounter.getCounters();
		slotBasedCounter.removeCounters();
		slotBasedCounter.clearCounters(tailSlot);
		advanceHead();
		return counts;
	}

	private void advanceHead() {
		headSlot = tailSlot;
		tailSlot = slotAfter(tailSlot);
	}

	private int slotAfter(int slot) {
		return (slot + 1) % windowLengthInSlots;
	}
}
