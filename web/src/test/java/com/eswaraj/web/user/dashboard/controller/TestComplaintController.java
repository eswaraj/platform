package com.eswaraj.web.user.dashboard.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
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
import com.eswaraj.web.admin.controller.BaseControllerTest;
import com.eswaraj.web.dto.ComplaintDto;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:eswaraj-servlet.xml", "classpath:eswaraj-web-test.xml"})
public class TestComplaintController extends BaseControllerTest{

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
	public void shouldGetUserComplaints01() throws Exception {
		
		Long personId = randomPositiveLong();
		int start = 0;
		int end = 10, count = 10;
		List<ComplaintDto> complaints = createComplaints(count);
		when(complaintService.getAllUserComplaints(personId)).thenReturn(complaints);
		
		ResultActions response = this.mockMvc.perform(get(getUserComplaints+"/"+ personId).accept(MediaType.APPLICATION_JSON));
		response.andExpect(status().isOk());
		response.andExpect(content().contentType("application/json;charset=UTF-8"));
		
		
		//checkPoliticalBodyAdmin(result, currentPoliticalBodyAdminDto);
	}
	
	@Test
	public void shouldGetUserComplaints02() throws Exception {
		
		Long personId = randomPositiveLong();
		int start = 0;
		int end = 10, count = 10;
		List<ComplaintDto> complaints = createComplaints(count);
		when(complaintService.getPagedUserComplaints(personId, start, end)).thenReturn(complaints);
		
        ResultActions response = this.mockMvc.perform(get(getUserComplaints + "/" + personId).accept(MediaType.APPLICATION_JSON).param("start", String.valueOf(start))
                .param("count", String.valueOf(end)));
		response.andExpect(status().isOk());
		response.andExpect(content().contentType("application/json;charset=UTF-8"));
		
		
		//checkPoliticalBodyAdmin(result, currentPoliticalBodyAdminDto);
	}
	
}
