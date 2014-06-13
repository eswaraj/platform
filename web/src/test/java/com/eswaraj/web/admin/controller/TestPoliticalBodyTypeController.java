package com.eswaraj.web.admin.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
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

import com.eswaraj.base.BaseEswarajMockitoTest;
import com.eswaraj.core.service.AppService;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:eswaraj-servlet.xml", "classpath:eswaraj-web-test.xml" })
public class TestPoliticalBodyTypeController extends BaseEswarajMockitoTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private AppService appService;

	private MockMvc mockMvc;
	
	private final String savePoliticalBodyTypeUrl = "/ajax/pbtype/save";
	
	private final String getPoliticalBodyTypeUrl = "/ajax/pbtype/get";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void test01_getAllPoliticalLocationTypes() throws Exception {
		// create Test expectation data
		int totalPoliticalBodyTypes = randomInteger(100);
		List<PoliticalBodyTypeDto> allPoliticalBodyTypes = new ArrayList<>(totalPoliticalBodyTypes);
		for(int i=0;i<totalPoliticalBodyTypes;i++ ){
			allPoliticalBodyTypes.add(createOnePoliticalBodyType());
		}
		// Set Mock expectation
		when(appService.getAllPoliticalBodyTypes()).thenReturn(allPoliticalBodyTypes);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = this.mockMvc.perform(get(getPoliticalBodyTypeUrl).accept(expectedMediaType));
		result.andExpect(status().isOk());
		// System.out.println("Content = "+content().);
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkPoliticalBodyTypeArray(result, allPoliticalBodyTypes);
	}

	/**
	 * Save State when Client passes LocationType as State in json It should
	 * save it as LocationType is always ignored
	 * 
	 * @throws Exception
	 */
	@Test
	public void test02_savePoliticalBodyType() throws Exception {


		// create Test expectation data
		PoliticalBodyTypeDto politicalBodyTypeDto = createOnePoliticalBodyType();
		// Set Mock expectation
		when(appService.savePoliticalBodyType(politicalBodyTypeDto)).thenReturn(politicalBodyTypeDto);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = mockMvc.perform(post(savePoliticalBodyTypeUrl).accept(expectedMediaType).contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(politicalBodyTypeDto)));
		result.andExpect(status().isOk());
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkPoliticalBodyType(result, politicalBodyTypeDto);
	}
	
	

	private void checkPoliticalBodyTypeArray(ResultActions result, List<PoliticalBodyTypeDto> locations) throws Exception {
		PoliticalBodyTypeDto politicalBodyTypeDto;
		int sizeOfArray = locations.size();
		result.andExpect(jsonPath("$", hasSize(sizeOfArray)));
		for (int i = 0; i < sizeOfArray; i++) {
			politicalBodyTypeDto = locations.get(i);
			result.andExpect(jsonPath("$[" + i + "].name").value(politicalBodyTypeDto.getName()));
			result.andExpect(jsonPath("$[" + i + "].id").value(politicalBodyTypeDto.getId()));
			result.andExpect(jsonPath("$[" + i + "].shortName").value(politicalBodyTypeDto.getShortName()));
			result.andExpect(jsonPath("$[" + i + "].description").value(politicalBodyTypeDto.getDescription()));
			result.andExpect(jsonPath("$[" + i + "].locationTypeId").value(politicalBodyTypeDto.getLocationTypeId()));
		}
	}

	private void checkPoliticalBodyType(ResultActions result, PoliticalBodyTypeDto politicalBodyType) throws Exception {
		result.andExpect(jsonPath("$.name").value(politicalBodyType.getName()));
		result.andExpect(jsonPath("$.id").value(politicalBodyType.getId()));
		result.andExpect(jsonPath("$.shortName").value(politicalBodyType.getShortName()));
		result.andExpect(jsonPath("$.description").value(politicalBodyType.getDescription()));
		result.andExpect(jsonPath("$.locationTypeId").value(politicalBodyType.getLocationTypeId()));
	}

	
	
	private PoliticalBodyTypeDto createOnePoliticalBodyType() {

		PoliticalBodyTypeDto locationTypeDto = new PoliticalBodyTypeDto();
		locationTypeDto.setId(randomPositiveLong());
		locationTypeDto.setName(randomAlphaString(30));
		locationTypeDto.setShortName(randomAlphaString(4));
		locationTypeDto.setDescription(randomAlphaString(128));
		locationTypeDto.setLocationTypeId(randomPositiveLong());
		return locationTypeDto;
	}
	
	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
