package com.eswaraj.queue.service.aws.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;

@Component
public class AwsTopicManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private AmazonSNS sns;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public AwsTopicManager(@Value("${aws_region}") String regions, @Value("${aws_access_key}") String accessKey, @Value("${aws_access_secret}") String secretKey) {
        this(Regions.valueOf(regions), accessKey, secretKey);
	}

    private AwsTopicManager(Regions regions, String accessKey, String secretKey) {
		AWSCredentials awsCredentials;
		awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        sns = new AmazonSNSClient(awsCredentials);
		Region usWest2 = Region.getRegion(regions);
        sns.setRegion(usWest2);

	}

    public void publishMessage(String topicArn, String messageBody) {
        PublishRequest publishRequest = new PublishRequest(topicArn, messageBody);
        sns.publish(publishRequest);
    }

}
