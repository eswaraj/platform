package com.eswaraj.tasks.bolt.counter.starter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class CategoryHourlyCounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;

    CounterKeyService counterKeyService;

    public CategoryHourlyCounterBolt() {
        counterKeyService = new CounterKeyServiceImpl();
    }

    @Override
    public Result processTuple(Tuple inputTuple) {
        logger.info("Received Message " + inputTuple.getMessageId());
        ComplaintCreatedMessage complaintCreatedMessage = (ComplaintCreatedMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> categories = complaintCreatedMessage.getCategoryIds();

        if (categories == null || categories.isEmpty()) {
            logInfo("No Categories attached, nothing to do");
            return Result.Success;
        }
        for (Long oneCategory : categories) {
            String cypherQuery = "start category=node({categoryId}) match (category)<-[:BELONGS_TO]-(complaint) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("categoryId", oneCategory);
            params.put("startTime", startOfHour);
            params.put("endTime", endOfHour);
            logInfo("params=" + params);

            Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

            String redisKey = counterKeyService.getCategoryHourComplaintCounterKey(creationDate, oneCategory);
            logInfo("redisKey = " + redisKey);

            writeToMemoryStoreValue(redisKey, totalComplaint);

            String keyPrefixForNextBolt = counterKeyService.getCategoryKeyPrefix(oneCategory);
            writeToStream(inputTuple, new Values(keyPrefixForNextBolt, complaintCreatedMessage));
        }
        return Result.Success;
    }

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }

}
