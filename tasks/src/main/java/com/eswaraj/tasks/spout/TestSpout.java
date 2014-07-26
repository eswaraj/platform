package com.eswaraj.tasks.spout;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class TestSpout extends EswarajBaseSpout {

	private static final long serialVersionUID = 1L;
	private SpoutOutputCollector collector;
	
	public TestSpout() {
	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

    int count = 0;
	@Override
	public void nextTuple() {
		Utils.sleep(1000); 
		final String[] words = new String[] {"nathan", "mike", "jackson", "golda", "bertels"}; 
		final Random rand = new Random(); 
        final String word = words[rand.nextInt(words.length)] + count;
        collector.emit("TestStream", new Values(word));
		logInfo("Emitting "+ word);
        count++;

	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
