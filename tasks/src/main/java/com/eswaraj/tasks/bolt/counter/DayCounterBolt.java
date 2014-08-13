package com.eswaraj.tasks.bolt.counter;

import java.util.Date;
import java.util.List;

import com.eswaraj.messaging.dto.ComplaintMessage;

public class DayCounterBolt extends CounterBolt {

    private static final long serialVersionUID = 1L;

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
