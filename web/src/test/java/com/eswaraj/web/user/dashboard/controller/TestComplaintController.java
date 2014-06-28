package com.eswaraj.web.user.dashboard.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.eswaraj.core.service.ComplaintService;
import com.eswaraj.web.dto.ComplaintDto;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:eswaraj-servlet.xml", "classpath:eswaraj-web-test.xml"})
@Ignore
public class TestComplaintController {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private ComplaintService complaintService;

	private MockMvc mockMvc;

	private final String getUserComplaints = "/user/complaints";

	private final String getUserComplaint = "/user/complaint/";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void shouldGetUserComplaints() throws Exception {
		
		String personId = RandomStringUtils.randomAlphanumeric(16);
		int start = 0;
		int end = 10, count = 10;
		ComplaintDto complaintDto = createComplaintDto();
		List<ComplaintDto> complaints = createComplaints(count);
		when(complaintService.getPagedUserComplaints(personId, start, end)).thenReturn(complaints);
		
		ResultActions response = this.mockMvc.perform(get(getUserComplaints+"/"+ personId).accept(MediaType.APPLICATION_JSON));
		response.andExpect(status().isOk());
		response.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		
		//checkPoliticalBodyAdmin(result, currentPoliticalBodyAdminDto);
	}
	
	private ComplaintDto createComplaintDto() {
		ComplaintDto complaintDto = new ComplaintDto();
		complaintDto.setDescription(RandomStringUtils.randomAlphabetic(30));
		complaintDto.setTitle(RandomStringUtils.randomAlphabetic(10));
		return complaintDto;
	}
	
	private List<ComplaintDto> createComplaints(int count){
		List<ComplaintDto> complaints = new ArrayList<>(count);
		for(int i = 0; i < count; i++) {
			complaints.add(createComplaintDto());
		}
		return complaints;
	}
}
