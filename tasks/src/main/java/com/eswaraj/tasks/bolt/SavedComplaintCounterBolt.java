package com.eswaraj.tasks.bolt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.eswaraj.web.dto.ComplaintDto;

/**
 * 
 * This is WIP counter bold, need to be connected to a datastore (like redis)
 * Currently only storing it in a Map.
 * 
 * @author anuj
 * @data Jul 7, 2014
 */

public class SavedComplaintCounterBolt implements IRichBolt {

	private static final long serialVersionUID = 1L;
	
	Map<String, Integer> counters;
	private OutputCollector collector;
	private ObjectMapper mapper;
	private final String key = "savedComplaintCount";
	
	public static final Logger LOG = LoggerFactory.getLogger(SavedComplaintCounterBolt.class);
	

    public void prepare(java.util.Map stormConf, TopologyContext context, OutputCollector collector) {
    	this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		mapper = new ObjectMapper();
    }
    
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    	
    }

	@Override
	public void execute(Tuple tuple) {
		ComplaintDto comp = null;
		try {
			comp = mapper.readValue(tuple.getString(0), ComplaintDto.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(comp != null) {
			if(!counters.containsKey(key)){
				counters.put(key, 1);
			}else{
				Integer c = counters.get(key) +1;
				counters.put(key, c);
			}
		}
		LOG.info("Complaints Saved: " + counters.get(key));
		collector.ack(tuple);		
	}

	@Override
	public void cleanup() {
		for(Map.Entry<String, Integer> entry:counters.entrySet()){
			System.out.println(entry.getKey()+" : " + entry.getValue());
		}		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}
