package com.next.eswaraj.admin.service;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.DepartmentAdmin;
import com.eswaraj.domain.nodes.Election;
import com.eswaraj.domain.nodes.ElectionManifesto;
import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.nodes.ElectionType;
import com.eswaraj.domain.nodes.EswarajAccount;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.LeaderTempFacebookAccount;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.SystemCategory;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminExtended;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;

public interface AdminService {

    LocationType saveLocationType(LocationType locationType) throws ApplicationException;

    LocationType getRootLocationType() throws ApplicationException;
    
    LocationType getLocationTypeById(Long locationTypeId) throws ApplicationException;

    Location getLocationById(Long locationId) throws ApplicationException;

    List<LocationType> getChildLocationsTypeOfParent(Long parentLocationTypeId) throws ApplicationException;

    List<LocationType> getAllLocationTypes() throws ApplicationException;

    Location getRootLocationForSwarajIndia() throws ApplicationException;

    List<Location> getChildLocationsOfParent(Long parentLocationId) throws ApplicationException;

    List<Location> getAllLocations() throws ApplicationException;

    List<LocationBoundaryFile> getLocationBoundaryFiles(Long locationId) throws ApplicationException;

    List<Location> findLocationByParentLocationAndLocationType(Long parentLocationId, Long locationTypeId) throws ApplicationException;

    Location saveLocation(Location location) throws ApplicationException;

    List<Category> getAllRootCategories() throws ApplicationException;

    List<Category> getAllCategories() throws ApplicationException;

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

    List<PoliticalBodyAdminSearchResult> searchPoliticalAdmin(String searchQuery) throws ApplicationException;

    List<PoliticalBodyAdminSearchResult> getAllActivePoliticalAdmin() throws ApplicationException;

    List<LocationBoundaryFile> getAllActiveLocationBoundaryFiles() throws ApplicationException;

    List<LocationBoundaryFile> getAllActiveFailedLocationBoundaryFiles() throws ApplicationException;

    PoliticalBodyAdmin savePoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException;

    List<Person> searchPersonByName(String name) throws ApplicationException;

    Person savePerson(Person person) throws ApplicationException;

    EswarajAccount savePersonLoginDetail(Person person, String userName, String password) throws ApplicationException;

    EswarajAccount getPersonEswarajAccount(Person person) throws ApplicationException;

    List<Person> getDepartmentStaffMembers(Department department) throws ApplicationException;

    DepartmentAdmin addDepartmentStaff(Department department, Person person) throws ApplicationException;

    DepartmentAdmin deleteDepartmentStaff(Department department, Person person) throws ApplicationException;

    Person getPersonById(Long personId) throws ApplicationException;
    
    LocationBoundaryFile createNewLocationBoundaryFile(Long locationId, String originalFilename, InputStream inputStream, FileService fileService) throws ApplicationException;

    List<ElectionType> getAllElectionTypes() throws ApplicationException;

    ElectionType saveElectionType(ElectionType electionType) throws ApplicationException;

    List<Election> getAllElections() throws ApplicationException;

    List<Election> getElectionsByPoliticalAdminType(PoliticalBodyType politicalBodyType) throws ApplicationException;

    Election saveElection(Election election) throws ApplicationException;

    Election getElectionById(Long electionId) throws ApplicationException;

    List<ElectionManifesto> getElectionManifestos() throws ApplicationException;

    ElectionManifesto saveElectionManifesto(ElectionManifesto electionManifesto) throws ApplicationException;

    ElectionManifestoPromise saveElectionManifestoPromise(ElectionManifestoPromise electionManifestoPromise) throws ApplicationException;

    List<ElectionManifestoPromise> getElectionManifestoPromisesOfManifesto(Long electionManifestoId) throws ApplicationException;

    SystemCategory saveSystemCategory(SystemCategory systemCategory) throws ApplicationException;

    List<SystemCategory> getAllSystemCategories() throws ApplicationException;

    void reprocessLocationFile(LocationBoundaryFile locationBoundaryFile) throws ApplicationException;
    
    List<TimelineItem> getTimelineItems(int first, int pageSize) throws ApplicationException;

    TimelineItem saveTimelineItem(TimelineItem timelineItem, List<PoliticalBodyAdminSearchResult> politicalBodyAdminSearchResults, Set<Location> locations, List<ElectionManifestoPromise> promises)
            throws ApplicationException;

    TimelineItem saveTimelineItem(TimelineItem timelineItem) throws ApplicationException;

    List<ElectionManifestoPromise> getAllPromisesOfPoliticalAdmin(Long electionManifestoId) throws ApplicationException;

    List<ElectionManifestoPromise> getAllPromisesOfPoliticalAdmin(Collection<Long> electionManifestoIds) throws ApplicationException;

    List<ElectionManifestoPromise> getAllPromises() throws ApplicationException;

    List<LocationSearchResult> getTimelineLocations(TimelineItem timelineItem) throws ApplicationException;

    List<ElectionManifestoPromise> getTimelinePromises(TimelineItem timelineItem) throws ApplicationException;

    List<PoliticalBodyAdminSearchResult> getTimelineAdmins(TimelineItem timelineItem) throws ApplicationException;

    FacebookAccount getFacebookAccountByPerson(Person person) throws ApplicationException;

    LeaderTempFacebookAccount addFacebookAccountEmailForPerson(Person person, String email) throws ApplicationException;

    LeaderTempFacebookAccount getFacebookAccountRequestForPerson(Person person) throws ApplicationException;

    List<FacebookAppPermission> getFacebookAppPermission(FacebookAccount facebookAccount) throws ApplicationException;

    List<Department> getAllRootDepartmentsOfcategory(Category category) throws ApplicationException;

    List<Department> getAllRootDepartments() throws ApplicationException;

    List<Department> getAllRootDepartmentsOfcategory(Long categoryId) throws ApplicationException;

    List<Department> getAllChildDepartments(Department department) throws ApplicationException;

    List<Department> getAllChildDepartments(Long departmentId) throws ApplicationException;

    Department saveDepartment(Department department, List<Location> locations, List<Category> categories) throws ApplicationException;

    List<Location> getAllLocationsOfDepartment(Department department) throws ApplicationException;

    List<Category> getAllCategoriesOfDepartment(Department department) throws ApplicationException;

}
