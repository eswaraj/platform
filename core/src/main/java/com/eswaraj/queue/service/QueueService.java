package com.eswaraj.queue.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;

public interface QueueService {

    void sendLocationFileUploadMessage(Long existingLocationBoundaryFileId, Long newLocationBoundaryFileId, Long locationId) throws ApplicationException;

    String receiveLocationFileUploadMessage() throws ApplicationException;

    void sendCategoryUpdateMessage(Long categoryId) throws ApplicationException;

    String receiveCategoryUpdateMessage() throws ApplicationException;

    void sendComplaintCreatedMessage(ComplaintCreatedMessage complaint) throws ApplicationException;

    ComplaintCreatedMessage receiveComplaintCreatedMessage() throws ApplicationException;
}
