package com.eswaraj.tasks.bolt;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.bolt.processors.BoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class SimpleEswarajBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Override
    public Result processTuple(Tuple input) {
        BoltProcessor boltprocessor = getBoltProcessor();
        return boltprocessor.processTuple(input);
    }

}
