package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.tasks.spout.mesage.id.MessageId;
import com.eswaraj.tasks.topology.EswarajBaseSpout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LocationFileUploadSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;
    

    @Override
    public void getNextTuple() {
        String message;
        try {
            message = getQueueService().receiveLocationFileUploadMessage();
            if (message != null) {
                logInfo("Mesage Recieved in Spout :  " + message);
                MessageId<String> messageId = new MessageId<>();
                messageId.setData(message);
                writeToStream(new Values(message), messageId);
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

    @Override
    public void onAck(Object msgId) {

    }

    @Override
    public void onFail(Object msgId) {
    }

    private void updateFileStatus(Object msg, String status) {
        if (msg instanceof MessageId) {
            MessageId<String> messageId = (MessageId) msg;
            String jsonData = messageId.getData();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(jsonData);
            Long newLocationBoundaryFileId = jsonObject.get("newLocationBoundaryFileId").getAsLong();
            // Call app Service
        }

    }

}
