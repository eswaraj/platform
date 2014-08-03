package com.eswaraj.tasks.bolt.counter.starter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public class LocationCategoryHourlyCounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;

    CounterKeyService counterKeyService;

    public LocationCategoryHourlyCounterBolt() {
        counterKeyService = new CounterKeyServiceImpl();
    }

    @Override
    public void execute(Tuple input) {
        ComplaintCreatedMessage complaintCreatedMessage = (ComplaintCreatedMessage) input.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> locations = complaintCreatedMessage.getLocationIds();
        List<Long> categories = complaintCreatedMessage.getCategoryIds();

        if (locations == null && locations.isEmpty()) {
            logInfo("No Locations attached, nothing to do");
            return;
        }
        for (Long oneLocation : locations) {
            for (Long oneCategory : categories) {
                String cypherQuery = "start location=node({locationId}), category=node({categoryId}) match (location)<-[:AT]-(complaint)-[:BELONGS_TO]->(category) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

                Map<String, Object> params = new HashMap<String, Object>();
                params.put("categoryId", oneCategory);
                params.put("locationId", oneLocation);
                params.put("startTime", startOfHour);
                params.put("endTime", endOfHour);
                logInfo("params=" + params);

                Result<Object> result = getNeo4jTemplate().queryEngineFor(QueryType.Cypher).query(cypherQuery, params);
                logInfo("Result.single() = " + result.single());
                Long totalComplaint = ((Integer) ((Map) result.single()).get("totalComplaint")).longValue();

                String redisKey = counterKeyService.getLocationCategoryHourComplaintCounterKey(creationDate, oneLocation, oneCategory);
                logInfo("redisKey = " + redisKey);

                writeToMemoryStoreValue(redisKey, totalComplaint);

                String keyPrefixForNextBolt = counterKeyService.getLocationCategoryKeyPrefix(oneLocation, oneCategory);
                writeToStream(new Values(keyPrefixForNextBolt, complaintCreatedMessage));
            }
        }
        
    }

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }

    protected Long getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        logInfo("startOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }

    protected Long getEndOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        logInfo("endOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }

}
