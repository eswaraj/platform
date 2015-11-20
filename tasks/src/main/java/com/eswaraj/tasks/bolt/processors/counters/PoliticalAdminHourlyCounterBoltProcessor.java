package com.eswaraj.tasks.bolt.processors.counters;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class PoliticalAdminHourlyCounterBoltProcessor extends AbstractBoltProcessor {

    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> politicalAdminIds = complaintCreatedMessage.getPoliticalAdminIds();

        if (politicalAdminIds == null || politicalAdminIds.isEmpty()) {
            logDebug("No Political Admins attached, nothing to do");
            return Result.Success;
        }
        for (Long onePoliticalAdmin : politicalAdminIds) {
            String cypherQuery = "start politicalAdmin=node({politicalAdmin}) match (politicalAdmin)<-[:SERVED_BY]-(complaint) where complaint.__type__ = 'Complaint' and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("politicalAdmin", onePoliticalAdmin);
            params.put("startTime", startOfHour);
            params.put("endTime", endOfHour);

            Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

            String redisKey = appKeyService.getPoliticalAdminCounterKey(onePoliticalAdmin);
            String hashKey = appKeyService.getHourKey(creationDate);
            writeToMemoryStoreHash(redisKey, hashKey, totalComplaint);

            //String keyPrefixForNextBolt = counterKeyService.getPoliticalAdminKey(onePoliticalAdmin);
            writeToStream(inputTuple, new Values(redisKey, "", complaintCreatedMessage));
        }
        return Result.Success;
    }

}
