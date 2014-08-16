package com.eswaraj.tasks.bolt.processors;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

public interface BoltProcessor {

    public Result processTuple(Tuple input);
    
    public void initBoltProcessorForTuple(Tuple tuple);
}
