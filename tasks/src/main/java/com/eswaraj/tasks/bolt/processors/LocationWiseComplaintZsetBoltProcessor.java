package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import backtype.storm.tuple.Tuple;

import com.eswaraj.core.service.AppKeyService;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class LocationWiseComplaintZsetBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private AppKeyService appKeyService;
    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);
        logDebug("Got complaintCreatedMessage : {}", complaintCreatedMessage);

        if (CollectionUtils.isEmpty(complaintCreatedMessage.getLocationIds())) {
            logInfo("No location found, nothing to do for complaint message: {}", complaintCreatedMessage);
            return Result.Success;
        }
        String redisKey;
        String locationCategoryKey;
        for (Long locationId : complaintCreatedMessage.getLocationIds()) {
            redisKey = appKeyService.getLocationComplaintsKey(locationId);
            writeToMemoryStoreSortedSet(redisKey, complaintCreatedMessage.getId().toString(), complaintCreatedMessage.getComplaintTime());
            if (!CollectionUtils.isEmpty(complaintCreatedMessage.getCategoryIds())) {
                for (Long oneCategoryId : complaintCreatedMessage.getCategoryIds()) {
                    locationCategoryKey = appKeyService.getLocationCategoryComplaintsKey(locationId, oneCategoryId);
                    writeToMemoryStoreSortedSet(locationCategoryKey, complaintCreatedMessage.getId().toString(), complaintCreatedMessage.getComplaintTime());
                }
            }
        }

        return Result.Success;
    }

}
