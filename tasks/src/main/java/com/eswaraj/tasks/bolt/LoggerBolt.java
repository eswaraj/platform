package com.eswaraj.tasks.bolt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * Use this bolt to log tuples within the topology
 * 
 * @author anuj
 * @data Jul 7, 2014
 */

public class LoggerBolt extends BaseBasicBolt {

	public static final Logger LOG = LoggerFactory.getLogger(LoggerBolt.class);
	
    public LoggerBolt() {}


    public void prepare(java.util.Map stormConf, TopologyContext context) {}
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) {}

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
    	LOG.info(tuple.toString());
    }
}
