package com.eswaraj.tasks.bolt;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.tasks.bolt.extras.LastModifiedSlotTracker;
import com.eswaraj.tasks.bolt.extras.SlidingTimeWindowCounter;
import com.eswaraj.tasks.util.TupleUtil;

/**
 * 
 * @author anuj
 * @data Jul 25, 2014
 */

public class SlidingTimeWindowCounterBolt extends BaseRichBolt {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(SlidingTimeWindowCounterBolt.class);
	
	private final SlidingTimeWindowCounter counter;
	private final int windowLengthInSeconds;
	private final int emitFrequencyInSeconds;
	private OutputCollector collector;
	private LastModifiedSlotTracker lastModifiedSlotTracker;
	private String key;
	private int updateSlots;
	
	private Class clazz;
	private Jedis jedis;

	public SlidingTimeWindowCounterBolt(String key, int windowLengthInSeconds, int emitFrequencyInSeconds, Class clazz) {
		this.clazz = clazz;
		this.key = key;
		this.windowLengthInSeconds = windowLengthInSeconds;
		this.emitFrequencyInSeconds = emitFrequencyInSeconds;
		this.updateSlots = windowLengthInSeconds / emitFrequencyInSeconds;
		counter = new SlidingTimeWindowCounter(updateSlots);
		lastModifiedSlotTracker = new LastModifiedSlotTracker(updateSlots);
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple tuple) {
		if (TupleUtil.isTickTuple(tuple)) {
			emitAndUpdate();
		} else {
			countAndAcknowledge(tuple);
		}
	}
	
	//TODO this might need changing and made to be more generic.
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("key", "count", "actualWindowLengthInSeconds"));
	}

	//TODO need to use the component configuration in a clever way
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> conf = new HashMap<String, Object>();
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds);
		return conf;
	}
	
	private void countAndAcknowledge(Tuple tuple) {
		//TODO maybe need to do a tuple check
		counter.incrementCount(key);
		collector.ack(tuple);
	}

	private void emitAndUpdate() {
		Map<String, Long> counts = counter.getCountsThenAdvanceWindow();
		int actualWindowLengthInSeconds = lastModifiedSlotTracker.getTimeSinceLastModificationSecs();
		lastModifiedSlotTracker.setLastModifiedTime();
		if (actualWindowLengthInSeconds != windowLengthInSeconds) {
			LOG.warn(String.format("Sliding window is falling behind by %d seconds", actualWindowLengthInSeconds - windowLengthInSeconds));
		}
		emit(counts, actualWindowLengthInSeconds);
	}

	private void emit(Map<String, Long> counts, int actualWindowLengthInSeconds) {
		for (Entry<String, Long> entry : counts.entrySet()) {
			String key = entry.getKey();
			Long count = entry.getValue();
			collector.emit(new Values(key, count, actualWindowLengthInSeconds));
			//maybe need to do an update to redis here.
			LOG.info("Emitting: " + key + " Value :" + count);
		}
	}
}
