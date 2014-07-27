package com.eswaraj.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Component
public class AwsQueueProducer implements QueueProducer {

	private AmazonSQS sqs;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public AwsQueueProducer(@Value("${aws_region}") String regions, @Value("${aws_access_key}") String accessKey, @Value("${aws_access_secret}") String secretKey) {
        this(Regions.valueOf(regions), accessKey, secretKey);
	}

    private AwsQueueProducer(Regions regions, String accessKey, String secretKey) {
		AWSCredentials awsCredentials;
		awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
		sqs = new AmazonSQSClient(awsCredentials);
		Region usWest2 = Region.getRegion(regions);
		sqs.setRegion(usWest2);

	}

    /* (non-Javadoc)
     * @see com.eswaraj.core.util.QueueProducer#sendMessage(java.lang.String, java.lang.String)
     */
    @Override
    public void sendMessage(String queueName, String messageBody) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueName, messageBody);
        sqs.sendMessage(sendMessageRequest);
    }

}
