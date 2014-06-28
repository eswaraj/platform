package com.eswaraj.core.service.impl;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.LocationBoundaryFileDto;

@Component
public class LocationBoundaryTopicManager extends KafkaQueueServiceImpl<String, LocationBoundaryFileDto> {

	private String locationTopic;
	
	@Autowired
	public LocationBoundaryTopicManager(@Value("${kafka_brokers}") String kafkaBrokers, @Value("${kafka_topic_location}") String locationTopic){
		super(kafkaBrokers);
		this.locationTopic = locationTopic;
	}
	
	@PreDestroy
	public void preDestroy(){
		super.preDestroy();
	}
	public void sendBoundaryfileMessage(LocationBoundaryFileDto locationBoundaryFileDto) throws ApplicationException {
		sendMessage(locationTopic, locationBoundaryFileDto);
	}

}
