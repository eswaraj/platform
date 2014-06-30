package com.eswaraj.cache.impl;

import java.util.ArrayList;
import java.util.List;

import com.eswaraj.base.BaseEswarajMockitoTest;
import com.eswaraj.web.dto.ComplaintDto;

public class BaseCacheTest extends BaseEswarajMockitoTest{

	protected ComplaintDto createComplaintDto() {
		ComplaintDto complaintDto = new ComplaintDto();
		complaintDto.setDescription(randomAlphaString(256));
		complaintDto.setTitle(randomAlphaString(30));
		return complaintDto;
	}
	
	protected List<ComplaintDto> createComplaints(int count){
		List<ComplaintDto> complaints = new ArrayList<>(count);
		for(int i = 0; i < count; i++) {
			complaints.add(createComplaintDto());
		}
		return complaints;
	}
}
