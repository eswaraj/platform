package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.cache.ComplaintCache;
import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class ComplaintRefreshBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private ComplaintCache complaintCache;
    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Override
    public Result processTuple(Tuple inputTuple) throws Exception {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);
        logDebug("Got complaintCreatedMessage : {}", complaintCreatedMessage);
        complaintCache.refreshComplaintInfo(complaintCreatedMessage.getId());
        return Result.Success;
    }

}
