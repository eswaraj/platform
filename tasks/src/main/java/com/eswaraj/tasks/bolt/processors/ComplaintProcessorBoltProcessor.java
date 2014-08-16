package com.eswaraj.tasks.bolt.processors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt.Result;
import com.eswaraj.web.dto.ComplaintDto;

@Component
public class ComplaintProcessorBoltProcessor extends AbstractBoltProcessor {

    @Autowired
    private ComplaintService complaintService;

	@Override
    public Result processTuple(Tuple input) {
		try{
            Long complaintId = null;
            Object value = input.getValue(0);
            logDebug("Got Input : {}", value);
            if (value instanceof ComplaintMessage) {
                ComplaintMessage complaint = (ComplaintMessage) value;
                complaintId = complaint.getId();
            } else if (value instanceof ComplaintDto) {
                ComplaintDto complaint = (ComplaintDto) value;
                complaintId = complaint.getId();
            } else {
                complaintId = (Long) value;
            }
            logDebug("Working on Complaint : {}", complaintId);
            ComplaintMessage updatedComplaintMessage = complaintService.updateLocationAndAdmins(complaintId);
            writeToStream(input, new Values(updatedComplaintMessage));
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}

}
