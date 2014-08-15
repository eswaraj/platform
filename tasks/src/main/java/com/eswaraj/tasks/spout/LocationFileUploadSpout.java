package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class LocationFileUploadSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;
    

    @Override
    public void getNextTuple() {
        String message;
        try {
            message = getQueueService().receiveLocationFileUploadMessage();
            if (message != null) {
                logInfo("Mesage Recieved in Spout :  " + message);
                String messageId = writeToStream(new Values(message));
                logInfo("Mesage Emitted from :  " + messageId);
            }
        } catch (ApplicationException e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        } catch (Exception e) {
            logError("Unable to receive Location File message from AWS Quque", e);
        }
    }
    
    @Override
    protected String[] getFields() {
        return new String[] { "LocationSaveMessage" };
    }

}
