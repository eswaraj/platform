package com.eswaraj.queue.service.aws.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.messaging.dto.ComplaintCreatedMessage;
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

    // Normal Constructor to be used by Spring
    public AwsQueueServiceImpl() {

    }

    public AwsQueueServiceImpl(AwsQueueManager awsQueueManager, String awsLocationQueueName, String awsCategoryUpdateQueueName, String awsComplaintCreatedQueueName) {
        super();
        this.awsQueueManager = awsQueueManager;
        this.awsLocationQueueName = awsLocationQueueName;
        this.awsCategoryUpdateQueueName = awsCategoryUpdateQueueName;
        this.awsComplaintCreatedQueueName = awsComplaintCreatedQueueName;
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

        logger.debug("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);

        awsQueueManager.sendMessage(awsCategoryUpdateQueueName, jsonObject.toString());
    }

    @Override
    public String receiveCategoryUpdateMessage() throws ApplicationException {
        return awsQueueManager.receiveMessage(awsCategoryUpdateQueueName);
    }

    @Override
    public void sendComplaintCreatedMessage(ComplaintCreatedMessage complaint) throws ApplicationException {
        Gson gson = new Gson();
        String messageBody = gson.toJson(complaint);
        awsQueueManager.sendMessage(awsComplaintCreatedQueueName, messageBody);

    }

    @Override
    public ComplaintCreatedMessage receiveComplaintCreatedMessage() throws ApplicationException {
        String mesage = awsQueueManager.receiveMessage(awsComplaintCreatedQueueName);
        Gson gson = new Gson();
        return gson.fromJson(mesage, ComplaintCreatedMessage.class);
    }

}
