package com.eswaraj.tasks.bolt;

import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseBolt;
import com.eswaraj.web.dto.ComplaintDto;

public class ComplaintProcessorBolt extends EswarajBaseBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
    public Result processTuple(Tuple input) {
		try{
            Long complaintId = null;
            Object value = input.getValue(0);
            if (value instanceof ComplaintMessage) {
                ComplaintMessage complaint = (ComplaintMessage) value;
                complaintId = complaint.getId();
            } else if (value instanceof ComplaintDto) {
                ComplaintDto complaint = (ComplaintDto) value;
                complaintId = complaint.getId();
            } else {
                complaintId = (Long) value;
            }
            logInfo("Working on Complaint : " + complaintId);
            ComplaintMessage updatedComplaintMessage = getComplaintService().updateLocationAndAdmins(complaintId);
            writeToStream(input, new Values(updatedComplaintMessage));
            return Result.Success;
		}catch(Exception ex){
            logError("Unable to process", ex);
        }
        return Result.Failed;
	}

}
