package com.eswaraj.tasks.bolt.processors.counters;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.messaging.dto.ComplaintMessage;

@Component
public class MonthCounterBoltProcessor extends CounterBoltProcessor {

    @Autowired
    private CounterKeyService counterKeyService;

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return counterKeyService.getDayComplaintKeysForTheMonth(prefix, complaintCreationDate);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return counterKeyService.getMonthComplaintCounterKey(prefix, complaintCreationDate);
    }


}
