package com.eswaraj.tasks.bolt.counter.starter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.core.service.impl.LocationkeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

/**
 * This bolt will put the Complaint on location rectangle as per lat long It
 * will increase the counter of that rectangle also it will add this complaint
 * id to list of complaints of that rectangle
 * 
 * @author Ravi
 *
 */
public class ComplaintHourlyMapAggregatorBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    LocationKeyService locationKeyService;
    CounterKeyService counterKeyService;

    public ComplaintHourlyMapAggregatorBolt() {
        locationKeyService = new LocationkeyServiceImpl();
        counterKeyService = new CounterKeyServiceImpl();
    }

	@Override
    public Result processTuple(Tuple inputTuple) {
        try {
            ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(0);

            Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
            long startOfHour = getStartOfHour(creationDate);
            long endOfHour = getEndOfHour(creationDate);

            String cypherQuery = "match complaint where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.nearByKey={nearByKey} and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

            String dbNearByKey = locationKeyService.buildLocationKeyForNearByComplaints(complaintCreatedMessage.getLattitude(), complaintCreatedMessage.getLongitude());
            
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nearByKey", dbNearByKey);
            params.put("startTime", startOfHour);
            params.put("endTime", endOfHour);
            logInfo("params=" + params);

            Long totalComplaint = executeCountQueryAndReturnLong(cypherQuery, params, "totalComplaint");
            
            String redisKey = locationKeyService.getNearByHourComplaintCounterKey(new Date(complaintCreatedMessage.getComplaintTime()), complaintCreatedMessage.getLattitude(),
                    complaintCreatedMessage.getLongitude());
            logInfo("redisKey = " + redisKey);

            writeToMemoryStoreValue(dbNearByKey, totalComplaint);

            String keyPrefixForNextBolt = locationKeyService.getNearByKeyPrefix(complaintCreatedMessage.getLattitude(), complaintCreatedMessage.getLongitude());
            writeToStream(inputTuple, new Values(keyPrefixForNextBolt, complaintCreatedMessage));

            return Result.Success;
        } catch (Exception ex) {
            logError("Unable to process tuple", ex);
        }
        return Result.Failed;
	}

	@Override
	public void cleanup() {
        super.cleanup();
	}

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }

}
