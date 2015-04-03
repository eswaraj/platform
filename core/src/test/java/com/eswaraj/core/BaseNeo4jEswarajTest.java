package com.eswaraj.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.eswaraj.base.BaseEswarajTest;
import com.eswaraj.base.aspect.TestObjectContextManager;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.AppService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.service.PersonService;
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.DepartmentDto;
import com.eswaraj.web.dto.ExecutiveBodyAdminDto;
import com.eswaraj.web.dto.ExecutiveBodyDto;
import com.eswaraj.web.dto.ExecutivePostDto;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PoliticalBodyAdminDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

public class BaseNeo4jEswarajTest extends BaseEswarajTest {

	@Autowired Neo4jTemplate neo4jTemplate;
	@Autowired(required=false) TestObjectContextManager testObjectContextManager;

	@Autowired
	private GraphDatabaseService graphDatabaseService;
    @Autowired
    private ApplicationContext context;
	
	@Before
	public void init(){
        String[] profiles = context.getEnvironment().getActiveProfiles();
        System.out.println("All Active Spring Profiles are for the test are: ");
        if (profiles != null) {
            for (String oneProfile : profiles) {
                System.out.println("        " + oneProfile);
            }
        }
	}

	@After
	public void destroyTest(){
		if(testObjectContextManager != null){
			Transaction txn = graphDatabaseService.beginTx();
			try{
				testObjectContextManager.clearAllObjectsCreatdDuringTest();
				txn.success();
			}catch(Exception ex){
				txn.failure();
			}finally{
				txn.finish();
			}
		}
	}
	
	protected LocationDto createLocation(String name, LocationTypeDto locationTypeDto, Long parentLocationId){
		LocationDto location = new LocationDto();
		location.setName(name);
		if(locationTypeDto != null){
			location.setLocationTypeId(locationTypeDto.getId());	
		}
		location.setParentLocationId(parentLocationId);
		return location;
	}
	protected LocationDto createAndSaveLocation(LocationService locationService,String name, LocationTypeDto locationTypeDto, Long parentLocationId) throws ApplicationException{
		LocationDto location = createLocation(name, locationTypeDto, parentLocationId);
		location = locationService.saveLocation(location);
		return location;
	}
	protected LocationTypeDto createLocationType(String name, Long parentLocationTypeId){
		LocationTypeDto location = new LocationTypeDto();
		location.setName(name);
		location.setParentLocationTypeId(parentLocationTypeId);
		return location;
	}
	protected LocationTypeDto createAndSaveLocationType(LocationService locationService, String name, Long parentLocationTypeId, boolean root) throws ApplicationException{
		LocationTypeDto locationTypeDto = createLocationType(name, parentLocationTypeId);
		if(root){
			locationTypeDto = locationService.saveRootLocationType(locationTypeDto);
		}else{
			locationTypeDto = locationService.saveLocationType(locationTypeDto);	
		}
		
		return locationTypeDto;
	}
	/**
	 * If you dont want your test to delete newly created DB objects to be deleted, then call this method from your test
	 */
	protected void dontDeleteDbObjects(){
		testObjectContextManager.setDontDeleteForThisTest(true);
	}
	
	protected void assertEqualLocations(LocationDto expectedLocation, LocationDto actualLocation){
		assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude());
		assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude());
		assertEquals(expectedLocation.getLocationTypeId(), actualLocation.getLocationTypeId());
		assertEquals(expectedLocation.getParentLocationId(), actualLocation.getParentLocationId());
		assertEquals(expectedLocation.getName(), actualLocation.getName());
	}
	
	protected void assertEqualLocationTypes(LocationTypeDto expectedLocation, LocationTypeDto actualLocation, boolean checkId){
		if(checkId){
			assertEquals(expectedLocation.getId(), actualLocation.getId());	
		}
		assertEquals(expectedLocation.getName(), actualLocation.getName());
	}

	protected void assertEqualCategories(CategoryDto expectedCategory, CategoryDto actualCategory, boolean checkId){
		if(checkId){
			assertEquals(expectedCategory.getId(), actualCategory.getId());	
		}
		assertEquals(expectedCategory.getName(), actualCategory.getName());
		assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
		assertEquals(expectedCategory.getParentCategoryId(), actualCategory.getParentCategoryId());
		assertEquals(expectedCategory.isRoot(), actualCategory.isRoot());
	}
	
	protected CategoryDto createCategory(String categoryName, String description, boolean isRoot, Long parentCategoryId){
		CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName(categoryName);
		categoryDto.setDescription(description);
		categoryDto.setRoot(isRoot);
		categoryDto.setParentCategoryId(parentCategoryId);
		return categoryDto;
	}
	/**
	 * Create a Random Category 
	 * @param isRoot - true If you want to make this as root category
	 * @param parentCategoryId - if isRoot is false then you must provide an existing categoryId here
	 * @return
	 */
	protected CategoryDto createRandomCateory(boolean isRoot, Long parentCategoryId){
		String categoryName = randomAlphaString(16);
		String description = randomAlphaString(16);
		return createCategory(categoryName, description, isRoot, parentCategoryId);
	}
	protected CategoryDto createAndSaveRandomCateory(AppService appService, boolean isRoot, Long parentCategoryId) throws ApplicationException{
		CategoryDto categoryDto = createRandomCateory(isRoot, parentCategoryId);
		return appService.saveCategory(categoryDto);
	}

	
	protected ExecutiveBodyDto createExecutiveBody(String name, AddressDto addressDto, Long boundaryId, DepartmentDto departmentDto,  boolean isRoot, ExecutiveBodyDto parentExecutiveBody){
		ExecutiveBodyDto executiveBodyDto = new ExecutiveBodyDto();
		executiveBodyDto.setName(name);
		executiveBodyDto.setAddressDto(addressDto);
		executiveBodyDto.setBoundaryId(boundaryId);
		if(departmentDto != null){
			executiveBodyDto.setDepartmentId(departmentDto.getId());	
		}
		if(parentExecutiveBody != null){
			executiveBodyDto.setParentExecutiveBodyId(parentExecutiveBody.getId());	
		}
		executiveBodyDto.setRoot(isRoot);
		return executiveBodyDto;
	}
	protected ExecutiveBodyDto createRandomExecutiveBody(DepartmentDto departmentDto, boolean isRoot, ExecutiveBodyDto parentExecutiveBody){
		String name = randomAlphaString(16);
		AddressDto addressDto = createRandomAddress();
		Long boundaryId = null;
		ExecutiveBodyDto executiveBodyDto = createExecutiveBody(name, addressDto, boundaryId, departmentDto, isRoot, parentExecutiveBody);
		return executiveBodyDto;
	}
	protected void assertEqualExecutiveBodies(ExecutiveBodyDto expectedExecutiveBody, ExecutiveBodyDto actualExecutiveBodyDto, boolean checkId){
		if(checkId){
			assertEquals(expectedExecutiveBody.getId(), actualExecutiveBodyDto.getId());	
		}
		assertEquals(expectedExecutiveBody.getName(), actualExecutiveBodyDto.getName());
		assertEquals(expectedExecutiveBody.getBoundaryId(), actualExecutiveBodyDto.getBoundaryId());
		assertEquals(expectedExecutiveBody.getDepartmentId(), actualExecutiveBodyDto.getDepartmentId());
		assertEquals(expectedExecutiveBody.isRoot(), actualExecutiveBodyDto.isRoot());
		assertEquals(expectedExecutiveBody.getParentExecutiveBodyId(), actualExecutiveBodyDto.getParentExecutiveBodyId());
		assertEqualAddresses(expectedExecutiveBody.getAddressDto(), actualExecutiveBodyDto.getAddressDto(), checkId);
	}

	
	protected PoliticalBodyTypeDto createPoliticalBodyType(String shortName, String name, String description, Long locationTypeId){
		PoliticalBodyTypeDto politicalBodyTypeDto = new PoliticalBodyTypeDto();
		politicalBodyTypeDto.setName(name);
		politicalBodyTypeDto.setShortName(shortName);
		politicalBodyTypeDto.setDescription(description);
		politicalBodyTypeDto.setLocationTypeId(locationTypeId);
		return politicalBodyTypeDto;
	}
	protected PoliticalBodyTypeDto createAndSavePoliticalBodyType(AppService appService, String shortName, String name, String description, Long locationTypeId) throws ApplicationException{
		PoliticalBodyTypeDto politicalBodyTypeDto = createPoliticalBodyType(shortName, name, description, locationTypeId);
		politicalBodyTypeDto = appService.savePoliticalBodyType(politicalBodyTypeDto);
		return politicalBodyTypeDto;
	}
	
	protected PoliticalBodyTypeDto createRandomPoliticalBodyType(Long locationTypeId){
		String shortName = randomAlphaString(3);
		String name = randomAlphaString(16);
		String description = randomAlphaString(256);
		return createPoliticalBodyType(shortName, name, description, locationTypeId);
	}
	protected PoliticalBodyTypeDto createAndSaveRandomPoliticalBodyType(AppService appService, Long locationTypeId) throws ApplicationException{
		PoliticalBodyTypeDto politicalBodyTypeDto = createRandomPoliticalBodyType(locationTypeId);
		politicalBodyTypeDto = appService.savePoliticalBodyType(politicalBodyTypeDto);
		return politicalBodyTypeDto;
	}
	protected void assertEqualPoliticalBodyTypes(PoliticalBodyTypeDto expectedCategory, PoliticalBodyTypeDto actualCategory, boolean checkId){
		if(checkId){
			assertEquals(expectedCategory.getId(), actualCategory.getId());	
		}
		assertEquals(expectedCategory.getName(), actualCategory.getName());
		assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
		assertEquals(expectedCategory.getShortName(), actualCategory.getShortName());
		assertEquals(expectedCategory.getLocationTypeId(), actualCategory.getLocationTypeId());
	}
	
	protected PartyDto createParty(String name){
		PartyDto partyDto = new PartyDto();
		partyDto.setName(name);
		return partyDto;
	}
	protected PartyDto createAndSaveParty(AppService appService, String name) throws ApplicationException{
		PartyDto partyDto = new PartyDto();
		partyDto.setName(name);
		partyDto = appService.saveParty(partyDto);
		return partyDto;
	}
	protected void assertEqualParties(PartyDto expectedParty, PartyDto actualParty, boolean checkId){
		if(checkId){
			assertEquals(expectedParty.getId(), actualParty.getId());	
		}
		assertEquals(expectedParty.getName(), actualParty.getName());
	}
	
	protected PersonDto createPerson(String name, String email, String bioData, Date dob, String gender, String landlineNumber1, 
			String landlineNumber2,String mobileNumber1, String mobileNumber2, AddressDto personAddress){
		PersonDto personDto = new PersonDto();
		personDto.setName(name);
		personDto.setEmail(email);
		personDto.setBiodata(bioData);
		personDto.setDob(dob);
		personDto.setGender(gender);
		personDto.setLandlineNumber1(landlineNumber1);
		personDto.setLandlineNumber2(landlineNumber2);
		personDto.setMobileNumber1(mobileNumber1);
		personDto.setMobileNumber2(mobileNumber2);
		personDto.setPersonAddress(personAddress);
		return personDto;
	}
	protected PersonDto createRandomPerson(){
		String name = randomAlphaString(16);
		String email = randomEmailAddress();
		String bioData = randomAlphaString(1024);
		Date dob = randomDateInPast();
		String gender = randomAlphaString(4);
		String landlineNumber1 = randomNumericString(10);
		String landlineNumber2 = randomNumericString(10);
		String mobileNumber1 = randomNumericString(10);
		String mobileNumber2 = randomNumericString(10);

		AddressDto personAddress = createRandomAddress();
		return createPerson(name, email, bioData, dob, gender, landlineNumber1, landlineNumber2, mobileNumber1, mobileNumber2, personAddress);
	}
	protected PersonDto createAndSaveRandomPerson(PersonService personService) throws ApplicationException{
		String name = randomAlphaString(16);
		String email = randomEmailAddress();
		String bioData = randomAlphaString(1024);
		Date dob = randomDateInPast();
		String gender = randomAlphaString(4);
		String landlineNumber1 = randomNumericString(10);
		String landlineNumber2 = randomNumericString(10);
		String mobileNumber1 = randomNumericString(10);
		String mobileNumber2 = randomNumericString(10);

		AddressDto personAddress = createRandomAddress();
		PersonDto person = createPerson(name, email, bioData, dob, gender, landlineNumber1, landlineNumber2, mobileNumber1, mobileNumber2, personAddress);
		return personService.savePerson(person);
	}
	protected AddressDto createAddress(String line1, String line2, String line3, String postalCode){
		AddressDto address = new AddressDto();
		address.setLine1(line1);
		address.setLine2(line2);
		address.setLine3(line3);
		address.setPostalCode(postalCode);
		return address;
	}
	protected AddressDto createRandomAddress(){
		String line1 = randomAlphaString(16);
		String line2 = randomAlphaString(16);
		String line3 = randomAlphaString(16);
		String postalCode = randomNumericString(6);
		return createAddress(line1, line2, line3, postalCode);
	}
	protected void assertEqualPersons(PersonDto expectedPerson, PersonDto actualPerson, boolean checkId){
		if(checkId){
			assertEquals(expectedPerson.getId(), actualPerson.getId());	
		}
		assertEquals(expectedPerson.getName(), actualPerson.getName());
		assertEquals(expectedPerson.getBiodata(), actualPerson.getBiodata());
		assertEquals(expectedPerson.getDob(), actualPerson.getDob());
		assertEquals(expectedPerson.getEmail(), actualPerson.getEmail());
		assertEquals(expectedPerson.getGender(), actualPerson.getGender());
		assertEquals(expectedPerson.getLandlineNumber1(), actualPerson.getLandlineNumber1());
		assertEquals(expectedPerson.getLandlineNumber2(), actualPerson.getLandlineNumber2());
		assertEquals(expectedPerson.getMobileNumber1(), actualPerson.getMobileNumber1());
		assertEquals(expectedPerson.getMobileNumber2(), actualPerson.getMobileNumber2());
		
		if(expectedPerson.getPersonAddress() == null && actualPerson.getPersonAddress() != null){
			fail("Person Address do not match, expected null but found not null "+ actualPerson.getPersonAddress());
		}
		if(expectedPerson.getPersonAddress() != null && actualPerson.getPersonAddress() == null){
			fail("Person Address do not match, expected Not null but found null ");
		}
		if(expectedPerson.getPersonAddress() != null && actualPerson.getPersonAddress() != null){
			assertEqualAddresses(expectedPerson.getPersonAddress(), actualPerson.getPersonAddress(), checkId);
		}
	}
	
	protected void assertEqualAddresses(AddressDto expectedAddress, AddressDto actualAddress, boolean checkId){
		if(checkId){
			assertEquals(expectedAddress.getId(), actualAddress.getId());	
		}
		assertEquals(expectedAddress.getCityId(), actualAddress.getCityId());
		assertEquals(expectedAddress.getCountryId(), actualAddress.getCountryId());
		assertEquals(expectedAddress.getDistrictId(), actualAddress.getDistrictId());
		assertEquals(expectedAddress.getLine1(), actualAddress.getLine1());
		assertEquals(expectedAddress.getLine2(), actualAddress.getLine2());
		assertEquals(expectedAddress.getLine3(), actualAddress.getLine3());
		assertEquals(expectedAddress.getPostalCode(), actualAddress.getPostalCode());
		assertEquals(expectedAddress.getStateId(), actualAddress.getStateId());
		assertEquals(expectedAddress.getVillageId(), actualAddress.getVillageId());
		assertEquals(expectedAddress.getWardId(), actualAddress.getWardId());
	}
	
	protected PoliticalBodyAdminDto createPoliticalBodyAdminDto(boolean active,String email,Date startDate, Date endDate, AddressDto homeAddressDto, AddressDto officeAddressDto,
			String landLine1, String landLine2, String mobile1, String mobile2, LocationDto location, PartyDto party, PersonDto person, PoliticalBodyTypeDto politicalBodyType){
		PoliticalBodyAdminDto politicalBodyAdminDto = new PoliticalBodyAdminDto();
		politicalBodyAdminDto.setActive(active);
		politicalBodyAdminDto.setEmail(email);
		politicalBodyAdminDto.setEndDate(endDate);
		politicalBodyAdminDto.setStartDate(startDate);
		politicalBodyAdminDto.setHomeAddressDto(homeAddressDto);
		politicalBodyAdminDto.setOfficeAddressDto(officeAddressDto);
		politicalBodyAdminDto.setLandLine1(landLine1);
		politicalBodyAdminDto.setLandLine2(landLine2);
		if(location != null){
			politicalBodyAdminDto.setLocationId(location.getId());	
		}
		politicalBodyAdminDto.setMobile1(mobile1);
		politicalBodyAdminDto.setMobile2(mobile2);
		if(party!= null){
			politicalBodyAdminDto.setPartyId(party.getId());	
		}
		if(person != null){
			politicalBodyAdminDto.setPersonId(person.getId());	
		}
		if(politicalBodyType != null){
			politicalBodyAdminDto.setPoliticalBodyTypeId(politicalBodyType.getId());	
		}
		return politicalBodyAdminDto;
	}
	
	protected void assertEqualPoliticalBodyAdmin(PoliticalBodyAdminDto expectedPoliticalBodyAdmin, PoliticalBodyAdminDto actualPoliticalBodyAdmin, boolean checkId){
		if(checkId){
			assertEquals(expectedPoliticalBodyAdmin.getId(), actualPoliticalBodyAdmin.getId());	
		}
		assertEquals(expectedPoliticalBodyAdmin.getEmail(), actualPoliticalBodyAdmin.getEmail());
		assertEquals(expectedPoliticalBodyAdmin.getEndDate(), actualPoliticalBodyAdmin.getEndDate());
		assertEqualAddresses(expectedPoliticalBodyAdmin.getHomeAddressDto(), actualPoliticalBodyAdmin.getHomeAddressDto(), checkId);
		assertEqualAddresses(expectedPoliticalBodyAdmin.getOfficeAddressDto(), actualPoliticalBodyAdmin.getOfficeAddressDto(), checkId);
		assertEquals(expectedPoliticalBodyAdmin.getLandLine1(), actualPoliticalBodyAdmin.getLandLine1());
		assertEquals(expectedPoliticalBodyAdmin.getLandLine2(), actualPoliticalBodyAdmin.getLandLine2());
		assertEquals(expectedPoliticalBodyAdmin.getLocationId(), actualPoliticalBodyAdmin.getLocationId());
		assertEquals(expectedPoliticalBodyAdmin.getMobile1(), actualPoliticalBodyAdmin.getMobile1());
		assertEquals(expectedPoliticalBodyAdmin.getMobile2(), actualPoliticalBodyAdmin.getMobile2());
		assertEquals(expectedPoliticalBodyAdmin.getPartyId(), actualPoliticalBodyAdmin.getPartyId());
		assertEquals(expectedPoliticalBodyAdmin.getPersonId(), actualPoliticalBodyAdmin.getPersonId());
		assertEquals(expectedPoliticalBodyAdmin.getPoliticalBodyTypeId(), actualPoliticalBodyAdmin.getPoliticalBodyTypeId());
		assertEquals(expectedPoliticalBodyAdmin.getStartDate(), actualPoliticalBodyAdmin.getStartDate());
		
	}
	
	protected ExecutiveBodyAdminDto createExecutiveBodyAdmin(ExecutiveBodyDto executiveBody, ExecutiveBodyAdminDto manager, 
			PersonDto person,  ExecutivePostDto executivePost, Date startDate, Date endDate){
		ExecutiveBodyAdminDto executiveBodyAdminDto = new ExecutiveBodyAdminDto();
		if(executiveBody != null){
			executiveBodyAdminDto.setExecutiveBodyId(executiveBody.getId());	
		}
		if(manager != null){
			executiveBodyAdminDto.setManagerId(manager.getId());	
		}
		if(person != null){
			executiveBodyAdminDto.setPersonId(person.getId());	
		}
		if(executivePost != null){
			executiveBodyAdminDto.setPostId(executivePost.getId());	
		}
		executiveBodyAdminDto.setStartDate(startDate);
		executiveBodyAdminDto.setEndDate(endDate);
		return executiveBodyAdminDto;
	}
	
	protected ExecutiveBodyAdminDto createAndSaveExecutiveBodyAdmin(AppService appService, ExecutiveBodyDto executiveBody, ExecutiveBodyAdminDto manager, 
			PersonDto person,  ExecutivePostDto executivePost, Date startDate, Date endDate) throws ApplicationException{
		ExecutiveBodyAdminDto executiveBodyAdmin = createExecutiveBodyAdmin(executiveBody, manager, person, executivePost, startDate, endDate);
		return appService.saveExecutiveBodyAdmin(executiveBodyAdmin);
	}
	
	protected void assertEqualExecutiveBodyAdmin(ExecutiveBodyAdminDto expectedExecutiveBodyAdmin, ExecutiveBodyAdminDto actualExecutiveBodyAdmin, boolean checkId){
		if(checkId){
			assertEquals(expectedExecutiveBodyAdmin.getId(), actualExecutiveBodyAdmin.getId());	
		}
		assertEquals(expectedExecutiveBodyAdmin.getEndDate(), actualExecutiveBodyAdmin.getEndDate());
		assertEquals(expectedExecutiveBodyAdmin.getExecutiveBodyId(), actualExecutiveBodyAdmin.getExecutiveBodyId());
		assertEquals(expectedExecutiveBodyAdmin.getManagerId(), actualExecutiveBodyAdmin.getManagerId());
		assertEquals(expectedExecutiveBodyAdmin.getPersonId(), actualExecutiveBodyAdmin.getPersonId());
		assertEquals(expectedExecutiveBodyAdmin.getPostId(), actualExecutiveBodyAdmin.getPostId());
		assertEquals(expectedExecutiveBodyAdmin.getPersonId(), actualExecutiveBodyAdmin.getPersonId());
		assertEquals(expectedExecutiveBodyAdmin.getStartDate(), actualExecutiveBodyAdmin.getStartDate());
		
	}
	
	protected ExecutivePostDto createExecutivePost(DepartmentDto departmentDto, String shortTitle, String title, String description){
		ExecutivePostDto executivePost = new ExecutivePostDto();
		if(departmentDto != null){
			executivePost.setDepartmentId(departmentDto.getId());	
		}
		executivePost.setDescription(description);
		executivePost.setShortTitle(shortTitle);
		executivePost.setTitle(title);
		return executivePost;
	}
	
	protected ExecutivePostDto createAndSaveExecutivePost(AppService appService, DepartmentDto departmentDto, String shortTitle, 
			String title, String description) throws ApplicationException{
		ExecutivePostDto executivePost = createExecutivePost(departmentDto, shortTitle, title, description);
		return appService.saveExecutivePost(executivePost);
	}
	protected ExecutivePostDto createRandomExecutivePost(DepartmentDto departmentDto){
		String shortTitle = randomAlphaString(3);
		String title = randomAlphaString(16);
		String description = randomAlphaString(256);
		ExecutivePostDto executivePost = createExecutivePost(departmentDto, shortTitle, title, description);
		return executivePost;
	}
	
	protected ExecutivePostDto createAndSaveRandomExecutivePost(AppService appService, DepartmentDto departmentDto) throws ApplicationException{
		ExecutivePostDto executivePost = createRandomExecutivePost(departmentDto);
		return appService.saveExecutivePost(executivePost);
	}
	protected void assertEqualExecutivePost(ExecutivePostDto expectedExecutivePost, ExecutivePostDto actualExecutivePost, boolean checkId){
		if(checkId){
			assertEquals(expectedExecutivePost.getId(), actualExecutivePost.getId());	
		}
		assertEquals(expectedExecutivePost.getDescription(), actualExecutivePost.getDescription());
		assertEquals(expectedExecutivePost.getDepartmentId(), actualExecutivePost.getDepartmentId());
		assertEquals(expectedExecutivePost.getShortTitle(), actualExecutivePost.getShortTitle());
		assertEquals(expectedExecutivePost.getTitle(), actualExecutivePost.getTitle());
	}
	
	protected DepartmentDto createDepartment(String categoryName, String description, Long categoryId){
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setName(categoryName);
		departmentDto.setDescription(description);
		return departmentDto;
	}
	protected DepartmentDto createDepartment(String categoryName, String description, CategoryDto category){
		Long categoryId = null;
		if(category != null){
			categoryId = category.getId();
		}
		return createDepartment(categoryName, description, categoryId);
	}
	protected DepartmentDto createRandomDepartment(Long categoryId){
		String categoryName = randomAlphaString(16);
		String description = randomAlphaString(16);
		return createDepartment(categoryName, description, categoryId);
	}
	protected DepartmentDto createAndSaveRandomDepartment(AppService appService, Long categoryId) throws ApplicationException{
		DepartmentDto departmentDto = createRandomDepartment(categoryId);
		return appService.saveDepartment(departmentDto);
	}
	protected void assertEqualDepartment(DepartmentDto expectedDepartment, DepartmentDto actualDepartment, boolean checkId){
		if(checkId){
			assertEquals(expectedDepartment.getId(), actualDepartment.getId());	
		}
		assertEquals(expectedDepartment.getDescription(), actualDepartment.getDescription());
		assertEquals(expectedDepartment.getName(), actualDepartment.getName());
	}

}
