package com.eswaraj.tasks.bolt.processors;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

public class CategoryHourlyCounterBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private CounterKeyService counterKeyService;

    public CategoryHourlyCounterBoltProcessor() {
        counterKeyService = new CounterKeyServiceImpl();
    }

    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> categories = complaintCreatedMessage.getCategoryIds();

        if (categories == null || categories.isEmpty()) {
            logDebug("No Categories attached, nothing to do");
            return Result.Success;
        }
        for (Long oneCategory : categories) {
            String cypherQuery = "start category=node({categoryId}) match (category)<-[:BELONGS_TO]-(complaint) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("categoryId", oneCategory);
            params.put("startTime", startOfHour);
            params.put("endTime", endOfHour);

            Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

            String redisKey = counterKeyService.getCategoryHourComplaintCounterKey(creationDate, oneCategory);
            logDebug("redisKey = {}, totalComplaint={}", redisKey, totalComplaint);

            writeToMemoryStoreValue(redisKey, totalComplaint);

            String keyPrefixForNextBolt = counterKeyService.getCategoryKeyPrefix(oneCategory);
            writeToStream(inputTuple, new Values(keyPrefixForNextBolt, complaintCreatedMessage));
        }
        return Result.Success;
    }

}
