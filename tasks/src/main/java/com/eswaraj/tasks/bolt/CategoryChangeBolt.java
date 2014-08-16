package com.eswaraj.tasks.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.impl.AppKeyServiceImpl;
import com.eswaraj.tasks.bolt.processors.BoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.google.gson.Gson;

public class CategoryChangeBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private AppKeyService appKeyService;
    private Gson gson;

    @Override
    public void onPrepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        appKeyService = new AppKeyServiceImpl();
        gson = new Gson();
    }

    @Override
    public Result processTuple(Tuple input) {
        BoltProcessor boltprocessor = getBoltProcessor();
        return boltprocessor.processTuple(input);
    }

}
