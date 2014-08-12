package com.eswaraj.tasks.bolt.counter.starter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.LocationKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.core.service.impl.LocationkeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
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

	@Autowired
	private AppService appService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	
    LocationKeyService locationKeyService;
    CounterKeyService counterKeyService;

    public ComplaintHourlyMapAggregatorBolt() {
        locationKeyService = new LocationkeyServiceImpl();
        counterKeyService = new CounterKeyServiceImpl();
    }

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
    public Result processTuple(Tuple inputTuple) {
        try {
            ComplaintCreatedMessage complaintCreatedMessage = (ComplaintCreatedMessage) inputTuple.getValue(0);

            Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
            long startOfHour = getStartOfHour(creationDate);
            long endOfHour = getEndOfHour(creationDate);

            String cypherQuery = "start complaint where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' and complaint.nearByKey={nearByKey} and complaint.complaintTime >= {startTime} and complaint.complaintTime<= {endTime} return count(complaint) as totalComplaint";

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
            logger.error("Unable to process tuple", ex);
        }
        return Result.Failed;
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

}
