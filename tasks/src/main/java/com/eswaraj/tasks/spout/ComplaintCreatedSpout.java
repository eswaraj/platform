package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class ComplaintCreatedSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void nextTuple() {
        ComplaintCreatedMessage complaint;
        try {
            logInfo("Geting Message from Complaint Queue");
            complaint = getQueueService().receiveComplaintCreatedMessage();
            if (complaint != null) {
                logInfo("Mesage Recieved in Spout :  " + complaint);
                writeToStream(new Values(complaint));
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        } catch (Exception e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        }

    }

    @Override
    public void ack(Object msgId) {
        logger.info("Message {} has been processed", msgId);
    }

    @Override
    public void fail(Object msgId) {
    }
}
