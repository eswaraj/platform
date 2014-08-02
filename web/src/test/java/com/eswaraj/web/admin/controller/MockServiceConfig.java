package com.eswaraj.web.admin.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.queue.service.aws.impl.AwsQueueManager;

@Configuration
public class MockServiceConfig {

	@Bean
	public LocationService locationService() {
		return Mockito.mock(LocationService.class);
	}
	
	@Bean
	public FileService fileService() {
		return Mockito.mock(FileService.class);
	}
	
	@Bean
	public AppService appService() {
		return Mockito.mock(AppService.class);
	}
	
	@Bean
	public PersonService personService() {
		return Mockito.mock(PersonService.class);
	}
	
	@Bean
	public ComplaintService complaintService(){
		return Mockito.mock(ComplaintService.class);
	}

    @Bean
    public AwsQueueManager awsQueueProducer() {
        return Mockito.mock(AwsQueueManager.class);
    }
}
