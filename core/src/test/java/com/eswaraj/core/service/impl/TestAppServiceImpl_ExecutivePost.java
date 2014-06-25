package com.eswaraj.core.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.DepartmentDto;
import com.eswaraj.web.dto.ExecutivePostDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl_ExecutivePost extends BaseNeo4jEswarajTest{

	@Autowired private AppService appService;

	/**
	 * Create a ExecutivePost and then get it by getExecutivePostById Service
	 * @throws ApplicationException
	 */
	@Test
	public void test01_saveExecutivePost() throws ApplicationException{
		final CategoryDto category = createAndSaveRandomCateory(appService, true, null);
		final DepartmentDto department = createAndSaveRandomDepartment(appService, category.getId());
		final String shortTitle = randomAlphaString(3);
		final String title = randomAlphaString(15);
		final String description = randomAlphaString(15);
		
		ExecutivePostDto executivePostDto = createExecutivePost(department, shortTitle, title, description);
		ExecutivePostDto savedExecutivePostDto = appService.saveExecutivePost(executivePostDto);
		assertEqualExecutivePost(executivePostDto, savedExecutivePostDto, false);
		
		ExecutivePostDto dbExecutivePostDto = appService.getExecutivePostById(savedExecutivePostDto.getId());
		assertEqualExecutivePost(executivePostDto, dbExecutivePostDto, false);
		assertEqualExecutivePost(savedExecutivePostDto, dbExecutivePostDto, true);
		
	}
	
	/**
	 * Create a ExecutivePost with title as null, it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test02_saveExecutivePost() throws ApplicationException{
		final CategoryDto category = createAndSaveRandomCateory(appService, true, null);
		final DepartmentDto department = createRandomDepartment(category.getId());
		final String shortTitle = randomAlphaString(3);
		final String title = null;
		final String description = randomAlphaString(15);
		
		ExecutivePostDto executivePostDto = createExecutivePost(department, shortTitle, title, description);
		appService.saveExecutivePost(executivePostDto);// Should throw exception
		
	}
	/**
	 * Create a ExecutivePost with title as empty "", it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test03_saveExecutivePost() throws ApplicationException{
		final CategoryDto category = createAndSaveRandomCateory(appService, true, null);
		final DepartmentDto department = createRandomDepartment(category.getId());
		final String shortTitle = randomAlphaString(3);
		final String title = "";
		final String description = randomAlphaString(15);
		
		ExecutivePostDto executivePostDto = createExecutivePost(department, shortTitle, title, description);
		appService.saveExecutivePost(executivePostDto);// Should throw exception
		
	}
	
	/**
	 * Create a ExecutivePost with Executive Body as null, it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test04_saveExecutivePost() throws ApplicationException{
		final DepartmentDto department = null;
		final String shortTitle = randomAlphaString(3);
		final String title = randomAlphaString(16);
		final String description = randomAlphaString(15);
		
		ExecutivePostDto executivePostDto = createExecutivePost(department, shortTitle, title, description);
		appService.saveExecutivePost(executivePostDto);// Should throw exception
		
	}
	
	/**
	 * Create a ExecutivePost with an ExecutiveBodyId which do not exists in database, it should throw ApplicationException
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test06_saveExecutivePost() throws ApplicationException{
		final CategoryDto category = createAndSaveRandomCateory(appService, true, null);
		final DepartmentDto department = createRandomDepartment(category.getId());
		department.setId(randomPositiveLong());//assign ID as some non existing executive body id
		final String shortTitle = randomAlphaString(3);
		final String title = randomAlphaString(15);
		final String description = randomAlphaString(15);
		
		ExecutivePostDto executivePostDto = createExecutivePost(department, shortTitle, title, description);
		appService.saveExecutivePost(executivePostDto);// This should throw ValidationException
		
	}
	
	
}
