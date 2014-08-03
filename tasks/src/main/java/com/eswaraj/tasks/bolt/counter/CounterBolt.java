package com.eswaraj.tasks.bolt.counter;

import java.util.List;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class CounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    protected CounterKeyService counterKeyService;

    public CounterBolt() {
        counterKeyService = new CounterKeyServiceImpl();
    }

    @Override
    public void execute(Tuple input) {
        // Read Input
        String prefix = (String) input.getValue(0);
        ComplaintCreatedMessage complaintCreatedMessage = (ComplaintCreatedMessage) input.getValue(1);

        List<String> allKeys = getMemoryKeysForRead(prefix, complaintCreatedMessage);

        List<Long> counterValues = readMultiKeyFromMemoryStore(allKeys, Long.class);

        Long totalComplaints = 0L;
        for (Long oneCounterValue : counterValues) {
            totalComplaints = totalComplaints + oneCounterValue;
        }

        String redisKey = getMemeoryKeyForWrite(prefix, complaintCreatedMessage);

        writeToMemoryStoreValue(redisKey, totalComplaints);
        if (getOutputStream() != null) {
            //Some Counter Bolt may be last in the hierarchy so Stream may not be defined
            writeToStream(new Values(prefix, complaintCreatedMessage));
        }

    }
    
    protected abstract List<String> getMemoryKeysForRead(String prefix, ComplaintCreatedMessage complaintCreatedMessage);

    protected abstract String getMemeoryKeyForWrite(String prefix, ComplaintCreatedMessage complaintCreatedMessage);

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }


}
