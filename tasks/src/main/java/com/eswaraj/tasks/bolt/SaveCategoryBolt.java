package com.eswaraj.tasks.bolt;

import java.util.Map;

import org.springframework.stereotype.Component;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppService;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

@Component
public class SaveCategoryBolt extends EswarajBaseBolt {

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
        logInfo("Message Received " + input);
		try{
			//List<CategoryWithChildCategoryDto> categories = appService.getAllCategories();
			//TODO write these categories to Redis
            collector.ack(input);
		}catch(Exception ex){
			collector.fail(input);
		}

	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}


}
