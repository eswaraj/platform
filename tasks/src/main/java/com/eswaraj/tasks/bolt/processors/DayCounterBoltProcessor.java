package com.eswaraj.tasks.bolt.processors;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eswaraj.core.service.CounterKeyService;
import com.eswaraj.messaging.dto.ComplaintMessage;

@Component
public class DayCounterBoltProcessor extends CounterBoltProcessor {

    @Autowired
    private CounterKeyService counterKeyService;

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return counterKeyService.getHourComplaintKeysForTheDay(prefix, complaintCreationDate);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return counterKeyService.getDayComplaintCounterKey(prefix, complaintCreationDate);
    }


}
