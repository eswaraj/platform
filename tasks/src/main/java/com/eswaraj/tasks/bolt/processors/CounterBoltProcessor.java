package com.eswaraj.tasks.bolt.processors;

import java.util.List;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;

public abstract class CounterBoltProcessor extends AbstractBoltProcessor {

    boolean writeToOutputStream = true;

    public CounterBoltProcessor(boolean writeToOutputStream) {
        this.writeToOutputStream = writeToOutputStream;
    }

    public CounterBoltProcessor() {
        this(true);
    }
    @Override
    public Result processTuple(Tuple inputTuple) {
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
        if (writeToOutputStream) {
            writeToStream(inputTuple, new Values(prefix, complaintCreatedMessage));
        }

        return Result.Success;

    }
    
    protected abstract List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage);

    protected abstract String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage);

}
