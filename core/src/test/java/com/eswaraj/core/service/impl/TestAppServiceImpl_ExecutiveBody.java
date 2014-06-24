package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
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
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.ExecutiveBodyDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl_ExecutiveBody extends BaseNeo4jEswarajTest{

	@Autowired private AppService appService;
	@Autowired private LocationService locationService;
	@Autowired private PersonService personService;

	/**
	 * Create a ExecutiveBody and then get it by getExecutiveBodyById Service
	 * @throws ApplicationException
	 */
	@Test
	public void test01_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final boolean isRoot = true;
		final ExecutiveBodyDto parentExecutiveBody = null;
		CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, isRoot, parentExecutiveBody);
		ExecutiveBodyDto savedExecutiveBodyDto = appService.saveExecutiveBody(executiveBodyDto);
		assertEqualExecutiveBodies(executiveBodyDto, savedExecutiveBodyDto, false);
		
		ExecutiveBodyDto dbExecutiveBodyDto = appService.getExecutiveBodyById(savedExecutiveBodyDto.getId());
		assertEqualExecutiveBodies(executiveBodyDto, dbExecutiveBodyDto, false);
		assertEqualExecutiveBodies(savedExecutiveBodyDto, dbExecutiveBodyDto, true);
		
	}
	
	
	/**
	 * Create a ExecutiveBody with category passed as null
	 * It must throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test02_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final boolean isRoot = true;
		final ExecutiveBodyDto parentExecutiveBody = null;
		final CategoryDto categoryDto = null;
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, isRoot, parentExecutiveBody);
		appService.saveExecutiveBody(executiveBodyDto);//This call should throw validation exception
		
	}
	/**
	 * Create a ExecutiveBody with an unknown category, i.e. category Do not exists in database
	 * It should throw ApplicationException
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test03_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final boolean isRoot = true;
		final ExecutiveBodyDto parentExecutiveBody = null;
		final CategoryDto categoryDto = createRandomCateory(true, null);
		//Above category hasn't been saved in database and will just assign any unknown Id to it
		categoryDto.setId(randomPositiveLong());
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, isRoot, parentExecutiveBody);
		appService.saveExecutiveBody(executiveBodyDto);//This should throw ApplicationException
		
		
	}
	
	/**
	 * Create a Non ROOT ExecutiveBody with no Parent Executive Body
	 * It should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test04_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final boolean isRoot = false;
		final ExecutiveBodyDto parentExecutiveBody = null;
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, isRoot, null);
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, isRoot, parentExecutiveBody);
		appService.saveExecutiveBody(executiveBodyDto);//This should throw ValidationException
	}
	
	/**
	 * Create a  ROOT ExecutiveBody with a Parent Executive Body
	 * It should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test05_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		ExecutiveBodyDto parentExecutiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, true, null);
		parentExecutiveBodyDto = appService.saveExecutiveBody(parentExecutiveBodyDto);
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, true, parentExecutiveBodyDto);
		appService.saveExecutiveBody(executiveBodyDto);//This should throw ValidationException
	}
	
	/**
	 * Create a  NON-ROOT ExecutiveBody with a Parent Executive Body
	 * @throws ApplicationException
	 */
	@Test
	public void test06_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		ExecutiveBodyDto parentExecutiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, true, null);
		parentExecutiveBodyDto = appService.saveExecutiveBody(parentExecutiveBodyDto);
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, false, parentExecutiveBodyDto);
		ExecutiveBodyDto savedExecutiveBodyDto = appService.saveExecutiveBody(executiveBodyDto);
		assertEqualExecutiveBodies(executiveBodyDto, savedExecutiveBodyDto, false);
		
		ExecutiveBodyDto dbExecutiveBodyDto = appService.getExecutiveBodyById(savedExecutiveBodyDto.getId());
		assertEqualExecutiveBodies(executiveBodyDto, dbExecutiveBodyDto, false);
		assertEqualExecutiveBodies(savedExecutiveBodyDto, dbExecutiveBodyDto, true);
	}
	
	/**
	 * Create a  1 ROOT ExecutiveBody and then get them back with getAllRootExecutiveBodyOfCategory
	 * @throws ApplicationException
	 */
	@Test
	public void test07_saveExecutiveBody() throws ApplicationException{
		final String name = randomAlphaString(16);
		final AddressDto addressDto = createRandomAddress();
		final Long boundaryId = null;
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, true, null);
		ExecutiveBodyDto savedExecutiveBodyDto = appService.saveExecutiveBody(executiveBodyDto);
		assertEqualExecutiveBodies(executiveBodyDto, savedExecutiveBodyDto, false);
		
		List<ExecutiveBodyDto> dbExecutiveBodyDto = appService.getAllRootExecutiveBodyOfCategory(categoryDto.getId());
		assertEqualExecutiveBodies(executiveBodyDto, dbExecutiveBodyDto.get(0), false);
		assertEqualExecutiveBodies(savedExecutiveBodyDto, dbExecutiveBodyDto.get(0), true);
	}
	
	
	/**
	 * Create a  n ROOT ExecutiveBody and then get them back with getAllRootExecutiveBodyOfCategory
	 * @throws ApplicationException
	 */
	@Test
	public void test08_saveExecutiveBody() throws ApplicationException{
		int totalRootExecutivBodies = randomInteger(20);
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		List<ExecutiveBodyDto> savedExecutiveBodyDtoList = new ArrayList<>(totalRootExecutivBodies);
		for(int i=0;i<totalRootExecutivBodies;i++){
			final String name = randomAlphaString(16);
			final AddressDto addressDto = createRandomAddress();
			final Long boundaryId = null;
			
			ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, true, null);
			ExecutiveBodyDto savedExecutiveBodyDto = appService.saveExecutiveBody(executiveBodyDto);
			savedExecutiveBodyDtoList.add(savedExecutiveBodyDto);
			assertEqualExecutiveBodies(executiveBodyDto, savedExecutiveBodyDto, false);
		}
		
		List<ExecutiveBodyDto> dbExecutiveBodyDto = appService.getAllRootExecutiveBodyOfCategory(categoryDto.getId());
		assertEquals(totalRootExecutivBodies, dbExecutiveBodyDto.size());
		for(int i=0;i<totalRootExecutivBodies;i++){
			assertEqualExecutiveBodies(savedExecutiveBodyDtoList.get(i), dbExecutiveBodyDto.get(i), true);
		}
	}
	
	/**
	 * Create a   ROOT ExecutiveBody and then create n child Executive Bodies and then get them back by 
	 * @throws ApplicationException
	 */
	@Test
	public void test09_saveExecutiveBody() throws ApplicationException{
		int totalRootExecutivBodies = randomInteger(20);
		final CategoryDto categoryDto = createAndSaveRandomCateory(appService, true, null);
		
		ExecutiveBodyDto parentExecutiveBodyDto = createExecutiveBody(randomAlphaString(16), createRandomAddress(), null, categoryDto, true, null);
		parentExecutiveBodyDto = appService.saveExecutiveBody(parentExecutiveBodyDto);
		List<ExecutiveBodyDto> savedExecutiveBodyDtoList = new ArrayList<>(totalRootExecutivBodies);
		for(int i=0;i<totalRootExecutivBodies;i++){
			final String name = randomAlphaString(16);
			final AddressDto addressDto = createRandomAddress();
			final Long boundaryId = null;
			
			ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, categoryDto, false, parentExecutiveBodyDto);
			ExecutiveBodyDto savedExecutiveBodyDto = appService.saveExecutiveBody(executiveBodyDto);
			savedExecutiveBodyDtoList.add(savedExecutiveBodyDto);
			assertEqualExecutiveBodies(executiveBodyDto, savedExecutiveBodyDto, false);
		}
		
		List<ExecutiveBodyDto> dbExecutiveBodyDto = appService.getAllChildExecutiveBodyOfParent(parentExecutiveBodyDto.getId());
		assertEquals(totalRootExecutivBodies, dbExecutiveBodyDto.size());
		for(int i=0;i<totalRootExecutivBodies;i++){
			assertEqualExecutiveBodies(savedExecutiveBodyDtoList.get(i), dbExecutiveBodyDto.get(i), true);
		}
	}
}
