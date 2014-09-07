package com.eswaraj.queue.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintMessage;

public interface QueueService {

    void sendLocationFileUploadMessage(Long existingLocationBoundaryFileId, Long newLocationBoundaryFileId, Long locationId) throws ApplicationException;

    String receiveLocationFileUploadMessage() throws ApplicationException;

    void sendCategoryUpdateMessage(Long categoryId) throws ApplicationException;

    String receiveCategoryUpdateMessage() throws ApplicationException;

    void sendComplaintCreatedMessage(ComplaintMessage complaint) throws ApplicationException;

    ComplaintMessage receiveComplaintCreatedMessage() throws ApplicationException;

    String receiveReprocessAllComplaintMessage() throws ApplicationException;

    void sendLocationUpdateMessage(Long locationId) throws ApplicationException;

    void sendPoliticalBodyAdminUpdateMessage(Long locationId, Long politicalBodyAdminId) throws ApplicationException;

    void sendReprocesAllComplaintOfLocation(Long locationId) throws ApplicationException;
}
