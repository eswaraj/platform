package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.util.DataMessageTypes;
import com.eswaraj.tasks.topology.EswarajBaseSpout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataChangeSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void getNextTuple() {
        String message = null;
        try {
            message = getQueueService().receiveCategoryUpdateMessage();
            if (message == null) {
                return;
            }
            logInfo("Mesage Recieved in Spout :  " + message);
            JsonObject jsonObject = (JsonObject) new JsonParser().parse(message);
            int messageType = jsonObject.get(DataMessageTypes.MESSAGE_TYPE).getAsInt();

            switch (messageType) {
            case DataMessageTypes.CATEGORY_CHANGE_MESSAGE_TYPE:
                writeToParticularStream(new Values(message), "CategoryUpdatedStream");
                break;
            case DataMessageTypes.LOCATION_UPDATE_MESSAGE_TYPE:
                writeToParticularStream(new Values(message), "LocationUpdatedStream");
                break;
            case DataMessageTypes.REFRESH_ALL_LOCATION_MESSAGE_TYPE:
                writeToParticularStream(new Values(message), "RefreshAllLocationStream");
                break;
            case DataMessageTypes.POLITICAL_BODY_ADMIN_UPDATE_MESSAGE_TYPE:
                writeToParticularStream(new Values(message), "PoliticalBodyAdminUpdatedStream");
                break;
            }
        } catch (Exception e) {
            logError("Unable to Process Data message from AWS Quque " + message, e);
        }

    }

    @Override
    public void onAck(Object msgId) {
    }

    @Override
    public void onFail(Object msgId) {
    }

}
