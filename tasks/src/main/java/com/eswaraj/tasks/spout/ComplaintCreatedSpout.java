package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.tasks.topology.EswarajBaseSpout;
import com.eswaraj.web.dto.ComplaintDto;

public class ComplaintCreatedSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void nextTuple() {
        ComplaintDto complaint;
        try {
            logInfo("Geting Message from Complaint Queue");
            complaint = getQueueService().receiveComplaintCreatedMessage();
            if (complaint != null) {
                logInfo("Mesage Recieved in Spout :  " + complaint);
                writeToStream(new Values(complaint));
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        }

    }
}
