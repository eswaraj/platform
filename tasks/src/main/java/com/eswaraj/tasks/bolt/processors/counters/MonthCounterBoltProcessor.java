package com.eswaraj.tasks.bolt.processors.counters;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.eswaraj.messaging.dto.ComplaintMessage;

@Component
public class MonthCounterBoltProcessor extends CounterBoltProcessor {


    @Override
    protected List<String> getMemoryKeysForRead(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return appKeyService.getDayComplaintKeysForTheMonth(prefix, complaintCreationDate);
    }

    @Override
    protected String getMemeoryKeyForWrite(String prefix, ComplaintMessage complaintCreatedMessage) {
        Date complaintCreationDate = new Date(complaintCreatedMessage.getComplaintTime());
        return appKeyService.getMonthComplaintCounterKey(prefix, complaintCreationDate);
    }


}
