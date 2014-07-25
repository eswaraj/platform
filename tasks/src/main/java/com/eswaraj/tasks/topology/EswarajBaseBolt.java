package com.eswaraj.tasks.topology;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;

/**
 * All bolts should extend this class
 * 
 * @author Ravi Sharma
 * @data Jul 25, 2014
 */

public abstract class EswarajBaseBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1L;
	public static final Logger LOG = LoggerFactory.getLogger(EswarajBaseBolt.class);
	
    public EswarajBaseBolt() {}


    public void prepare(Map stormConf, TopologyContext context) {}
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) {}

}
