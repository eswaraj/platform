package com.eswaraj.core.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

@ContextConfiguration(locations = { "classpath:eswaraj-core-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class TestAppServiceImpl extends BaseNeo4jEswarajTest{

	@Autowired private AppService appService;
	@Autowired private LocationService locationService;
	@Autowired private PersonService personService;

	/**
	 * Create a category and then get it by getcategory Service
	 * @throws ApplicationException
	 */
	@Test
	public void test01_saveCategory() throws ApplicationException{
		final String categoryName = randomAlphaString(16);
		final String categoryDescription = randomAlphaString(128);
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
		CategoryDto savedCategory = appService.saveCategory(categoryDto);
		assertEqualCategories(categoryDto, savedCategory, false);
		
		CategoryDto dbCategory = appService.getCategoryById(savedCategory.getId());
		assertEqualCategories(categoryDto, dbCategory, false);
		assertEqualCategories(savedCategory, dbCategory, true);
		
	}
	
	/**
	 * Create a category with name as null, it should throw exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test02_saveCategory() throws ApplicationException{
		final String categoryName = null;
		final String categoryDescription = randomAlphaString(128);
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
		appService.saveCategory(categoryDto);
		
	}
	
	/**
	 * Create a non root category and pass parent category as null
	 * it shud throw validation exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test03_saveCategory() throws ApplicationException{
		final String categoryName = null;
		final String categoryDescription = randomAlphaString(128);
		final boolean isRoot = false;
		final Long parentCategoryId = null;
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, isRoot, parentCategoryId);
		appService.saveCategory(categoryDto);
		
	}
	
	/**
	 * Create a root category and also try to create under it another category
	 * it shud throw validation exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test04_saveCategory() throws ApplicationException{
		CategoryDto parentRootCategoryDto = createCategory(randomAlphaString(16), randomAlphaString(128), true, null);
		parentRootCategoryDto = appService.saveCategory(parentRootCategoryDto);
		
		final String categoryName = null;
		final String categoryDescription = randomAlphaString(128);
		final boolean isRoot = true;
		final Long parentCategoryId = parentRootCategoryDto.getId();
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, isRoot, parentCategoryId);
		//This shud throw validation exception
		appService.saveCategory(categoryDto);
		
	}
	
	/**
	 * find all root categories from DB when no category exists
	 * @throws ApplicationException
	 */
	@Test
	public void test05_getRootCategories() throws ApplicationException{
		List<CategoryDto> allRootCategories = appService.getAllRootCategories();
		assertEquals(0, allRootCategories.size());
		
	}
	
	/**
	 * create 1 Root category and then find all root categories from DB
	 * it must return 1 category
	 * @throws ApplicationException
	 */
	@Test
	public void test06_getRootCategories() throws ApplicationException{
		final String categoryName = randomAlphaString(16);
		final String categoryDescription = randomAlphaString(128);
		CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
		CategoryDto savedCategory = appService.saveCategory(categoryDto);
		List<CategoryDto> allRootCategories = appService.getAllRootCategories();
		assertEquals(1, allRootCategories.size());
		assertEqualCategories(savedCategory, allRootCategories.get(0), true);
	}
	
	/**
	 * create n Root category and then find all root categories from DB
	 * it must return n category
	 * @throws ApplicationException
	 */
	@Test
	public void test07_getRootCategories() throws ApplicationException{
		int totalRootNodes = randomInteger(100);
		for(int i=0;i<totalRootNodes;i++){
			final String categoryName = randomAlphaString(16);
			final String categoryDescription = randomAlphaString(128);
			CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
			appService.saveCategory(categoryDto);
		}
		
		List<CategoryDto> allRootCategories = appService.getAllRootCategories();
		assertEquals(totalRootNodes , allRootCategories.size());
	}
	
	/**
	 * create n Root category and create mXn root categories then find all root categories from DB
	 * it must return n category
	 * also find child categories of each root node and we shud get m child under each
	 * @throws ApplicationException
	 */
	@Test
	public void test08_getRootCategories() throws ApplicationException{
		int totalRootNodes = randomInteger(100);
		int totalChildPerRootnode = 2;
		CategoryDto rootCategory;
		List<CategoryDto> allRootCategories = new ArrayList<>(totalRootNodes);
		for(int i=0;i<totalRootNodes;i++){
			final String categoryName = uniqueAlphaNumericString(16, "Category");
			final String categoryDescription = uniqueAlphaNumericString(128, "CategoryDescription");
			CategoryDto categoryDto = createCategory(categoryName, categoryDescription, true, null);
			rootCategory = appService.saveCategory(categoryDto);
			allRootCategories.add(rootCategory);
			//create child nodes
			for(int j=0;j<totalChildPerRootnode;j++){
				final String childCategoryName = uniqueAlphaNumericString(16, "Category");
				final String childCategoryDescription = uniqueAlphaNumericString(128, "CategoryDescription");
				CategoryDto childCategoryDto = createCategory(childCategoryName, childCategoryDescription, false, rootCategory.getId());
				appService.saveCategory(childCategoryDto);
				
			}
		}
		
		List<CategoryDto> allDbRootCategories = appService.getAllRootCategories();
		assertEquals(totalRootNodes , allDbRootCategories.size());
		
		for(CategoryDto oneRootCategory:allRootCategories){
			allDbRootCategories = appService.getAllChildCategoryOfParentCategory(oneRootCategory.getId());
			assertEquals(totalChildPerRootnode , allDbRootCategories.size());
		}
	}
	/**
	 * Create a PoliticaBodyType and then get it by getPoliticalBodyTypeById Service
	 * @throws ApplicationException
	 */
	@Test
	public void test09_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = randomAlphaString(128);
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		PoliticalBodyTypeDto savedPoliticalBodyType = appService.savePoliticalBodyType(politicalBodyTypeDto);
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, savedPoliticalBodyType, false);
		
		PoliticalBodyTypeDto dbPoliticalBodyType = appService.getPoliticalBodyTypeById(savedPoliticalBodyType.getId());
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, dbPoliticalBodyType, false);
		assertEqualPoliticalBodyTypes(savedPoliticalBodyType, dbPoliticalBodyType, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType with Short Name as null
	 * it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test10_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = null;
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = randomAlphaString(128);
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		appService.savePoliticalBodyType(politicalBodyTypeDto);//Should throw validationException
		
	}
	
	/**
	 * Create a PoliticaBodyType with Short Name as empty i.e. ""
	 * it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test11_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = "";
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = randomAlphaString(128);
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		appService.savePoliticalBodyType(politicalBodyTypeDto);//Should throw validationException
		
	}
	
	/**
	 * Create a PoliticaBodyType with Name as null
	 * it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test12_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = null;
		final String politicalBodyTypeDescription = randomAlphaString(128);
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		appService.savePoliticalBodyType(politicalBodyTypeDto);//Should throw validationException
		
	}
	
	/**
	 * Create a PoliticaBodyType with Name as empty i.e. ""
	 * it should throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test13_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = "";
		final String politicalBodyTypeDescription = randomAlphaString(128);
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		appService.savePoliticalBodyType(politicalBodyTypeDto);//Should throw validationException
		
	}
	
	/**
	 * Create a PoliticaBodyType with LocationTypeId as null
	 * it should throw ApplicationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test14_savePoliticalBodyType() throws ApplicationException{
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = randomAlphaString(128);
		final Long locationTypeId = null;
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationTypeId);
		appService.savePoliticalBodyType(politicalBodyTypeDto);//Should throw validationException
		
	}
	
	/**
	 * Create a PoliticaBodyType with unknown LocationTypeId i.e. that LocationType do no exists
	 * it should throw ApplicationException
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test15_savePoliticalBodyType() throws ApplicationException{
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = randomAlphaString(128);
		final Long locationTypeId = randomPositiveLong();
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationTypeId);
		appService.savePoliticalBodyType(politicalBodyTypeDto);//Should throw validationException
		
	}
	
	/**
	 * Create a PoliticaBodyType with description as null
	 * it shud work fine
	 * @throws ApplicationException
	 */
	@Test
	public void test16_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = null;
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		PoliticalBodyTypeDto savedPoliticalBodyType = appService.savePoliticalBodyType(politicalBodyTypeDto);
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, savedPoliticalBodyType, false);
		
		PoliticalBodyTypeDto dbPoliticalBodyType = appService.getPoliticalBodyTypeById(savedPoliticalBodyType.getId());
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, dbPoliticalBodyType, false);
		assertEqualPoliticalBodyTypes(savedPoliticalBodyType, dbPoliticalBodyType, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType with description as empty i.e. ""
	 * it shud work fine
	 * @throws ApplicationException
	 */
	@Test
	public void test17_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = "";
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		PoliticalBodyTypeDto savedPoliticalBodyType = appService.savePoliticalBodyType(politicalBodyTypeDto);
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, savedPoliticalBodyType, false);
		
		PoliticalBodyTypeDto dbPoliticalBodyType = appService.getPoliticalBodyTypeById(savedPoliticalBodyType.getId());
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, dbPoliticalBodyType, false);
		assertEqualPoliticalBodyTypes(savedPoliticalBodyType, dbPoliticalBodyType, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType and get it back with 
	 * @throws ApplicationException
	 */
	@Test
	public void test18_savePoliticalBodyType() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		final String politicalBodyTypeShortName = randomAlphaString(3);
		final String politicalBodyTypeName = randomAlphaString(16);
		final String politicalBodyTypeDescription = "";
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(politicalBodyTypeShortName,politicalBodyTypeName,  politicalBodyTypeDescription, locationType.getId());
		PoliticalBodyTypeDto savedPoliticalBodyType = appService.savePoliticalBodyType(politicalBodyTypeDto);
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, savedPoliticalBodyType, false);
		
		List<PoliticalBodyTypeDto> dbPoliticalBodyTypes = appService.getAllPoliticalBodyTypes();
		assertEquals(1, dbPoliticalBodyTypes.size());
		assertEqualPoliticalBodyTypes(politicalBodyTypeDto, dbPoliticalBodyTypes.get(0), false);
		assertEqualPoliticalBodyTypes(savedPoliticalBodyType, dbPoliticalBodyTypes.get(0), true);
		
	}
	/**
	 * Create one party and get it back by getPartyById
	 * @throws ApplicationException
	 */
	@Test
	public void test19_saveParty() throws ApplicationException{
		PartyDto partyDto = createParty(randomAlphaString(16));
		PartyDto savedPartyDto = appService.saveParty(partyDto);
		assertEqualParties(partyDto, savedPartyDto, false);
		
		PartyDto dbPartyDto = appService.getPartyById(savedPartyDto.getId());
		assertEqualParties(partyDto, dbPartyDto, false);
		assertEqualParties(savedPartyDto, dbPartyDto, false);
	}
	
	/**
	 * create a party with name as null, it shud throw Validation Exception
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test20_saveParty() throws ApplicationException{
		PartyDto partyDto = createParty(null);
		appService.saveParty(partyDto);//This hsould throw exception
	}
	/**
	 * create N parties and get them back by getAllParties
	 * @throws ApplicationException
	 */
	@Test
	public void test21_saveParty() throws ApplicationException{
		int totalParties = randomInteger(20);
		List<PartyDto> allPartiesToBeCreated = new ArrayList<>(totalParties);
		List<PartyDto> allPartiesCreated = new ArrayList<>(totalParties);
		PartyDto partyDto;
		PartyDto savedPartyDto;
		for(int i=0;i<totalParties;i++){
			partyDto = createParty(randomAlphaString(16));
			allPartiesToBeCreated.add(partyDto);
			savedPartyDto = appService.saveParty(partyDto);
			allPartiesCreated.add(savedPartyDto);
			assertEqualParties(partyDto, savedPartyDto, false);
		}
		
		
		List<PartyDto> dbPartyDtos = appService.getAllPoliticalParties();
		assertEquals(totalParties, dbPartyDtos.size());
	}
	
	/**
	 * Create a PoliticaBodyType and get it back with getPoliticalBodyById 
	 * @throws ApplicationException
	 */
	@Test
	public void test12_savePoliticalBody() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateAfter(startDate);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType when End Date is null
	 * it shud save and work fine 
	 * @throws ApplicationException
	 */
	@Test
	public void test13_savePoliticalBody() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = null;
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType when End Date is before start Date
	 * it shud throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test14_savePoliticalBody() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateBefore(startDate);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType when location is null
	 * it shud throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test15_savePoliticalBody() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = null;
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateAfter(startDate);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	
	/**
	 * Create a PoliticaBodyType when political Body Type is null
	 * it shud throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test16_savePoliticalBody() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = null;
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateAfter(startDate);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType when political party is null
	 * it shud throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test17_savePoliticalBody() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = null;//createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateAfter(startDate);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType when Person is null
	 * it shud work fine
	 * @throws ApplicationException
	 */
	@Test
	public void test18_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = null;//createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = randomDateInPast();
		Date endDate = randomDateAfter(startDate);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create a PoliticaBodyType when startDate is null
	 * it shud throw ValidationException
	 * @throws ApplicationException
	 */
	@Test(expected=ValidationException.class)
	public void test19_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate = null;
		Date endDate = null;
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto = createPoliticalBodyAdminDto(active, email, startDate, endDate, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto = appService.savePoliticalBodyAdmin(politicalBodyAdminDto);
		
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, savedPoliticalBodyAdminDto, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto, dbPoliticalBodyAdminDto, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto, dbPoliticalBodyAdminDto, true);
		
	}
	
	/**
	 * Create one PoliticaBodyType as Active and then create anothEr one as active then first one shud become inactive
	 * it shud work fine
	 * @throws ApplicationException
	 */
	@Test
	public void test20_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = null;//createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate1 = randomDateInPast();
		Date endDate1 = randomDateAfter(startDate1);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto1 = createPoliticalBodyAdminDto(active, email, startDate1, endDate1, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto1 = appService.savePoliticalBodyAdmin(politicalBodyAdminDto1);
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, savedPoliticalBodyAdminDto1, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto1 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto1.getId());
		assertTrue(dbPoliticalBodyAdminDto1.isActive());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, dbPoliticalBodyAdminDto1, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto1, dbPoliticalBodyAdminDto1, true);

		
		
		Date startDate2 = nextDayAfter(endDate1);
		Date endDate2 = randomDateAfter(startDate2);

		PoliticalBodyAdminDto politicalBodyAdminDto2 = createPoliticalBodyAdminDto(active, email, startDate2, endDate2, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);

		PoliticalBodyAdminDto savedPoliticalBodyAdminDto2 = appService.savePoliticalBodyAdmin(politicalBodyAdminDto2);
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto2, savedPoliticalBodyAdminDto2, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto2 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto2.getId());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto2, dbPoliticalBodyAdminDto2, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto2, dbPoliticalBodyAdminDto2, true);
		
		dbPoliticalBodyAdminDto1 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto1.getId());
		assertFalse(dbPoliticalBodyAdminDto1.isActive());
		assertTrue(dbPoliticalBodyAdminDto2.isActive());
		
	}
	
	/**
	 * Create one PoliticaBodyType and then create anothEr one , both has null end Date
	 * it shud throw ApplicationException as dates are overlapped
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test21_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = null;//createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate1 = randomDateInPast();
		Date endDate1 = null;
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto1 = createPoliticalBodyAdminDto(active, email, startDate1, endDate1, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto1 = appService.savePoliticalBodyAdmin(politicalBodyAdminDto1);
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, savedPoliticalBodyAdminDto1, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto1 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto1.getId());
		assertTrue(dbPoliticalBodyAdminDto1.isActive());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, dbPoliticalBodyAdminDto1, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto1, dbPoliticalBodyAdminDto1, true);

		
		
		Date startDate2 = nextDayAfter(startDate1);
		Date endDate2 = null;

		PoliticalBodyAdminDto politicalBodyAdminDto2 = createPoliticalBodyAdminDto(active, email, startDate2, endDate2, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);

		appService.savePoliticalBodyAdmin(politicalBodyAdminDto2);// this should thow applicationException
		
	}
	
	/**
	 * Create one PoliticaBodyType and then create another one , both has Non null end Date but dates overlap
	 * it shud throw ApplicationException as dates are overlapped
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test22_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = null;//createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate1 = randomDateInPast();
		Date endDate1 = randomDateAfter(startDate1);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto1 = createPoliticalBodyAdminDto(active, email, startDate1, endDate1, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto1 = appService.savePoliticalBodyAdmin(politicalBodyAdminDto1);
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, savedPoliticalBodyAdminDto1, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto1 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto1.getId());
		assertTrue(dbPoliticalBodyAdminDto1.isActive());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, dbPoliticalBodyAdminDto1, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto1, dbPoliticalBodyAdminDto1, true);

		
		
		Date startDate2 = nextDayAfter(startDate1);
		Date endDate2 = randomDateAfter(endDate1);

		PoliticalBodyAdminDto politicalBodyAdminDto2 = createPoliticalBodyAdminDto(active, email, startDate2, endDate2, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);

		appService.savePoliticalBodyAdmin(politicalBodyAdminDto2);// this should thow applicationException
		
	}
	
	/**
	 * Create one PoliticaBodyType and then create another one , one has Non null end Date other has null endDate and dates overlap
	 * it shud throw ApplicationException as dates are overlapped
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test23_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = null;//createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate1 = randomDateInPast();
		Date endDate1 = null;
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto1 = createPoliticalBodyAdminDto(active, email, startDate1, endDate1, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto1 = appService.savePoliticalBodyAdmin(politicalBodyAdminDto1);
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, savedPoliticalBodyAdminDto1, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto1 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto1.getId());
		assertTrue(dbPoliticalBodyAdminDto1.isActive());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, dbPoliticalBodyAdminDto1, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto1, dbPoliticalBodyAdminDto1, true);

		
		
		Date startDate2 = randomDateBefore(startDate1);
		Date endDate2 = randomDateAfter(startDate1);

		PoliticalBodyAdminDto politicalBodyAdminDto2 = createPoliticalBodyAdminDto(active, email, startDate2, endDate2, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);

		appService.savePoliticalBodyAdmin(politicalBodyAdminDto2);// this should thow applicationException
		
	}
	
	/**
	 * Create one PoliticaBodyType and then create another one , one has Null end Date other has Non null endDate and dates overlap
	 * it shud throw ApplicationException as dates are overlapped
	 * @throws ApplicationException
	 */
	@Test(expected=ApplicationException.class)
	public void test24_savePoliticalBodyAdmin() throws ApplicationException{
		LocationTypeDto locationType = createAndSaveLocationType(locationService, randomAlphaString(16), null, true);
		LocationDto location = createAndSaveLocation(locationService, randomAlphaString(16), locationType, null);
		PoliticalBodyTypeDto politicalBodyType = createAndSavePoliticalBodyType(appService, randomAlphaString(3),randomAlphaString(16),  randomAlphaString(256), locationType.getId());
		PartyDto party = createAndSaveParty(appService, randomAlphaString(16));
		PersonDto person = null;//createAndSaveRandomPerson(personService);
		
		boolean active = true;
		String email = randomEmailAddress();
		Date startDate1 = randomDateInPast();
		Date endDate1 = randomDateAfter(startDate1);
		AddressDto homeAddressDto = createRandomAddress();
		AddressDto officeAddressDto = createRandomAddress();
		String landLine1 = randomNumericString(10);
		String landLine2 = randomNumericString(10);
		String mobile1 = randomNumericString(10);
		String mobile2 = randomNumericString(10);
		
		PoliticalBodyAdminDto politicalBodyAdminDto1 = createPoliticalBodyAdminDto(active, email, startDate1, endDate1, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);
		PoliticalBodyAdminDto savedPoliticalBodyAdminDto1 = appService.savePoliticalBodyAdmin(politicalBodyAdminDto1);
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, savedPoliticalBodyAdminDto1, false);
		
		PoliticalBodyAdminDto dbPoliticalBodyAdminDto1 = appService.getPoliticalBodyAdminById(savedPoliticalBodyAdminDto1.getId());
		assertTrue(dbPoliticalBodyAdminDto1.isActive());
		assertEqualPoliticalBodyAdmin(politicalBodyAdminDto1, dbPoliticalBodyAdminDto1, false);
		assertEqualPoliticalBodyAdmin(savedPoliticalBodyAdminDto1, dbPoliticalBodyAdminDto1, true);

		
		
		Date startDate2 = nextDayAfter(startDate1);
		Date endDate2 = null;

		PoliticalBodyAdminDto politicalBodyAdminDto2 = createPoliticalBodyAdminDto(active, email, startDate2, endDate2, homeAddressDto, officeAddressDto,
				landLine1, landLine2, mobile1, mobile2, location, party, person, politicalBodyType);

		appService.savePoliticalBodyAdmin(politicalBodyAdminDto2);// this should thow applicationException
		
	}
	
	
}
