package com.eswaraj.queue.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.web.dto.ComplaintDto;

public interface QueueService {

    void sendLocationFileUploadMessage(Long existingLocationBoundaryFileId, Long newLocationBoundaryFileId, Long locationId) throws ApplicationException;

    String receiveLocationFileUploadMessage() throws ApplicationException;

    void sendCategoryUpdateMessage(Long categoryId) throws ApplicationException;

    String receiveCategoryUpdateMessage() throws ApplicationException;

    void sendComplaintCreatedMessage(Complaint complaint) throws ApplicationException;

    ComplaintDto receiveComplaintCreatedMessage() throws ApplicationException;
}
