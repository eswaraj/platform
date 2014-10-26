package com.eswaraj.tasks.bolt.processors.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.cache.PersonCache;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class RefreshPersonBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private PersonCache personCache;
    @Override
    public Result processTuple(Tuple inputTuple) throws Exception {
        Long personId = (Long) inputTuple.getValue(0);
        logDebug("Got personId : {} to refresh", personId);
        personCache.refreshPerson(personId);
        return Result.Success;
    }

}
