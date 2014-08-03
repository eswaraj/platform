package com.eswaraj.tasks.bolt;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.neo4j.annotation.QueryType;
import org.springframework.data.neo4j.conversion.Result;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.domain.nodes.Complaint;

public class GlobalHourlyCounterBolt extends CounterBolt {

    private static final long serialVersionUID = 1L;

    private String cipharQuery;

    public static void main(String[] args) {
        Calendar now = Calendar.getInstance();
        printStartAndEnd(now.getTime());
        now.add(Calendar.HOUR, 1);
        printStartAndEnd(now.getTime());
        now.add(Calendar.HOUR, 1);
        printStartAndEnd(now.getTime());
        now.add(Calendar.HOUR, 1);
        printStartAndEnd(now.getTime());
        now.add(Calendar.HOUR, 1);
        printStartAndEnd(now.getTime());

    }

    private static void printStartAndEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 1);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        long startOfHour = calendar.getTimeInMillis();
        System.out.println("startOfHour = " + startOfHour + " , " + calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MILLISECOND, 0);
        long endOfHour = calendar.getTimeInMillis();
        System.out.println("endOfHour = " + endOfHour + " , " + calendar.getTime());

    }

    @Override
    public void execute(Tuple input) {
        Complaint complaint = (Complaint) input.getValue(0);

        Date creationDate = complaint.getDateCreated();
        long startOfHour = getStartOfHour(creationDate);
        long endOfHour = getEndOfHour(creationDate);

        String redisKey = buildGlobalHourKey(creationDate);
        logInfo("redisKey = " + redisKey);
        String cypherQuery = "match n where n.__type__ = 'com.eswaraj.domain.nodes.Complaint' and n.complaintTime >= %startTime and n.complaintTime<=%endTime return count(n)";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startTime", startOfHour);
        params.put("endTime", endOfHour);
        logInfo("params=" + params);

        Result<Object> result = getNeo4jTemplate().queryEngineFor(QueryType.Cypher).query(cypherQuery, params);
        logInfo("Result = " + result);
        Long totalComplaint = (Long) result.single();

        writeToMemoryStoreValue(redisKey, totalComplaint);

        String keyPrefixForNextBolt = getKeyPrefix();
        writeToStream(new Values(keyPrefixForNextBolt));
        
    }

    protected String buildGlobalHourKey(Date date) {
        return buildGlobalKey(hourFormat, date);
    }

    @Override
    protected String[] getFields() {
        return new String[] { getKeyPrefixFieldName() };
    }

    public String getCipharQuery() {
        return cipharQuery;
    }

    public void setCipharQuery(String cipharQuery) {
        this.cipharQuery = cipharQuery;
    }

}
