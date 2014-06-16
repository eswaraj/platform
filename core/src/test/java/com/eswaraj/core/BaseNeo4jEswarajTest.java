package com.eswaraj.core;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.eswaraj.base.BaseEswarajTest;
import com.eswaraj.base.aspect.TestObjectContextManager;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.AddressDto;
import com.eswaraj.web.dto.CategoryDto;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.PartyDto;
import com.eswaraj.web.dto.PersonDto;
import com.eswaraj.web.dto.PoliticalBodyTypeDto;

public class BaseNeo4jEswarajTest extends BaseEswarajTest {

	@Autowired Neo4jTemplate neo4jTemplate;
	@Autowired(required=false) TestObjectContextManager testObjectContextManager;

	@Autowired
	private GraphDatabaseService graphDatabaseService;
	
	@Before
	public void init(){
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
	
	protected PoliticalBodyTypeDto createPoliticalBodyType(String shortName, String name, String description, Long locationTypeId){
		PoliticalBodyTypeDto politicalBodyTypeDto = new PoliticalBodyTypeDto();
		politicalBodyTypeDto.setName(name);
		politicalBodyTypeDto.setShortName(shortName);
		politicalBodyTypeDto.setDescription(description);
		politicalBodyTypeDto.setLocationTypeId(locationTypeId);
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
	protected void assertEqualParties(PartyDto expectedParty, PartyDto actualParty, boolean checkId){
		if(checkId){
			assertEquals(expectedParty.getId(), actualParty.getId());	
		}
		assertEquals(expectedParty.getName(), actualParty.getName());
	}
	
	protected PersonDto createPerson(String name, String email, String biodata, Date dob, String gender, String landlineNumber1, 
			String landlineNumber2,String mobileNumber1, String mobileNumber2, AddressDto personAddress){
		PersonDto personDto = new PersonDto();
		personDto.setName(name);
		personDto.setEmail(email);
		personDto.setBiodata(biodata);
		personDto.setDob(dob);
		personDto.setGender(gender);
		personDto.setLandlineNumber1(landlineNumber1);
		personDto.setLandlineNumber2(landlineNumber2);
		personDto.setMobileNumber1(mobileNumber1);
		personDto.setMobileNumber2(mobileNumber2);
		personDto.setPersonAddress(personAddress);
		return personDto;
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
	

}
