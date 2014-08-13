package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class CatageoryChangeSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void nextTuple() {
        String message;
        try {
            message = getQueueService().receiveCategoryUpdateMessage();
            if (message != null) {
                logInfo("Mesage Recieved in Spout :  " + message);
                writeToStream(new Values(message));
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Category Update message from AWS Quque", e);
        }

    }

    @Override
    public void ack(Object msgId) {
        logInfo("****** Mesage ack in Spout :  " + msgId);
    }

    @Override
    public void fail(Object msgId) {
        logInfo("****** Mesage failed in Spout :  " + msgId);
    }

}
