package com.eswaraj.queue.service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;

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

    void sendReprocesAllComplaints() throws ApplicationException;

    void sendReprocesAllLocations() throws ApplicationException;

    void sendReprocesAllPersons() throws ApplicationException;

    void sendRefreshPerson(Long personId, String system) throws ApplicationException;

    void sendReprocesAllComments() throws ApplicationException;

    void sendComplaintViewedByPoliticalLeaderMessage(ComplaintViewedByPoliticalAdminMessage complaintViewedByPoliticalAdminMessage) throws ApplicationException;

    ComplaintViewedByPoliticalAdminMessage receiveComplaintViewedByPoliticalLeaderMessage() throws ApplicationException;

    void sendCommentSavedMessage(CommentSavedMessage commentSavedMessage) throws ApplicationException;

    CommentSavedMessage receiveCommentSavedMessage() throws ApplicationException;

}
