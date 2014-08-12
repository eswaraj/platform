package com.eswaraj.core.service.impl;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.queue.service.QueueService;


public class KafkaQueueServiceImpl implements QueueService {

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
    
}
