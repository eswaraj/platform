package com.next.eswaraj.admin.service;

import java.io.InputStream;
import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Election;
import com.eswaraj.domain.nodes.ElectionType;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminExtended;

public interface AdminService {

    LocationType saveLocationType(LocationType locationType) throws ApplicationException;

    LocationType getRootLocationType() throws ApplicationException;
    
    LocationType getLocationTypeById(Long locationTypeId) throws ApplicationException;

    Location getLocationById(Long locationId) throws ApplicationException;

    List<LocationType> getChildLocationsTypeOfParent(Long parentLocationTypeId) throws ApplicationException;

    List<LocationType> getAllLocationTypes() throws ApplicationException;

    Location getRootLocationForSwarajIndia() throws ApplicationException;

    List<Location> getChildLocationsOfParent(Long parentLocationId) throws ApplicationException;

    List<LocationBoundaryFile> getLocationBoundaryFiles(Long locationId) throws ApplicationException;

    List<Location> findLocationByParentLocationAndLocationType(Long parentLocationId, Long locationTypeId) throws ApplicationException;

    Location saveLocation(Location location) throws ApplicationException;

    List<Category> getAllRootCategories() throws ApplicationException;

    List<Category> getChildCategories(Long parentCategoryId) throws ApplicationException;

    Category saveCategory(Category category) throws ApplicationException;

    List<PoliticalBodyType> getAllPoliticalBodyTypes() throws ApplicationException;

    List<PoliticalBodyType> getAllPoliticalBodyTypeOfLocation(Long locationId) throws ApplicationException;

    PoliticalBodyType savePoliticalBodyType(PoliticalBodyType politicalBodyType) throws ApplicationException;

    PoliticalBodyType getPoliticalBodyTypeById(Long politicalBodyTypeId) throws ApplicationException;

    List<Party> getAllParties() throws ApplicationException;

    Party saveParty(Party party) throws ApplicationException;

    Party getPartyById(Long partyId) throws ApplicationException;

    List<LocationSearchResult> searchLocationByName(String name) throws ApplicationException;

    List<PoliticalBodyAdminExtended> getPoliticalAdminOfLocationAndAdminType(Long locationid, Long politicalBodyAdminTypeId) throws ApplicationException;

    PoliticalBodyAdmin savePoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException;

    List<Person> searchPersonByName(String name) throws ApplicationException;

    Person savePerson(Person person) throws ApplicationException;

    Person getPersonById(Long personId) throws ApplicationException;
    
    LocationBoundaryFile createNewLocationBoundaryFile(Long locationId, String originalFilename, InputStream inputStream, FileService fileService) throws ApplicationException;

    List<ElectionType> getAllElectionTypes() throws ApplicationException;

    ElectionType saveElectionType(ElectionType electionType) throws ApplicationException;

    List<Election> getAllElections() throws ApplicationException;

    Election saveElection(Election election) throws ApplicationException;

    Election getElectionById(Long electionId) throws ApplicationException;

}
