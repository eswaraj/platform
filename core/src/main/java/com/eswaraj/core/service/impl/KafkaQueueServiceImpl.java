package com.eswaraj.core.service.impl;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.ComplaintDto;


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
    public void sendComplaintCreatedMessage(Complaint complaint) throws ApplicationException {
        // TODO Auto-generated method stub

    }

    @Override
    public ComplaintDto receiveComplaintCreatedMessage() throws ApplicationException {
        // TODO Auto-generated method stub
        return null;
    }
    
}
