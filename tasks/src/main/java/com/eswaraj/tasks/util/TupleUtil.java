package com.eswaraj.tasks.util;

import backtype.storm.Constants;
import backtype.storm.tuple.Tuple;

public final class TupleUtil {
	/**
	 * Helps us check the tuple to see if it's tick tuple or not. 
	 * Read about tick tuples on https://groups.google.com/forum/#!msg/storm-user/8addaQm3OT4/0OQfSgQkRwEJ point number 6
	 * @param tuple
	 * @return
	 */
	public static boolean isTickTuple(Tuple tuple) {
		return tuple.getSourceComponent().equals(Constants.SYSTEM_COMPONENT_ID) 
				&& tuple.getSourceStreamId().equals(Constants.SYSTEM_TICK_STREAM_ID);
	}
}
