package com.eswaraj.tasks.spout;

import backtype.storm.tuple.Values;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.tasks.topology.EswarajBaseSpout;

public class ComplaintViwedByPoliticalAdminSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Override
    public void getNextTuple() {
        ComplaintViewedByPoliticalAdminMessage complaintViewedByPoliticalAdminMessage;
        try {
            complaintViewedByPoliticalAdminMessage = getQueueService().receiveComplaintViewedByPoliticalLeaderMessage();
            if (complaintViewedByPoliticalAdminMessage != null) {
                logInfo("Mesage Recieved in Spout :  " + complaintViewedByPoliticalAdminMessage);
                writeToStream(new Values(complaintViewedByPoliticalAdminMessage));
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
