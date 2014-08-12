package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class ReProcessAllComplaintsSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void nextTuple() {
        ComplaintMessage complaint;
        try {
            complaint = getQueueService().receiveComplaintCreatedMessage();
            if (complaint != null) {
                logInfo("Mesage Recieved in Spout :  " + complaint);
                String messageId = writeToStream(new Values(complaint));
                logInfo("Mesage Written by Spout :  " + messageId);
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        } catch (Exception e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        }

    }

    @Override
    public void ack(Object msgId) {
        logger.info("********************************");
        logger.info("Message {} has been processed", msgId + " , " + msgId.getClass());
        logger.info("********************************");
    }

    @Override
    public void fail(Object msgId) {
        logger.info("********************************");
        logger.info("Message {} has been Failed", msgId + " , " + msgId.getClass());
        logger.info("********************************");
    }
}
