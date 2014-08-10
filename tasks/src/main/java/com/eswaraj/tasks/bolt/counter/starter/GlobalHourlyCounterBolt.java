package com.eswaraj.tasks.bolt.counter.starter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class GlobalHourlyCounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;

    CounterKeyService counterKeyService;

    public GlobalHourlyCounterBolt() {
        counterKeyService = new CounterKeyServiceImpl();
    }

    @Override
    public Result processTuple(Tuple inputTuple) {
        logger.info("Received Message " + inputTuple.getMessageId());
        ComplaintCreatedMessage complaintCreatedMessage = (ComplaintCreatedMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);

        String redisKey = counterKeyService.getGlobalHourComplaintCounterKey(creationDate);
        logInfo("redisKey = " + redisKey);
        String cypherQuery = "match n where n.__type__ = 'com.eswaraj.domain.nodes.Complaint' and n.complaintTime >= {startTime} and n.complaintTime<= {endTime} return count(n) as totalComplaint";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startOfHour);
        params.put("endTime", endOfHour);
        logInfo("params=" + params);

        Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

        writeToMemoryStoreValue(redisKey, totalComplaint);

        String keyPrefixForNextBolt = counterKeyService.getGlobalKeyPrefix();
        writeToStream(inputTuple, new Values(keyPrefixForNextBolt, complaintCreatedMessage));
        return Result.Success;
    }

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }

}
