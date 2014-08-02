package com.eswaraj.queue.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;

public interface QueueService {

    void sendLocationFileUploadMessage(Long existingLocationBoundaryFileId, Long newLocationBoundaryFileId, Long locationId) throws ApplicationException;

    String receiveLocationFileUploadMessage() throws ApplicationException;

    void sendCategoryUpdateMessage(Long categoryId) throws ApplicationException;

    String receiveCategoryUpdateMessage() throws ApplicationException;

    void sendComplaintCreatedMessage(Complaint complaint) throws ApplicationException;

    Complaint receiveComplaintCreatedMessage() throws ApplicationException;
}
