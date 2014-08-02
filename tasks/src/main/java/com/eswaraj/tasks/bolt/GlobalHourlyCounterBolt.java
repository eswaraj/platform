package com.eswaraj.tasks.bolt;

import java.util.Date;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.domain.nodes.Complaint;

public class GlobalHourlyCounterBolt extends CounterBolt {

    private static final long serialVersionUID = 1L;

    private String cipharQuery;


    @Override
    public void execute(Tuple input) {
        Complaint complaint = (Complaint) input.getValue(0);

        // TODO write DB query to get all Complaint count in  hour when complaint was saved)
        // and update it in Redis
        Date creationDate = complaint.getDateCreated();
        String redisKey = buildGlobalHourKey(creationDate);

        String keyPrefixForNextBolt = getKeyPrefix();
        writeToStream(new Values(keyPrefixForNextBolt));
        
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
