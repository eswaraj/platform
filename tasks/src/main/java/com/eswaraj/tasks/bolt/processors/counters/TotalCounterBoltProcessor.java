package com.eswaraj.tasks.bolt.processors.counters;

import java.util.List;

import org.springframework.stereotype.Component;

import com.eswaraj.messaging.dto.ComplaintMessage;

@Component
public class TotalCounterBoltProcessor extends CounterBoltProcessor {

    public TotalCounterBoltProcessor() {
        super(false);
    }

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        return appKeyService.getYearComplaintKeysForEternitySinceStart(prefix);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        return appKeyService.getTotalComplaintCounterKey(prefix);
    }

}
