package com.eswaraj.tasks.bolt.processors.counters;

import java.util.List;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.bolt.processors.AbstractBoltProcessor;
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
        logDebug("inputTuple = {}", inputTuple);
        String redisKey = (String) inputTuple.getValue(0);
        String prefix = (String) inputTuple.getValue(1);
        logDebug("redisKey = {}, hashKeyprefix = {}", redisKey, prefix);
        ComplaintMessage complaintCreatedMessage = (ComplaintMessage) inputTuple.getValue(2);
        logDebug("complaintCreatedMessage = " + complaintCreatedMessage);
        List<String> allKeys = getMemoryKeysForRead(prefix, complaintCreatedMessage);

        List<Object> counterValues = readMultiKeyFromStringMemoryHashStore(redisKey, allKeys);

        Long totalComplaints = 0L;
        for (Object oneCounterValue : counterValues) {
            if (oneCounterValue != null) {
                totalComplaints = totalComplaints + Long.parseLong(oneCounterValue.toString());
            }
        }

        String writeHashKey = getMemeoryKeyForWrite(prefix, complaintCreatedMessage);
        logDebug("prefix  redisKey= " + redisKey + ", " + totalComplaints);

        writeToMemoryStoreHash(redisKey, writeHashKey, totalComplaints);
        // writeToMemoryStoreValue(redisKey, totalComplaints);
        if (writeToOutputStream) {
            writeToStream(inputTuple, new Values(redisKey, prefix, complaintCreatedMessage));
        }

        return Result.Success;

    }
    
    protected abstract List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage);

    protected abstract String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage);

}
