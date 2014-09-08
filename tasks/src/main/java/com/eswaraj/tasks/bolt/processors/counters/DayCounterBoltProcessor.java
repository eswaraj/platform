package com.eswaraj.tasks.bolt.processors.counters;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eswaraj.messaging.dto.ComplaintMessage;

@Component
public class DayCounterBoltProcessor extends CounterBoltProcessor {

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return appKeyService.getHourComplaintKeysForTheDay(prefix, complaintCreationDate);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return appKeyService.getDayComplaintCounterKey(prefix, complaintCreationDate);
    }


}
