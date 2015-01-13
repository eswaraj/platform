package com.eswaraj.queue.service.aws.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.util.DataMessageTypes;
import com.eswaraj.messaging.dto.CommentSavedMessage;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.messaging.dto.ComplaintViewedByPoliticalAdminMessage;
import com.eswaraj.queue.service.QueueService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Will create this bean manually in core project's eswaraj-core.xml, so that we can control which Queue Service to use i.e. Aws or Kafka
 * 
 * @author Ravi
 *
 */
public class AwsQueueServiceImpl implements QueueService, Serializable {

    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Gson gson = new Gson();

    @Autowired
    private AwsQueueManager awsQueueManager;

    @Value("${aws_location_file_queue_name}")
    private String awsLocationQueueName;
    
    @Value("${aws_category_queue_name}") 
    private String awsCategoryUpdateQueueName;

    @Value("${aws_complaint_created_queue_name}")
    private String awsComplaintCreatedQueueName;

    @Value("${aws_reprocess_all_complaint_queue_name}")
    private String awsReProcessAllComplaintQueueName;

    @Value("${aws_complaint_viewed_by_political_admin_queue_name}")
    private String awsComplaintViewedByPoliticalAdminQueueName;

    @Value("${aws_comment_saved_queue_name}")
    private String awsCommentSavedQueueName;

    // Normal Constructor to be used by Spring
    public AwsQueueServiceImpl() {

    }

    public AwsQueueServiceImpl(AwsQueueManager awsQueueManager, String awsLocationQueueName, String awsCategoryUpdateQueueName, String awsComplaintCreatedQueueName,
            String awsReProcessAllComplaintQueueName, String awsComplaintViewedByPoliticalAdminQueueName, String awsCommentSavedQueueName) {
        super();
        this.awsQueueManager = awsQueueManager;
        this.awsLocationQueueName = awsLocationQueueName;
        this.awsCategoryUpdateQueueName = awsCategoryUpdateQueueName;
        this.awsComplaintCreatedQueueName = awsComplaintCreatedQueueName;
        this.awsReProcessAllComplaintQueueName = awsReProcessAllComplaintQueueName;
        this.awsComplaintViewedByPoliticalAdminQueueName = awsComplaintViewedByPoliticalAdminQueueName;
        this.awsCommentSavedQueueName = awsCommentSavedQueueName;
    }

    /**
     * Will send a message to AWS Queue whose name is awsLocationQueueName
     */
    @Override
    public void sendLocationFileUploadMessage(Long existingLocationBoundaryFileId, Long newLocationBoundaryFileId, Long locationId) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        if (existingLocationBoundaryFileId != null) {
            jsonObject.addProperty("oldLocationBoundaryFileId", existingLocationBoundaryFileId);
        }
        jsonObject.addProperty("newLocationBoundaryFileId", newLocationBoundaryFileId);
        jsonObject.addProperty("locationId", locationId);

        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);

        awsQueueManager.sendMessage(awsLocationQueueName, jsonObject.toString());
    }

    @Override
    public String receiveLocationFileUploadMessage() throws ApplicationException {
        return awsQueueManager.receiveMessage(awsLocationQueueName);
    }

    @Override
    public void sendCategoryUpdateMessage(Long categoryId) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("categoryId", categoryId);
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.CATEGORY_CHANGE_MESSAGE_TYPE);

        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
    }

    @Override
    public String receiveCategoryUpdateMessage() throws ApplicationException {
        return awsQueueManager.receiveMessage(awsCategoryUpdateQueueName);
    }

    @Override
    public void sendComplaintCreatedMessage(ComplaintMessage complaint) throws ApplicationException {
        Gson gson = new Gson();
        String messageBody = gson.toJson(complaint);
        awsQueueManager.sendMessage(awsComplaintCreatedQueueName, messageBody);

    }

    @Override
    public ComplaintMessage receiveComplaintCreatedMessage() throws ApplicationException {
        String mesage = awsQueueManager.receiveMessage(awsComplaintCreatedQueueName);
        logger.debug("Message Received : {} ", mesage);
        Gson gson = new Gson();
        return gson.fromJson(mesage, ComplaintMessage.class);
    }

    @Override
    public String receiveReprocessAllComplaintMessage() throws ApplicationException {
        logger.debug("Receiving Message From : {} ", awsReProcessAllComplaintQueueName);
        String mesage = awsQueueManager.receiveMessage(awsReProcessAllComplaintQueueName);
        logger.debug("Message Received : {} ", mesage);
        return mesage;
    }

    @Override
    public void sendLocationUpdateMessage(Long locationId) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("locationId", locationId);
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.LOCATION_UPDATE_MESSAGE_TYPE);
        
        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
        
    }

    @Override
    public void sendPoliticalBodyAdminUpdateMessage(Long locationId, Long politicalBodyAdminId) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("locationId", locationId);
        jsonObject.addProperty("politicalBodyAdminId", politicalBodyAdminId);
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.POLITICAL_BODY_ADMIN_UPDATE_MESSAGE_TYPE);

        logger.info("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());

    }

    @Override
    public void sendReprocesAllComplaintOfLocation(Long locationId) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("locationId", locationId);
        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsReProcessAllComplaintQueueName);
        awsQueueManager.sendMessage(awsReProcessAllComplaintQueueName, jsonObject.toString());
    }

    @Override
    public void sendComplaintViewedByPoliticalLeaderMessage(ComplaintViewedByPoliticalAdminMessage complaintViewedByPoliticalAdminMessage) throws ApplicationException {
        String jsonMessage = gson.toJson(complaintViewedByPoliticalAdminMessage);
        sendMessageToQueue(awsComplaintViewedByPoliticalAdminQueueName, jsonMessage);
    }

    @Override
    public ComplaintViewedByPoliticalAdminMessage receiveComplaintViewedByPoliticalLeaderMessage() throws ApplicationException {
        String message = awsQueueManager.receiveMessage(awsComplaintViewedByPoliticalAdminQueueName);
        return gson.fromJson(message, ComplaintViewedByPoliticalAdminMessage.class);

    }
    private void sendMessageToQueue(String queuename, String message){
        logger.debug("Sending message {} to queue {}", message, queuename);
        awsQueueManager.sendMessage(queuename, message);
    }

    @Override
    public void sendCommentSavedMessage(CommentSavedMessage commentSavedMessage) throws ApplicationException {
        String jsonMessage = gson.toJson(commentSavedMessage);
        sendMessageToQueue(awsCommentSavedQueueName, jsonMessage);
    }

    @Override
    public CommentSavedMessage receiveCommentSavedMessage() throws ApplicationException {
        String message = awsQueueManager.receiveMessage(awsCommentSavedQueueName);
        return gson.fromJson(message, CommentSavedMessage.class);
    }

    @Override
    public void sendReprocesAllComplaints() throws ApplicationException {
        logger.debug("Sending message {} to queue {}", "test-Wrog json", awsReProcessAllComplaintQueueName);
        awsQueueManager.sendMessage(awsReProcessAllComplaintQueueName, "Test-Wrong Json Intentionally");
    }

    @Override
    public void sendReprocesAllLocations() throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.REFRESH_ALL_LOCATION_MESSAGE_TYPE);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
    }

    @Override
    public void sendReprocesAllPersons() throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.REFRESH_ALL_PERSON_MESSAGE_TYPE);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
    }

    @Override
    public void sendReprocesAllComments() throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.REFRESH_ALL_COMMENT_MESSAGE_TYPE);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
    }

    @Override
    public void sendRefreshPerson(Long personId, String system) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(DataMessageTypes.MESSAGE_TYPE, DataMessageTypes.REFRESH_ONE_PERSON_MESSAGE_TYPE);
        jsonObject.addProperty("personId", personId);
        jsonObject.addProperty("system", system);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
    }

}
