package com.eswaraj.domain.repo;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;

/**
 * Test for Video repository
 * @author ravi
 * @data May 29, 2014
 */

@ContextConfiguration(locations = { "classpath:eswaraj-domain-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TestAddressRepository extends BaseNeo4jEswarajTest{

    @Autowired
    AddressRepository addressRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    LocationTypeRepository locationTypeRepository;
    @Autowired
    DataClientRepository dataClientRepository;

    /**
     * Given we provide all required attributes of a address and attach only one
     * location
     */
	@Test
    public void shouldSaveAddress() {
        Address address = new Address();
        address.setDateCreated(new Date());
        address.setLattitude(2.3);
        address.setLongitude(3.4);
        address.setLine1(randomAlphaString(16));
        address.setLine2(randomAlphaString(16));
        address.setLine3(randomAlphaString(16));
        address.setPostalCode(randomAlphaNumericString(6));
        
        DataClient dataClient = createDataClient(dataClientRepository, randomAlphaString(16));
        
        LocationType countryLocationType = createLocationType(locationTypeRepository, "Country", null, dataClient, true);

        Location countryLocation = createLocation(locationRepository, randomAlphaNumericString(24), countryLocationType, null);
        Set<Location> locations = new HashSet<>();
        locations.add(countryLocation);
        address.setLocations(locations);
        address = addressRepository.save(address);
        
        Address dbAddress = addressRepository.findOne(address.getId());

        assertAddressEquals(address, dbAddress, true);

        assertNotNull(dbAddress.getId());
	}
	
    /**
     * Given we provide all required attributes of a address and attach only
     * more then one location
     */
    @Test
    public void shouldSaveAddress02() {
        Address address = new Address();
        address.setDateCreated(new Date());
        address.setLattitude(2.3);
        address.setLongitude(3.4);
        address.setLine1(randomAlphaString(16));
        address.setLine2(randomAlphaString(16));
        address.setLine3(randomAlphaString(16));
        address.setPostalCode(randomAlphaNumericString(6));

        DataClient dataClient = createDataClient(dataClientRepository, randomAlphaString(16));

        LocationType countryLocationType = createLocationType(locationTypeRepository, "Country", null, dataClient, true);
        LocationType stateLocationType = createLocationType(locationTypeRepository, "State", countryLocationType, dataClient, false);
        LocationType pcLocationType = createLocationType(locationTypeRepository, "PC", stateLocationType, dataClient, false);

        Location countryLocation = createLocation(locationRepository, randomAlphaNumericString(24), countryLocationType, null);
        Location stateLocation = createLocation(locationRepository, randomAlphaNumericString(24), stateLocationType, countryLocation);
        Location pcLocation = createLocation(locationRepository, randomAlphaNumericString(24), pcLocationType, stateLocation);
        Set<Location> locations = new HashSet<>();
        locations.add(countryLocation);
        locations.add(stateLocation);
        locations.add(pcLocation);
        address.setLocations(locations);
        address = addressRepository.save(address);

        Address dbAddress = addressRepository.findOne(address.getId());

        assertAddressEquals(address, dbAddress, true);
        assertEquals(3, dbAddress.getLocations().size());

        assertNotNull(dbAddress.getId());
    }

}
