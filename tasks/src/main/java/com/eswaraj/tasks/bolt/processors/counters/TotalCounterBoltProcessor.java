package com.eswaraj.tasks.bolt.processors.counters;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.messaging.dto.ComplaintMessage;

@Component
public class TotalCounterBoltProcessor extends CounterBoltProcessor {

    @Autowired
    private CounterKeyService counterKeyService;

    public TotalCounterBoltProcessor() {
        super(false);
    }

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        return counterKeyService.getYearComplaintKeysForEternitySinceStart(prefix);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        return counterKeyService.getTotalComplaintCounterKey(prefix);
    }

}
