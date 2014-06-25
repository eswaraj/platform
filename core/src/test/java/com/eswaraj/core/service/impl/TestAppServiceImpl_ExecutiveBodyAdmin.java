package com.eswaraj.core.service.impl;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.eswaraj.core.BaseNeo4jEswarajTest;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.domain.nodes.ExecutiveBody;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.DepartmentDto;
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;
import com.eswaraj.web.dto.ExecutiveBodyDto;
import com.eswaraj.web.dto.ExecutivePostDto;
import com.eswaraj.web.dto.PersonDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl_ExecutiveBodyAdmin extends BaseNeo4jEswarajTest{

	@Autowired private AppService appService;
	@Autowired private PersonService personService;

	/**
	 * Create a ExecutiveBodyAdmin and then get it by getExecutiveBodyAdminById Service
	 * @throws ApplicationException
	 */
	@Test
	public void test01_saveExecutiveBodyAdmin() throws ApplicationException{
		final CategoryDto category = createAndSaveRandomCateory(appService, true, null);
		final AddressDto address = createRandomAddress();
		final DepartmentDto department = createAndSaveRandomDepartment(appService, category.getId());
		final ExecutiveBodyDto executiveBody = createAndSaveExecutiveBody(appService, randomAlphaString(16), address, null, department , true, null);
		final PersonDto person = createAndSaveRandomPerson(personService);
		final ExecutiveBodyAdminDto manager = null;
		final Date startDate = randomDateInPast();
		final Date endDate = randomDateInFuture();
		final ExecutivePostDto executivePost = createAndSaveExecutivePost(appService, department, randomAlphaString(3), randomAlphaString(30), randomAlphaString(128));

		ExecutiveBodyAdminDto executiveBodyAdminDto = createExecutiveBodyAdmin(executiveBody, manager, person, executivePost, startDate, endDate);
		ExecutiveBodyAdminDto savedExecutiveBodyAdminDto = appService.saveExecutiveBodyAdmin(executiveBodyAdminDto);
		assertEqualExecutiveBodyAdmin(executiveBodyAdminDto, savedExecutiveBodyAdminDto, false);
		
		ExecutiveBodyAdminDto dbExecutiveBodyAdminDto = appService.getExecutiveBodyAdminById(savedExecutiveBodyAdminDto.getId());
		assertEqualExecutiveBodyAdmin(executiveBodyAdminDto, dbExecutiveBodyAdminDto, false);
		assertEqualExecutiveBodyAdmin(savedExecutiveBodyAdminDto, dbExecutiveBodyAdminDto, true);
		
	}
	

	/**
	 * Create a ExecutiveBodyAdmin and pass ExecutiveBody as null, then it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test02_saveExecutiveBodyAdmin() throws ApplicationException{
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		final DepartmentDto department = createAndSaveRandomDepartment(appService, categoryDto.getId());
		final ExecutiveBodyDto executiveBody = null;
		final PersonDto person = createAndSaveRandomPerson(personService);
		final ExecutiveBodyAdminDto manager = null;
		final Date startDate = randomDateInPast();
		final Date endDate = randomDateInFuture();
		final ExecutivePostDto executivePost = createAndSaveExecutivePost(appService, department, randomAlphaString(3), randomAlphaString(30), randomAlphaString(128));

		ExecutiveBodyAdminDto executiveBodyAdminDto = createExecutiveBodyAdmin(executiveBody, manager, person, executivePost, startDate, endDate);
		appService.saveExecutiveBodyAdmin(executiveBodyAdminDto);// this should throw ValidationException
		
	}
	
	/**
	 * Create a ExecutiveBodyAdmin and pass Person as null, then it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test03_saveExecutiveBodyAdmin() throws ApplicationException{
		final CategoryDto category = createAndSaveRandomCateory(appService, true, null);
		final AddressDto address = createRandomAddress();
		final DepartmentDto department = createRandomDepartment(category.getId());
		final ExecutiveBodyDto executiveBody = createAndSaveExecutiveBody(appService, randomAlphaString(16), address, null, department , true, null);
		final PersonDto person = null;
		final ExecutiveBodyAdminDto manager = null;
		final Date startDate = randomDateInPast();
		final Date endDate = randomDateInFuture();
		final ExecutivePostDto executivePost = createAndSaveExecutivePost(appService, department, randomAlphaString(3), randomAlphaString(30), randomAlphaString(128));

		ExecutiveBodyAdminDto executiveBodyAdminDto = createExecutiveBodyAdmin(executiveBody, manager, person, executivePost, startDate, endDate);
		appService.saveExecutiveBodyAdmin(executiveBodyAdminDto);//this should throw ValdationException
		
	}
	
	
	
}
