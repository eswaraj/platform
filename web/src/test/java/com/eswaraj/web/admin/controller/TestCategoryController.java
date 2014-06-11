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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:eswaraj-servlet.xml", "classpath:eswaraj-web-test.xml" })
public class TestCategoryController extends BaseEswarajMockitoTest {

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private AppService appService;

	private MockMvc mockMvc;

	private final String saveCategoryUrl = "/ajax/categories/save";

	private final String getChildCategoryUrl = "/ajax/categories/getchild/";

	private final String getRootCategoryUrl = "/ajax/categories/getroot";

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void test01_getroot() throws Exception {
		// create Test expectation data
		Long parentLocationId = null;
		boolean isRoot = true;
		int totalRootCategories = randomInteger(20);
		List<CategoryDto> rootCategories = new ArrayList<>(totalRootCategories);
		for (int i = 0; i < totalRootCategories; i++) {
			rootCategories.add(createOneCategory(parentLocationId, isRoot));
		}
		// Set Mock expectation
		when(appService.getAllRootCategories()).thenReturn(rootCategories);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = this.mockMvc.perform(get(getRootCategoryUrl).accept(expectedMediaType));
		result.andExpect(status().isOk());
		// System.out.println("Content = "+content().);
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkCategoryArray(result, rootCategories, parentLocationId);
	}

	@Test
	public void test02_getChildLocations() throws Exception {
		// create Test expectation data
		Long parentCategoryId = randomPositiveLong();
		boolean isRoot = true;
		int totalRootCategories = randomInteger(20);
		List<CategoryDto> childCategories = new ArrayList<>(totalRootCategories);
		for (int i = 0; i < totalRootCategories; i++) {
			childCategories.add(createOneCategory(parentCategoryId, isRoot));
		}
		// Set Mock expectation
		when(appService.getAllChildCategoryOfParentCategory(parentCategoryId)).thenReturn(childCategories);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = this.mockMvc.perform(get(getChildCategoryUrl + parentCategoryId).accept(expectedMediaType));
		result.andExpect(status().isOk());
		System.out.println("Content = "+content());
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkCategoryArray(result, childCategories, parentCategoryId);
	}

	/**
	 * Save Category save it as LocationType is always ignored
	 * 
	 * @throws Exception
	 */
	@Test
	public void test03_saveLocation() throws Exception {

		// create Test expectation data
		Long parentLocationId = randomLong();
		CategoryDto newCategoryToBeSaved = createOneCategory(parentLocationId, true);

		// Set Mock expectation
		when(appService.saveCategory(newCategoryToBeSaved)).thenReturn(newCategoryToBeSaved);

		// Run test
		MediaType expectedMediaType = MediaType.APPLICATION_JSON;
		ResultActions result = mockMvc.perform(post(saveCategoryUrl).accept(expectedMediaType).contentType(APPLICATION_JSON_UTF8)
				.content(convertObjectToJsonBytes(newCategoryToBeSaved)));
		result.andExpect(status().isOk());
		result.andExpect(content().contentType("application/json;charset=UTF-8"));

		checkCategory(result, newCategoryToBeSaved, parentLocationId);
	}

	private void checkCategoryArray(ResultActions result, List<CategoryDto> locations, Long parentCategoryId) throws Exception {
		CategoryDto categoryDto;
		int sizeOfArray = locations.size();
		result.andExpect(jsonPath("$", hasSize(sizeOfArray)));
		for (int i = 0; i < sizeOfArray; i++) {
			categoryDto = locations.get(i);
			result.andExpect(jsonPath("$[" + i + "].name").value(categoryDto.getName()));
			result.andExpect(jsonPath("$[" + i + "].id").value(categoryDto.getId()));
			result.andExpect(jsonPath("$[" + i + "].description").value(categoryDto.getDescription()));
			result.andExpect(jsonPath("$[" + i + "].root").value(categoryDto.isRoot()));
			// For country Parent Id must Not be null and must be equal to
			// parametr we passed to url
			if (parentCategoryId == null) {
				result.andExpect(jsonPath("$[" + i + "].parentCategoryId").doesNotExist());
			} else {
				result.andExpect(jsonPath("$[" + i + "].parentCategoryId").value(parentCategoryId));
				result.andExpect(jsonPath("$[" + i + "].parentCategoryId").value(categoryDto.getParentCategoryId()));
			}
		}
	}

	private void checkCategory(ResultActions result, CategoryDto categoryDto, Long parentLocationId) throws Exception {
		result.andExpect(jsonPath("$.name").value(categoryDto.getName()));
		result.andExpect(jsonPath("$.id").value(categoryDto.getId()));
		result.andExpect(jsonPath("$.description").value(categoryDto.getDescription()));
		// For country Parent Id must Not be null and must be equal to parametr
		// we passed to url
		if (parentLocationId == null) {
			result.andExpect(jsonPath("$.parentCategoryId").doesNotExist());
		} else {
			result.andExpect(jsonPath("$.parentCategoryId").value(parentLocationId));
		}
	}

	private CategoryDto createOneCategory(Long parentCategoryId, boolean isRoot) {
		Long categoryId = uniqueLong("categoryId");
		String categoryName = randomAlphaString(30);
		String description = randomAlphaString(128);

		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setId(categoryId);
		categoryDto.setName(categoryName);
		categoryDto.setDescription(description);
		categoryDto.setParentCategoryId(parentCategoryId);
		return categoryDto;
	}

	public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsBytes(object);
	}

}
