package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class ReProcessAllComplaintsSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void getNextTuple() {
        String message;
        try {
            message = getQueueService().receiveReprocessAllComplaintMessage();
            if (message != null) {
                logInfo("Mesage Recieved in Spout :  " + message);
                writeToStream(new Values(message));
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        } catch (Exception e) {
            logError("Unable to receive Complaint Created message from AWS Quque", e);
        }

    }

    @Override
    public void onAck(Object msgId) {
    }

    @Override
    public void onFail(Object msgId) {
    }

}
