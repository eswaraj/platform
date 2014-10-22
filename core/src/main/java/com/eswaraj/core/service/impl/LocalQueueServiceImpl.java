package com.eswaraj.core.service.impl;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.queue.service.QueueService;

public class LocalQueueServiceImpl implements QueueService {

    @Override
    public void sendLocationFileUploadMessage(Long existingLocationBoundaryFileId, Long newLocationBoundaryFileId, Long locationId) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public String receiveLocationFileUploadMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendCategoryUpdateMessage(Long categoryId) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public String receiveCategoryUpdateMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendComplaintCreatedMessage(ComplaintMessage complaint) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public ComplaintMessage receiveComplaintCreatedMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String receiveReprocessAllComplaintMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendLocationUpdateMessage(Long locationId) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendPoliticalBodyAdminUpdateMessage(Long locationId, Long politicalBodyAdminId) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendReprocesAllComplaintOfLocation(Long locationId) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public void sendComplaintViewedByPoliticalLeaderMessage(ComplaintViewedByPoliticalAdminMessage complaintViewedByPoliticalAdminMessage) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public ComplaintViewedByPoliticalAdminMessage receiveComplaintViewedByPoliticalLeaderMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void sendCommentSavedMessage(CommentSavedMessage commentSavedMessage) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public CommentSavedMessage receiveCommentSavedMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }

}
