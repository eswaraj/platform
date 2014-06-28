package com.eswaraj.core.service.impl;

import java.util.Date;

import org.junit.Test;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.web.dto.LocationBoundaryFileDto;

public class TestLocationBoundaryTopicManager {

	@Test
	public void TestLocationBoundaryTopicManagerAsMain() throws ApplicationException {
		LocationBoundaryTopicManager locationBoundaryTopicManager = new LocationBoundaryTopicManager("localhost:9092", "test");
		LocationBoundaryFileDto locationBoundaryFileDto = new LocationBoundaryFileDto();
		locationBoundaryFileDto.setFileNameAndPath("Test Path");
		locationBoundaryFileDto.setId(10L);
		locationBoundaryFileDto.setLocationId(102L);
		locationBoundaryFileDto.setStatus("Pending");
		locationBoundaryFileDto.setUploadDate(new Date());
		locationBoundaryTopicManager.sendBoundaryfileMessage(locationBoundaryFileDto);
	}

}
