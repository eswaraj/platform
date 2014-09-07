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
public class LocationHourlyCounterBoltProcessor extends AbstractBoltProcessor {

    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> locations = complaintCreatedMessage.getLocationIds();

        if (locations == null || locations.isEmpty()) {
            logInfo("No Locations attached, nothing to do");
            return Result.Success;
        }
        for (Long oneLocation : locations) {
            String cypherQuery = "start location=node({locationId}) match (location)<-[:AT]-(complaint) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("locationId", oneLocation);
            params.put("startTime", startOfHour);
            params.put("endTime", endOfHour);

            Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

            String redisKey = appKeyService.getLocationKey(oneLocation);
            String hashKey = appKeyService.getHourKey(creationDate);

            writeToMemoryStoreHash(redisKey, hashKey, totalComplaint);

            writeToStream(inputTuple, new Values(redisKey, "", complaintCreatedMessage));
        }
        return Result.Success;
        
    }

}
