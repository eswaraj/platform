package com.eswaraj.tasks.bolt.extras;

import backtype.storm.utils.Time;
import org.apache.commons.collections.buffer.CircularFifoBuffer;

/**
 * Track time (in milliseconds) since last update
 * @author anuj
 * @data Jul 26, 2014
 */

public class LastModifiedSlotTracker {

	private CircularFifoBuffer lastUpdateTracker;

	public LastModifiedSlotTracker(int trackerSize) {
		if(trackerSize > 0 ) {
			lastUpdateTracker = new CircularFifoBuffer(trackerSize);
			initialiseLastUpdateTracker();
		} else {
			throw new IllegalArgumentException(" trackerSize of " + trackerSize + ")");
		}
	}
	
	public int getTimeSinceLastModificationSecs() {
		long lastModifiedTimeMillis = ((Long) lastUpdateTracker.get()).longValue();
		//convert to seconds because we accept the input from user in seconds
		return (int) ((getCurrentTimeMillis() - lastModifiedTimeMillis) / 1000);
	}

	public void setLastModifiedTime() {
		lastUpdateTracker.add(getCurrentTimeMillis());
	}

	private void initialiseLastUpdateTracker() {
		long initTime = getCurrentTimeMillis();
		for (int i = 0; i < lastUpdateTracker.maxSize(); i++) {
			lastUpdateTracker.add(Long.valueOf(initTime));
		}
	}

	private long getCurrentTimeMillis() {
		return Time.currentTimeMillis();
	}
}