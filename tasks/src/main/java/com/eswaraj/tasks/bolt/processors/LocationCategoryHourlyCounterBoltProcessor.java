package com.eswaraj.tasks.bolt.processors;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

@Component
public class LocationCategoryHourlyCounterBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private CounterKeyService counterKeyService;

    @Override
    public Result processTuple(Tuple inputTuple) {
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> locations = complaintCreatedMessage.getLocationIds();
        List<Long> categories = complaintCreatedMessage.getCategoryIds();

        if (locations == null || locations.isEmpty()) {
            logInfo("No Locations attached, nothing to do");
            return Result.Success;
        }
        if (categories == null || categories.isEmpty()) {
            logInfo("No Categories attached, nothing to do");
            return Result.Success;
        }
        for (Long oneLocation : locations) {
            for (Long oneCategory : categories) {
                String cypherQuery = "start location=node({locationId}), category=node({categoryId}) match (location)<-[:AT]-(complaint)-[:BELONGS_TO]->(category) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("categoryId", oneCategory);
                params.put("locationId", oneLocation);
                params.put("startTime", startOfHour);
                params.put("endTime", endOfHour);

                Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");

                String redisKey = counterKeyService.getLocationCategoryHourComplaintCounterKey(creationDate, oneLocation, oneCategory);


                writeToMemoryStoreValue(redisKey, totalComplaint);

                String keyPrefixForNextBolt = counterKeyService.getLocationCategoryKeyPrefix(oneLocation, oneCategory);
                writeToStream(inputTuple, new Values(keyPrefixForNextBolt, complaintCreatedMessage));
            }
        }
        return Result.Success;
        
    }

}
