package com.eswaraj.tasks.bolt.counter;

import java.util.Date;
import java.util.List;

import com.eswaraj.messaging.dto.ComplaintCreatedMessage;

public class Lat24HourCounterBolt extends CounterBolt {

    private static final long serialVersionUID = 1L;

    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintCreatedMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return counterKeyService.getHourComplaintKeysForLast24Hours(prefix, complaintCreationDate);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintCreatedMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return counterKeyService.getLast24HourComplaintCounterKey(prefix, complaintCreationDate);
    }


}
