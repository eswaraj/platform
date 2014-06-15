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
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.PartyDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:eswaraj-servlet.xml", "classpath:eswaraj-web-test.xml" })
public class TestPartyController extends BaseEswarajMockitoTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private AppService appService;

	private MockMvc mockMvc;

	private final String savePartyUrl = "/ajax/party/save";

	private final String getAllPartyUrl = "/ajax/party/getall";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void test01_getAllParty() throws Exception {
		// create Test expectation data
		int totalParties = randomInteger(20);
		List<PartyDto> allParties = new ArrayList<>(totalParties);
		for (int i = 0; i < totalParties; i++) {
			allParties.add(createOneParty());
		}
		// Set Mock expectation
		when(appService.getAllPoliticalParties()).thenReturn(allParties);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = this.mockMvc.perform(get(getAllPartyUrl).accept(expectedMediaType));
		result.andExpect(status().isOk());
		System.out.println("Content = "+content());
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkPartyArray(result, allParties);
	}

	/**
	 * Save Party with not null name
	 * 
	 * @throws Exception
	 */
	@Test
	public void test02_saveParty() throws Exception {

		// create Test expectation data
		PartyDto newPartyToBeSaved = createOneParty();

		// Set Mock expectation
		when(appService.saveParty(newPartyToBeSaved)).thenReturn(newPartyToBeSaved);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = mockMvc.perform(post(savePartyUrl).accept(expectedMediaType).contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(newPartyToBeSaved)));
		result.andExpect(status().isOk());
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkCategory(result, newPartyToBeSaved);
	}

	private void checkPartyArray(ResultActions result, List<PartyDto> parties) throws Exception {
		PartyDto partyDto;
		int sizeOfArray = parties.size();
		result.andExpect(jsonPath("$", hasSize(sizeOfArray)));
		for (int i = 0; i < sizeOfArray; i++) {
			partyDto = parties.get(i);
			result.andExpect(jsonPath("$[" + i + "].name").value(partyDto.getName()));
			result.andExpect(jsonPath("$[" + i + "].id").value(partyDto.getId()));
		}
	}

	private void checkCategory(ResultActions result, PartyDto partyDto) throws Exception {
		result.andExpect(jsonPath("$.name").value(partyDto.getName()));
		result.andExpect(jsonPath("$.id").value(partyDto.getId()));
	}

	private PartyDto createOneParty() {
		PartyDto partyDto = new PartyDto();
		partyDto.setName(randomAlphaString(30));
		partyDto.setId(uniqueLong("PartyID"));
		return partyDto;
	}


	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
