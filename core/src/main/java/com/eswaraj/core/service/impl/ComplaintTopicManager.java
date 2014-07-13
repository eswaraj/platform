package com.eswaraj.core.service.impl;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.ComplaintDto;
import com.eswaraj.web.dto.LocationBoundaryFileDto;

/**
 * 
 * @author anuj
 * @data Jul 7, 2014
 */

@Component
public class ComplaintTopicManager extends KafkaQueueServiceImpl<String, ComplaintDto> {

	private String complaintTopic;
	
	@Autowired
	public ComplaintTopicManager(@Value("${kafka_brokers}") String kafkaBrokers, @Value("${kafka_topic_complaint}") String complaintTopic){
		super(kafkaBrokers);
		this.complaintTopic = complaintTopic;
	}
	
	@PreDestroy
	public void preDestroy(){
		super.preDestroy();
	}
	
	public void sendComplaintMessage(ComplaintDto complaint) throws ApplicationException {
		sendMessage(complaintTopic, complaint);
	}
}
