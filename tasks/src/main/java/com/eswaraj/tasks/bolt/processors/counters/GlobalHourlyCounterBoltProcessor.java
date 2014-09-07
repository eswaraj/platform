package com.eswaraj.tasks.bolt.processors.counters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class GlobalHourlyCounterBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private CounterKeyService counterKeyService;

    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);
        logDebug("Got complaintCreatedMessage : {}", complaintCreatedMessage);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);

        String redisKey = counterKeyService.getGlobalComplaintCounterKey();
        String hashKey = counterKeyService.getHourKey(creationDate);
        String cypherQuery = "match n where n.__type__ = 'com.eswaraj.domain.nodes.Complaint' and n.complaintTime >= {startTime} and n.complaintTime<= {endTime} return count(n) as totalComplaint";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startOfHour);
        params.put("endTime", endOfHour);

        Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

        writeToMemoryStoreHash(redisKey, hashKey, totalComplaint);

        writeToStream(inputTuple, new Values(redisKey, "", complaintCreatedMessage));
        return Result.Success;
    }

}
