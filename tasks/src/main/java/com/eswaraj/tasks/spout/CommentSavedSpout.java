package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class CommentSavedSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void getNextTuple() {
        CommentSavedMessage commentSavedMessage;
        try {
            commentSavedMessage = getQueueService().receiveCommentSavedMessage();
            if (commentSavedMessage != null) {
                logInfo("Mesage Recieved in Spout :  " + commentSavedMessage);
                writeToStream(new Values(commentSavedMessage));
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
