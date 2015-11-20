package com.eswaraj.domain.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;
import com.eswaraj.domain.nodes.PoliticalBodyType;

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestPoliticalAdminStaffRepository extends BaseNeo4jEswarajTest{

	@Autowired LocationRepository locationRepository;
	@Autowired LocationTypeRepository locationTypeRepository;
	@Autowired PoliticalBodyTypeRepository politicalBodyTypeRepository;
	@Autowired PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    PoliticalBodyAdminStaffRepository politicalBodyAdminStaffRepository;
	@Autowired DataClientRepository dataClientRepository;
	@Autowired PartyRepository partyRepository;
	@Autowired PersonRepository personRepository;
	@Test
    public void test01_getAllPoliticalAdminStaffByPoliticalAdmin() {
		final String countryName = "Country";
		final boolean isRoot = true;
		DataClient dataClient = createDataClient(dataClientRepository, randomAlphaString(16));
		
		Person person = createPerson(personRepository, randomAlphaString(16));
		Party party = createParty(partyRepository, randomAlphaString(16), randomAlphaString(3));
		LocationType countryLocationType = createLocationType(locationTypeRepository, countryName, null, dataClient, isRoot);
		LocationType stateLocationType = createLocationType(locationTypeRepository, countryName, countryLocationType, dataClient, false);
		Location country = createLocation(locationRepository, randomAlphaString(16), countryLocationType, null);
		Location state = createLocation(locationRepository, randomAlphaString(16), stateLocationType, country);
		PoliticalBodyType politicalBodyTypeAtCountry = createPoliticalBodyType(politicalBodyTypeRepository, randomAlphaString(16), randomAlphaString(3), 
				randomAlphaString(128), countryLocationType);
		PoliticalBodyType politicalBodyTypeAtState = createPoliticalBodyType(politicalBodyTypeRepository, randomAlphaString(16), randomAlphaString(3), 
				randomAlphaString(128), stateLocationType);
		
		Date startDateForCountryAdmin = randomDateInPast();
		Date endDateForCountryAdmin = randomDateInFuture();
		
		PoliticalBodyAdmin politicalBodyAdminAtCountry = createPoliticalBodyAdmin(politicalBodyAdminRepository, true, randomEmailAddress(), 
 startDateForCountryAdmin, endDateForCountryAdmin, null,
                null, null, null, country, null, null, party, person, politicalBodyTypeAtCountry, "/leader1");
		PoliticalBodyAdmin politicalBodyAdminAtState = createPoliticalBodyAdmin(politicalBodyAdminRepository, true, randomEmailAddress(), 
 startDateForCountryAdmin, endDateForCountryAdmin, null, null,
                null, null, state, null, null, party, person, politicalBodyTypeAtState, "/leader2");

        PoliticalBodyAdminStaff politicalBodyAdminStaff = new PoliticalBodyAdminStaff();
        Person staffPerson = createPerson(personRepository, randomAlphaString(16));
        politicalBodyAdminStaff.setPerson(staffPerson);
        politicalBodyAdminStaff.setPost("PA");
        politicalBodyAdminStaff.setPoliticalBodyAdmin(politicalBodyAdminAtCountry);
        politicalBodyAdminStaff = politicalBodyAdminStaffRepository.save(politicalBodyAdminStaff);

		//Now search country admin and state admin seprately
        Collection<PoliticalBodyAdminStaff> dbPoliticalBodyAdminAtCountry = politicalBodyAdminStaffRepository.getAllPoliticalAdminStaffByPoliticalBodyAdmin(politicalBodyAdminAtCountry);
		assertNotNull(dbPoliticalBodyAdminAtCountry);
		assertEquals(1, dbPoliticalBodyAdminAtCountry.size());
        assertPoliticalBodyAdminStaffEquals(politicalBodyAdminStaff, dbPoliticalBodyAdminAtCountry.iterator().next(), true);
		
	}
	
	
}
