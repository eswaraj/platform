package com.eswaraj.tasks.bolt.counter.starter;

import java.util.Calendar;
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
    public void execute(Tuple inputTuple) {
        logger.info("Received Message " + inputTuple.getMessageId());
        ComplaintCreatedMessage complaintCreatedMessage = (ComplaintCreatedMessage) inputTuple.getValue(0);

        Date creationDate = new Date(complaintCreatedMessage.getComplaintTime());
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);
        List<Long> categories = complaintCreatedMessage.getCategoryIds();

        if (categories == null || categories.isEmpty()) {
            logInfo("No Categories attached, nothing to do");
            return;
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
        acknowledgeTuple(inputTuple);
    }

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }

    @Override
    protected Long getStartOfHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        logInfo("startOfHour = " + calendar.getTimeInMillis() + " , " + calendar.getTime());
        return calendar.getTimeInMillis();
    }

    @Override
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
