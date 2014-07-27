package com.eswaraj.tasks.bolt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppService;

/**
 * This bolt will put the Complaint on location rectangle as per lat long It
 * will increase the counter of that rectangle also it will add this complaint
 * id to list of complaints of that rectangle
 * 
 * @author Ravi
 *
 */
@Component
public class ComplaintMapAggregatorBolt implements IRichBolt{

	@Autowired
	private AppService appService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		try{
		}catch(Exception ex){
			collector.fail(input);
		}
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}


}
