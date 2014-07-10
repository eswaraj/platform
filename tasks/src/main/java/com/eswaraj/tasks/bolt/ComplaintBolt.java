package com.eswaraj.tasks.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

/**
 * @author anuj
 * @data Jul 7, 2014
 */

public class ComplaintBolt extends BaseBasicBolt {

    public ComplaintBolt() {
    }


    public void prepare(java.util.Map stormConf, TopologyContext context) {
    	
    }
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector collector) {
    	//do something with the saved complaint
    }
}
