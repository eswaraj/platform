package com.eswaraj.core.service.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.DepartmentDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl_Department extends BaseNeo4jEswarajTest{

	@Autowired private AppService appService;
	@Autowired private LocationService locationService;
	@Autowired private PersonService personService;

	/**
	 * Create a Department and then get it by getDepartmentById Service
	 * @throws ApplicationException
	 */
	@Test
	public void test01_saveDepartment() throws ApplicationException{
		final String name = randomAlphaString(16);
		final String description = randomAlphaString(256);
		CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		DepartmentDto departmentDto = createDepartment(name, description, categoryDto.getId());
		DepartmentDto savedDepartmentDto = appService.saveDepartment(departmentDto);
		assertEqualDepartment(departmentDto, savedDepartmentDto, false);
		
		DepartmentDto dbDepartmentDto = appService.getDepartmentById(savedDepartmentDto.getId());
		assertEqualDepartment(departmentDto, dbDepartmentDto, false);
		assertEqualDepartment(savedDepartmentDto, dbDepartmentDto, true);
		
	}
	
	
	/**
	 * Create a Department with category passed as null
	 * It must throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test02_saveDepartment() throws ApplicationException{
		final String name = randomAlphaString(16);
		final String description = randomAlphaString(256);
		final Long categoryId = null;
		
		DepartmentDto departmentDto = createDepartment(name, description, categoryId);
		appService.saveDepartment(departmentDto);//This call should throw validation exception
		
	}
	/**
	 * Create a Department with an unknown Category, i.e. Category Do not exists in database
	 * It should throw ApplicationException
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test03_saveDepartment() throws ApplicationException{
		final String name = randomAlphaString(16);
		final String description = randomAlphaString(256);
		final CategoryDto categoryDto = createRandomCateory(true, null);
		//Above department hasn't been saved in database and will just assign any unknown Id to it
		categoryDto.setId(randomPositiveLong());
		
		DepartmentDto departmentDto = createDepartment(name, description, categoryDto.getId());
		appService.saveDepartment(departmentDto);//This should throw ApplicationException
		
		
	}
	
	/**
	 * Create a Non ROOT Department with name as null
	 * It should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test04_saveDepartment() throws ApplicationException{
		final String name = null;
		final String description = randomAlphaString(256);
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		DepartmentDto departmentDto = createDepartment(name, description, categoryDto);
		appService.saveDepartment(departmentDto);//This should throw ValidationException
	}
	
	/**
	 * Create Multiple Department under a category and get them back by  getAllDepartmentsOfCategory
	 * @throws ApplicationException
	 */
	@Test
	public void test05_saveDepartment() throws ApplicationException{
		int totalCategoty = randomInteger(5);
		Map<CategoryDto, List<DepartmentDto>> categoryAndDepartmentMap = new LinkedHashMap<>();
		for(int i=0;i<totalCategoty;i++){
			CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
			int totalDepartmentUnderCategory = randomInteger(15);
			categoryAndDepartmentMap.put(categoryDto, new ArrayList<DepartmentDto>(totalDepartmentUnderCategory));
			for(int j=0;j<totalDepartmentUnderCategory;j++){
				DepartmentDto department = createRandomDepartment(categoryDto.getId());
				department = appService.saveDepartment(department);
				categoryAndDepartmentMap.get(categoryDto).add(department);
			}
		}
		
		List<DepartmentDto> departmetOfCategoryFromDb;
		//check the data
		for(Entry<CategoryDto, List<DepartmentDto>> oneEntry : categoryAndDepartmentMap.entrySet()){
			departmetOfCategoryFromDb = appService.getAllDepartmentsOfCategory(oneEntry.getKey().getId());
			assertEquals(oneEntry.getValue().size(), departmetOfCategoryFromDb.size());
			assertEquals(oneEntry.getValue(), departmetOfCategoryFromDb);
		}
		
	}
	
	
}
