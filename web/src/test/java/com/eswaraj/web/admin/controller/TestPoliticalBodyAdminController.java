package com.eswaraj.web.admin.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
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

import com.eswaraj.core.service.AppService;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:eswaraj-servlet.xml", "classpath:eswaraj-web-test.xml" })
public class TestPoliticalBodyAdminController extends BaseControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private AppService appService;

	private MockMvc mockMvc;
	
	private final String savePoliticalBodyAdminUrl = "/ajax/pbadmin/save";
	
	private final String getAllPoliticalBodyAdminsOfLocationUrl = "/ajax/pbadmin/get/";
	
	private final String getCurrentPoliticalBodyAdminsOfLocationUrl = "/ajax/pbadmin/getcurrent/";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void test01_getAllPoliticalBodyAdminsOfLocation() throws Exception {
		// create Test expectation data
		long locationId = randomPositiveLong();
		long pbTypeId = randomPositiveLong();
		int totalPoliticalBodyAdmins = randomInteger(100);
		List<PoliticalBodyAdminDto> allPoliticalBodyAdmins = new ArrayList<>(totalPoliticalBodyAdmins);
		for(int i=0;i<totalPoliticalBodyAdmins;i++ ){
			allPoliticalBodyAdmins.add(createOnePoliticalBodyAdminDto());
		}
		// Set Mock expectation
		when(appService.getAllPoliticalBodyAdminByLocationId(locationId, pbTypeId)).thenReturn(allPoliticalBodyAdmins);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = this.mockMvc.perform(get(getAllPoliticalBodyAdminsOfLocationUrl+locationId+"/"+pbTypeId).accept(expectedMediaType));
		result.andExpect(status().isOk());
		// System.out.println("Content = "+content().);
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkPoliticalBodyAdminArray(result, allPoliticalBodyAdmins);
	}

	
	@Test
	public void test02_getAllPoliticalLocationTypes() throws Exception {
		// create Test expectation data
		long locationId = randomPositiveLong();
		long pbTypeId = randomPositiveLong();
		PoliticalBodyAdminDto currentPoliticalBodyAdminDto = createOnePoliticalBodyAdminDto();
		// Set Mock expectation
		when(appService.getCurrentPoliticalBodyAdminByLocationId(locationId, pbTypeId)).thenReturn(currentPoliticalBodyAdminDto);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = this.mockMvc.perform(get(getCurrentPoliticalBodyAdminsOfLocationUrl+locationId+"/"+pbTypeId).accept(expectedMediaType));
		result.andExpect(status().isOk());
		// System.out.println("Content = "+content().);
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkPoliticalBodyAdmin(result, currentPoliticalBodyAdminDto);
	}

	/**
	 * Save State when Client passes LocationType as State in json It should
	 * save it as LocationType is always ignored
	 * 
	 * @throws Exception
	 */
	@Test
	public void test03_savePoliticalBodyAdmin() throws Exception {


		// create Test expectation data
		PoliticalBodyAdminDto politicalBodyTypeDto = createOnePoliticalBodyAdminDto();
		// Set Mock expectation
		when(appService.savePoliticalBodyAdmin(politicalBodyTypeDto)).thenReturn(politicalBodyTypeDto);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = mockMvc.perform(post(savePoliticalBodyAdminUrl).accept(expectedMediaType).contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(politicalBodyTypeDto)));
		result.andExpect(status().isOk());
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkPoliticalBodyAdmin(result, politicalBodyTypeDto);
	}
	
	

	private void checkPoliticalBodyAdminArray(ResultActions result, List<PoliticalBodyAdminDto> locations) throws Exception {
		PoliticalBodyAdminDto politicalBodyAdminDto;
		int sizeOfArray = locations.size();
		result.andExpect(jsonPath("$", hasSize(sizeOfArray)));
		for (int i = 0; i < sizeOfArray; i++) {
			politicalBodyAdminDto = locations.get(i);
			result.andExpect(jsonPath("$[" + i + "].email").value(politicalBodyAdminDto.getEmail()));
			result.andExpect(jsonPath("$[" + i + "].id").value(politicalBodyAdminDto.getId()));
			result.andExpect(jsonPath("$[" + i + "].endDate").value(politicalBodyAdminDto.getEndDate().getTime()));
			result.andExpect(jsonPath("$[" + i + "].landLine1").value(politicalBodyAdminDto.getLandLine1()));
			result.andExpect(jsonPath("$[" + i + "].landLine2").value(politicalBodyAdminDto.getLandLine2()));
			result.andExpect(jsonPath("$[" + i + "].locationId").value(politicalBodyAdminDto.getLocationId()));
			result.andExpect(jsonPath("$[" + i + "].mobile1").value(politicalBodyAdminDto.getMobile1()));
			result.andExpect(jsonPath("$[" + i + "].mobile2").value(politicalBodyAdminDto.getMobile2()));
			result.andExpect(jsonPath("$[" + i + "].partyId").value(politicalBodyAdminDto.getPartyId()));
			result.andExpect(jsonPath("$[" + i + "].personId").value(politicalBodyAdminDto.getPersonId()));
			result.andExpect(jsonPath("$[" + i + "].politicalBodyTypeId").value(politicalBodyAdminDto.getPoliticalBodyTypeId()));
			//result.andExpect(jsonPath("$[" + i + "].startDate", new IsEqual<>(equalArg)).value(new IsEqual<Long>(politicalBodyAdminDto.getStartDate().getTime())));
			result.andExpect(jsonPath("$[" + i + "].startDate", new IsEqual<>(politicalBodyAdminDto.getStartDate().getTime())));
			
		}
	}

	private void checkPoliticalBodyAdmin(ResultActions result, PoliticalBodyAdminDto politicalBodyAdminDto) throws Exception {
		result.andExpect(jsonPath("$.email").value(politicalBodyAdminDto.getEmail()));
		result.andExpect(jsonPath("$.id").value(politicalBodyAdminDto.getId()));
		result.andExpect(jsonPath("$.endDate").value(politicalBodyAdminDto.getEndDate().getTime()));
		result.andExpect(jsonPath("$.landLine1").value(politicalBodyAdminDto.getLandLine1()));
		result.andExpect(jsonPath("$.landLine2").value(politicalBodyAdminDto.getLandLine2()));
		result.andExpect(jsonPath("$.locationId").value(politicalBodyAdminDto.getLocationId()));
		result.andExpect(jsonPath("$.mobile1").value(politicalBodyAdminDto.getMobile1()));
		result.andExpect(jsonPath("$.mobile2").value(politicalBodyAdminDto.getMobile2()));
		result.andExpect(jsonPath("$.partyId").value(politicalBodyAdminDto.getPartyId()));
		result.andExpect(jsonPath("$.personId").value(politicalBodyAdminDto.getPersonId()));
		result.andExpect(jsonPath("$.politicalBodyTypeId").value(politicalBodyAdminDto.getPoliticalBodyTypeId()));
		result.andExpect(jsonPath("$.startDate").value(politicalBodyAdminDto.getStartDate().getTime()));
		
	}
}
