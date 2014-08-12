package com.eswaraj.tasks.bolt.counter;

import java.util.List;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.core.service.impl.CounterKeyServiceImpl;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;

public abstract class CounterBolt extends EswarajBaseBolt {

    private static final long serialVersionUID = 1L;
    protected CounterKeyService counterKeyService;

    public CounterBolt() {
        counterKeyService = new CounterKeyServiceImpl();
    }

    @Override
    public Result processTuple(Tuple inputTuple) {
        logDebug("Received Message {}", inputTuple.getMessageId());
        // Read Input
        String prefix = (String) inputTuple.getValue(0);
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(1);
        logDebug("prefix = " + prefix);
        List<String> allKeys = getMemoryKeysForRead(prefix, complaintCreatedMessage);

        List<Long> counterValues = readMultiKeyFromMemoryStore(allKeys, Long.class);

        Long totalComplaints = 0L;
        for (Long oneCounterValue : counterValues) {
            if (oneCounterValue != null) {
                totalComplaints = totalComplaints + oneCounterValue;
            }
        }

        String redisKey = getMemeoryKeyForWrite(prefix, complaintCreatedMessage);
        logDebug("prefix  redisKey= " + redisKey + ", " + totalComplaints);

        writeToMemoryStoreValue(redisKey, totalComplaints);
        if (getOutputStream() != null) {
            //Some Counter Bolt may be last in the hierarchy so Stream may not be defined
            writeToStream(inputTuple, new Values(prefix, complaintCreatedMessage));
        }
        return Result.Success;

    }
    
    protected abstract List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage);

    protected abstract String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage);

    @Override
    protected String[] getFields() {
        return new String[] { "KeyPrefix", "Complaint" };
    }


}
