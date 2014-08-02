package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class LocationFileUploadSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;
    

    @Override
    public void nextTuple() {
        String message;
        try {
            System.out.println("queueService=" + getQueueService());
            message = getQueueService().receiveLocationFileUploadMessage();
            if (message != null) {
                logInfo("Mesage Recieved in Spout :  " + message);
                writeToStream(new Values(message, 1L));
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        }
    }
    
    @Override
    protected String[] getFields() {
        return new String[] { "LocationSaveMessage" };
    }

}
