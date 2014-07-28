package com.eswaraj.tasks.topology;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public abstract class EswarajAwsSqsBaseSpout extends EswarajBaseSpout {

    private static final long serialVersionUID = 1L;

    @Value("${aws_access_key}")
    private String accessKey;
    @Value("${aws_access_secret}")
    private String secretKey;
    @Value("${aws_region}")
    private String region;
    private String awsQueueName;

    private boolean deleteMessageOnFail = true;
    private int defaultWaitTime = 20;
    private int maxNumberOfMessages = 1;
    private AmazonSQSClient sqs;
    ReceiveMessageRequest receiveMessageRequest;

    public EswarajAwsSqsBaseSpout(String awsQueueName) {
        this.awsQueueName = awsQueueName;
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        super.open(conf, context, collector);
        AWSCredentials awsCredentials;
        awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        sqs = new AmazonSQSClient(awsCredentials);
        Region usWest2 = Region.getRegion(Regions.valueOf(region));
        sqs.setRegion(usWest2);

        receiveMessageRequest = new ReceiveMessageRequest(awsQueueName);
        receiveMessageRequest.setWaitTimeSeconds(defaultWaitTime);
        // Read only one message ata time
        receiveMessageRequest.setMaxNumberOfMessages(maxNumberOfMessages);

    }

    protected String getMessage() {
        String messageFromSqs = null;
        try {
            logger.info("Reading Message from Queue " + awsQueueName);
            ReceiveMessageResult rmResult = sqs.receiveMessage(receiveMessageRequest);
            // System.out.println("Got Result "+ rmResult);

            if (rmResult.getMessages().size() > 0) {
                // A message has been received
                for (Message message : rmResult.getMessages()) {
                    try {
                        messageFromSqs = message.getBody();
                        deleteMessage(message);
                    } catch (Exception ex) {
                        if (deleteMessageOnFail) {
                            deleteMessage(message);
                        }
                    }
                }
            } else {
                logger.info("No messages available, attempt ");
            }

        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return messageFromSqs;
    }

    private void deleteMessage(Message message) {
        // Doesnt matter what happened, remove it
        // from AWS, its App's responsbility to make
        // sure that persist this message
        // in case of failure/exception
        try {
            sqs.deleteMessage(new DeleteMessageRequest(awsQueueName, message.getReceiptHandle()));
        } catch (Exception ex) {
            // In case some error occurs
        }
    }

    public String getAwsQueueName() {
        return awsQueueName;
    }

    public void setAwsQueueName(String awsQueueName) {
        this.awsQueueName = awsQueueName;
    }

    public boolean isDeleteMessageOnFail() {
        return deleteMessageOnFail;
    }

    public void setDeleteMessageOnFail(boolean deleteMessageOnFail) {
        this.deleteMessageOnFail = deleteMessageOnFail;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getDefaultWaitTime() {
        return defaultWaitTime;
    }

    public void setDefaultWaitTime(int defaultWaitTime) {
        this.defaultWaitTime = defaultWaitTime;
    }

    public int getMaxNumberOfMessages() {
        return maxNumberOfMessages;
    }

    public void setMaxNumberOfMessages(int maxNumberOfMessages) {
        this.maxNumberOfMessages = maxNumberOfMessages;
    }
}
