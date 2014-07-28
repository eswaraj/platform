package com.eswaraj.tasks.bolt;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;


/**
 * @author anuj
 * @data Jul 25, 2014
 */

public class RedisSaveBolt extends BaseRichBolt {

	public static final Logger LOG = LoggerFactory.getLogger(LoggerBolt.class);

	public RedisSaveBolt(String host, int port) {
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		
	}

	@Override
	public void execute(Tuple tuple) {
		LOG.info(tuple.toString());
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
}
