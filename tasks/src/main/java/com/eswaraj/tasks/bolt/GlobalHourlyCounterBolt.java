package com.eswaraj.tasks.bolt;

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


    @Override
    public void execute(Tuple input) {
        Complaint complaint = (Complaint) input.getValue(0);

        // TODO write DB query to get all Complaint count in  hour when complaint was saved)
        // and update it in Redis
        Date creationDate = complaint.getDateCreated();
        String redisKey = buildGlobalHourKey(creationDate);

        String cypherQuery = "START n=node(*) WHERE n.__type__ = 'com.eswaraj.domain.nodes.Complaint' and n.complaintTime >= {0} and n.complaintTime <= {1} RETURN count(n)";
        Map<String, Object> params = new HashMap<String, Object>();
        Result<Object> result = getNeo4jTemplate().queryEngineFor(QueryType.Cypher).query(cypherQuery, params);
        Long totalComplaint = (Long) result.single();

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
