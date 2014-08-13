package com.eswaraj.tasks.bolt.counter;

import java.util.List;

import com.eswaraj.messaging.dto.ComplaintMessage;

public class TotalCounterBolt extends CounterBolt {

    private static final long serialVersionUID = 1L;

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        return counterKeyService.getYearComplaintKeysForEternitySinceStart(prefix);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        return counterKeyService.getTotalComplaintCounterKey(prefix);
    }

}
