package com.eswaraj.core.service.impl;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;

@Component
public class StringTopicManager extends KafkaQueueServiceImpl<String, String> {

	private String locationTopic;
	@Autowired
	public StringTopicManager(@Value("${kafka_brokers}") String kafkaBrokers, @Value("${kafka_topic_location}") String locationTopic){
		super(kafkaBrokers);
		this.locationTopic = locationTopic;
	}
	
	@Override
    @PreDestroy
	public void preDestroy(){
		super.preDestroy();
	}

    public void sendBoundaryfileMessage(String message) throws ApplicationException {
        sendMessage(locationTopic, message);
	}
}
