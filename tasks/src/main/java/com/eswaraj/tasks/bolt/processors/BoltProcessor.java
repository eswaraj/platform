package com.eswaraj.tasks.bolt.processors;

import backtype.storm.tuple.Tuple;

import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

public interface BoltProcessor {

    public Result processTuple(Tuple input) throws Exception;
    
    public void initBoltProcessorForTuple(ThreadLocal<Tuple> tuple, EswarajBaseBolt eswarajBaseBolt);
}
