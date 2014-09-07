package com.eswaraj.queue.service.aws.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.util.DataMessageTypes;
import com.eswaraj.messaging.dto.ComplaintMessage;
import com.eswaraj.queue.service.QueueService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Will create this bean manually in core project's eswaraj-core.xml, so that we
 * can control which Quueue Service to use i.e. Aws or Kafka
 * 
 * @author Ravi
 *
 */
public class AwsQueueServiceImpl implements QueueService, Serializable {

    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    // Normal Constructor to be used by Spring
    public AwsQueueServiceImpl() {

    }

    public AwsQueueServiceImpl(AwsQueueManager awsQueueManager, String awsLocationQueueName, String awsCategoryUpdateQueueName, String awsComplaintCreatedQueueName,
            String awsReProcessAllComplaintQueueName) {
        super();
        this.awsQueueManager = awsQueueManager;
        this.awsLocationQueueName = awsLocationQueueName;
        this.awsCategoryUpdateQueueName = awsCategoryUpdateQueueName;
        this.awsComplaintCreatedQueueName = awsComplaintCreatedQueueName;
        this.awsReProcessAllComplaintQueueName = awsReProcessAllComplaintQueueName;
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

        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);
        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());

    }

    @Override
    public void sendReprocesAllComplaintOfLocation(Long locationId) throws ApplicationException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("locationId", locationId);
        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsReProcessAllComplaintQueueName);
        awsQueueManager.sendMessage(awsReProcessAllComplaintQueueName, jsonObject.toString());
    }

}
