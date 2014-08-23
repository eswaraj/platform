package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.core.service.StormCacheAppServices;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.google.gson.JsonObject;

@Component
public class ComplaintRefreshBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private StormCacheAppServices stormCacheAppServices;
    @Autowired
    private AppKeyService appKeyService;
    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);
        logDebug("Got complaintCreatedMessage : {}", complaintCreatedMessage);
        try {
            JsonObject jsonObject = stormCacheAppServices.getCompleteComplaintInfo(complaintCreatedMessage.getId());
            String redisKeyForComplaint = appKeyService.getComplaintObjectKey(complaintCreatedMessage.getId());
            writeToMemoryStoreValue(redisKeyForComplaint, jsonObject.toString());
        } catch (Exception ex) {
            logError("Unable to refresh Complaint : ", ex);
        }
        return Result.Success;
    }

}
