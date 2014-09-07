package com.eswaraj.tasks.bolt.processors.counters;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

/**
 * This bolt will put the Complaint on location rectangle as per lat long It
 * will increase the counter of that rectangle also it will add this complaint
 * id to list of complaints of that rectangle
 * 
 * @author Ravi
 *
 */
@Component
public class ComplaintHourlyMapAggregatorBoltProcessor extends AbstractBoltProcessor {
	
	@Override
    public Result processTuple(Tuple inputTuple) {
        try {
            ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);

            Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
            long startOfHour = getStartOfHour(creationDate);
            long endOfHour = getEndOfHour(creationDate);

            String cypherQuery = "match complaint where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.nearByKey={nearByKey} and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

            String dbNearByKey = appKeyService.buildLocationKeyForNearByComplaints(complaintCreatedMessage.getLattitude(), complaintCreatedMessage.getLongitude());
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nearByKey", dbNearByKey);
            params.put("startTime", startOfHour);
            params.put("endTime", endOfHour);

            Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");
            
            String redisKey = appKeyService.getNearByKey(complaintCreatedMessage.getLattitude(), complaintCreatedMessage.getLongitude());
            String hashKey = appKeyService.getHourKey(new Date(complaintCreatedMessage.getComplaintTime()));

            writeToMemoryStoreHash(redisKey, hashKey, totalComplaint);

            writeToStream(inputTuple, new Values(redisKey, "", complaintCreatedMessage));

            return Result.Success;
        } catch (Exception ex) {
            logError("Unable to process tuple", ex);
        }
        return Result.Failed;
	}

}
