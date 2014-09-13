package com.eswaraj.domain.repo;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;

import com.eswaraj.base.BaseEswarajTest;
import com.eswaraj.base.aspect.TestObjectContextManager;
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.Device.DeviceType;
import com.eswaraj.domain.nodes.relationships.UserDevice;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.User;

public class BaseNeo4jEswarajTest extends BaseEswarajTest {

	@Autowired Neo4jTemplate neo4jTemplate;
	@Autowired(required=false) TestObjectContextManager testObjectContextManager;

	@Before
	public void init(){
	}

	/**
	 * If you dont want your test to delete newly created DB objects to be deleted, then call this method from your test
	 */
	protected void dontDeleteDbObjects(){
		testObjectContextManager.setDontDeleteForThisTest(true);
	}

	@After
	public void destroyTest(){
		if(testObjectContextManager != null){
			testObjectContextManager.clearAllObjectsCreatdDuringTest();
		}
	}
	
	protected Location createLocation(LocationRepository locationRepository, String name, LocationType locationType, Location parentLocation){
		Location location = new Location();
		location.setName(name);
		location.setLocationType(locationType);
		location.setParentLocation(parentLocation);
		location = locationRepository.save(location);
		return location;
	}
	
	protected PoliticalBodyType createPoliticalBodyType(PoliticalBodyTypeRepository politicalBodyTypeRepository, String name, String shortName, String description, LocationType locationType){
		PoliticalBodyType politicalBodyType = new PoliticalBodyType();
		politicalBodyType.setDescription(description);
		politicalBodyType.setLocationType(locationType);
		politicalBodyType.setName(name);
		politicalBodyType.setShortName(shortName);
		politicalBodyType = politicalBodyTypeRepository.save(politicalBodyType);
		return politicalBodyType;
	}
	
	protected PoliticalBodyAdmin createPoliticalBodyAdmin(PoliticalBodyAdminRepository politicalBodyAdminRepository, boolean active, String email, 
			Date startDate, Date endDate, Address homeAddress, Address officeAddress, String landLine1, String landLine2,Location location,
			String mobile1, String mobile2, Party party, Person person, PoliticalBodyType politicalBodyType){
		PoliticalBodyAdmin politicalBodyAdmin = new PoliticalBodyAdmin();
		politicalBodyAdmin.setActive(active);
		politicalBodyAdmin.setEmail(email);
		politicalBodyAdmin.setStartDate(startDate);
		politicalBodyAdmin.setEndDate(endDate);
		politicalBodyAdmin.setHomeAddress(homeAddress);
		politicalBodyAdmin.setOfficeAddress(officeAddress);
		politicalBodyAdmin.setLandLine1(landLine1);
		politicalBodyAdmin.setLandLine2(landLine2);
		politicalBodyAdmin.setLocation(location);
		politicalBodyAdmin.setMobile1(mobile1);
		politicalBodyAdmin.setMobile2(mobile2);
		politicalBodyAdmin.setParty(party);
		politicalBodyAdmin.setPerson(person);
		politicalBodyAdmin.setPoliticalBodyType(politicalBodyType);
		politicalBodyAdmin = politicalBodyAdminRepository.save(politicalBodyAdmin);
		return politicalBodyAdmin;
	}
	
	protected LocationType createLocationType(LocationTypeRepository locationTypeRepository, String name, LocationType parentLocationType, DataClient dataClient, boolean root){
		LocationType locationType = new LocationType();
		locationType.setName(name);
		locationType.setParentLocationType(parentLocationType);
		locationType.setDataClient(dataClient);
		locationType.setRoot(root);
		locationType = locationTypeRepository.save(locationType);
		return locationType;
	}
	protected DataClient createDataClient(DataClientRepository dataClientRepository, String name){
		DataClient dataClient = new DataClient();
		dataClient.setName(name);
		dataClient = dataClientRepository.save(dataClient);
		return dataClient;
	}
	
	protected Party createParty(PartyRepository partyRepository, String name, String shortName){
		Party party = new Party();
		party.setName(name);
		party.setShortName(shortName);
		party = partyRepository.save(party);
		return party;
	}
	
	protected Person createPerson(PersonRepository personRepository, String name){
		Person person = new Person();
		person.setName(name);
		person = personRepository.save(person);
		return person;
	}

    protected User createUser(UserRepository userRepository, PersonRepository personRepository, String name) {
        Person person = createPerson(personRepository, name);

        User user = new User();
        user.setPerson(person);
        user.setExternalId(UUID.randomUUID().toString());
        user = userRepository.save(user);
        return user;
    }


    protected User createUserRandom(UserRepository userRepository, PersonRepository personRepository) {
        return createUser(userRepository, personRepository, randomAlphaString(10));
    }

    protected Device createDeviceRandom(DeviceRepository deviceRepository, DeviceType deviceType) {
        return createDevice(deviceRepository, randomAlphaNumericString(32), deviceType);
    }

    protected Device createDevice(DeviceRepository deviceRepository, String deviceId, DeviceType deviceType) {
        Device device = new Device();
        device.setDeviceId(deviceId);
        device.setDeviceType(deviceType);
        device = deviceRepository.save(device);
        return device;
    }

    protected UserDevice createUserDevice(UserDeviceRepository userDeviceRepository, User user, Device device) {
        UserDevice userDevice = new UserDevice();
        userDevice.setDevice(device);
        userDevice.setUser(user);
        userDevice = userDeviceRepository.save(userDevice);
        return userDevice;
    }


	protected void assertLocationTypeEquals(LocationType expected, LocationType actual, boolean compareId){
		if(compareId){
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getName(), actual.getName());
	}
	protected void assertDataClientEquals(DataClient expected, DataClient actual, boolean compareId){
		if(compareId){
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getName(), actual.getName());
	}

	protected void assertLocationEquals(Location expected, Location actual, boolean compareId){
		if(compareId){
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getName(), actual.getName());
	}
	protected void assertLocationEquals(Party expected, Party actual, boolean compareId){
		if(compareId){
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getShortName(), actual.getShortName());
	}
	protected void assertAddressEquals(Address expected, Address actual, boolean compareId){
		if(compareId){
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getLine1(), actual.getLine1());
		assertEquals(expected.getLine2(), actual.getLine2());
		assertEquals(expected.getLine3(), actual.getLine3());
	}
	
	protected void assertPoliticalBodyAdminEquals(PoliticalBodyAdmin expected, PoliticalBodyAdmin actual, boolean compareId){
		if(compareId){
			assertEquals(expected.getId(), actual.getId());
		}
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getEndDate(), actual.getEndDate());
		assertEquals(expected.getLandLine1(), actual.getLandLine1());
		assertEquals(expected.getLandLine2(), actual.getLandLine2());
		assertEquals(expected.getMobile1(), actual.getMobile1());
		assertEquals(expected.getLocation().getId(), actual.getLocation().getId());
		assertEquals(expected.getMobile1(), actual.getMobile1());
		assertEquals(expected.getMobile2(), actual.getMobile2());
		assertEquals(expected.getStartDate(), actual.getStartDate());
		assertEquals(expected.getParty().getId(), actual.getParty().getId());
		assertEquals(expected.getPerson().getId(), actual.getPerson().getId());
		assertEquals(expected.getPoliticalBodyType().getId(), actual.getPoliticalBodyType().getId());
		
		assertEquals(expected, actual);
		assertEquals(expected, actual);
		assertEquals(expected, actual);
		
	}
}
