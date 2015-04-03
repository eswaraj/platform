package com.next.eswaraj.admin.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.neo4j.cypher.MissingIndexException;
import org.neo4j.rest.graphdb.RestResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.util.DateTimeUtil;
import com.eswaraj.core.util.DateUtil;
import com.eswaraj.core.util.PasswordUtil;
import com.eswaraj.domain.base.BaseNode;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.DataClient;
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
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminExtended;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.domain.nodes.relationships.DepartmentCategory;
import com.eswaraj.domain.nodes.relationships.DepartmentLocation;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;
import com.eswaraj.domain.nodes.relationships.LocationTimelineItem;
import com.eswaraj.domain.nodes.relationships.PoliticalAdminTimelineItem;
import com.eswaraj.domain.nodes.relationships.PromiseTimelineItem;
import com.eswaraj.domain.repo.AddressRepository;
import com.eswaraj.domain.repo.CategoryRepository;
import com.eswaraj.domain.repo.DataClientRepository;
import com.eswaraj.domain.repo.DepartmentAdminRepository;
import com.eswaraj.domain.repo.DepartmentCategoryRepository;
import com.eswaraj.domain.repo.DepartmentLocationRepository;
import com.eswaraj.domain.repo.DepartmentRepository;
import com.eswaraj.domain.repo.ElectionManifestoPromiseRepository;
import com.eswaraj.domain.repo.ElectionManifestoRepository;
import com.eswaraj.domain.repo.ElectionRepository;
import com.eswaraj.domain.repo.ElectionTypeRepository;
import com.eswaraj.domain.repo.EswarajAccountRepository;
import com.eswaraj.domain.repo.FacebookAccountRepository;
import com.eswaraj.domain.repo.FacebookAppPermissionRepository;
import com.eswaraj.domain.repo.LeaderTempFacebookAccountRepository;
import com.eswaraj.domain.repo.LocationBoundaryFileRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTimelineItemRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PartyRepository;
import com.eswaraj.domain.repo.PersonRepository;
import com.eswaraj.domain.repo.PoliticalAdminTimelineItemRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.domain.repo.PoliticalBodyTypeRepository;
import com.eswaraj.domain.repo.PromiseTimelineItemRepository;
import com.eswaraj.domain.repo.SystemCategoryRepository;
import com.eswaraj.domain.repo.TimelineItemRepository;
import com.eswaraj.domain.repo.UserRepository;
import com.eswaraj.domain.validator.exception.ValidationException;
import com.eswaraj.queue.service.QueueService;

@Service
public class AdminServiceImpl implements AdminService {

    private String indiaEswarajClientName = "Eswaraj-India";
    private String indiaEswarajRootLocationTypeName = "Country";

    @Autowired
    private LocationTypeRepository locationTypeRepository;
    
    @Autowired
    private DataClientRepository dataClientRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private DepartmentLocationRepository departmentLocationRepository;

    @Autowired
    private DepartmentCategoryRepository departmentCategoryRepository;

    @Autowired
    private EswarajAccountRepository eswarajAccountRepository;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    @Value("${aws_s3_directory_for_location_files:locations}")
    private String awsDirectoryForLocationFiles;

    @Value("${leader_domain_and_context}")
    private String leaderServerBaseUrl;

    @Autowired
    private LocationBoundaryFileRepository locationBoundaryFileRepository;

    @Autowired
    private QueueService queueService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;

    @Autowired
    private PoliticalBodyTypeRepository politicalBodyTypeRepository;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ElectionTypeRepository electionTypeRepository;

    @Autowired
    private ElectionRepository electionRepository;

    @Autowired
    private ElectionManifestoRepository electionManifestoRepository;

    @Autowired
    private ElectionManifestoPromiseRepository electionManifestoPromiseRepository;

    @Autowired
    private SystemCategoryRepository systemCategoryRepository;

    @Autowired
    private TimelineItemRepository timelineItemRepository;

    @Autowired
    private LocationTimelineItemRepository locationTimelineItemRepository;

    @Autowired
    private PoliticalAdminTimelineItemRepository politicalAdminTimelineItemRepository;

    @Autowired
    private FacebookAccountRepository facebookAccountRepository;

    @Autowired
    private PromiseTimelineItemRepository promiseTimelineItemRepository;

    @Autowired
    private FacebookAppPermissionRepository facebookAppPermissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentAdminRepository departmentAdminRepository;

    @Autowired
    private LeaderTempFacebookAccountRepository leaderTempFacebookAccountRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public LocationType saveLocationType(LocationType locationType) throws ApplicationException {
        DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
        locationType.setDataClient(dataClient);
        if (locationType.getId() == null) {
            // means we are trying to create new location type
            checkIfRootLocationAlreadyExists(locationType, dataClient);
        }
        String urlIdentifier = getLocationTypeUrlIdentifier(locationType);
        locationType.setUrlIdentifier(urlIdentifier);
        locationType = locationTypeRepository.save(locationType);
        return locationType;
    }


    @Override
    public LocationType getRootLocationType() throws ApplicationException {
        DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
        return getOrCreateRootLocationTypeIndiaEswaraj(dataClient);
    }

    @Override
    public LocationType getLocationTypeById(Long locationTypeId) throws ApplicationException {
        return locationTypeRepository.findOne(locationTypeId);
    }

    @Override
    public List<LocationType> getChildLocationsTypeOfParent(Long parentLocationTypeId) throws ApplicationException {
        List<LocationType> locationTypes = locationTypeRepository.findLocationTypeByParentLocation(parentLocationTypeId);
        return locationTypes;
    }

    private DataClient getOrCreateDataClientIndiaEswaraj() {
        DataClient dataClient = null;
        try {
            dataClient = dataClientRepository.getDataClientByName(indiaEswarajClientName);

        } catch (MissingIndexException | InvalidDataAccessResourceUsageException | RestResultException mie) {
            // for first time this exception gets thrown as we havent created a
            // single node of type DataClient.
            // just catch it and ignore it
            dataClient = null;
        }
        if (dataClient == null) {
            dataClient = new DataClient();
            dataClient.setName(indiaEswarajClientName);
            dataClient = dataClientRepository.save(dataClient);
        }
        return dataClient;
    }

    private void checkIfRootLocationAlreadyExists(LocationType locationType, DataClient dataClient) throws ApplicationException {
        if (locationType.getParentLocationType() == null) {
            // now check if one ROOT Location already exists for this Data
            // client
            // If yes then throw exception as we can not have more then one Root
            // Location(usually country) for a Data client
            LocationType existingRootLocationType = locationTypeRepository.getRootLocationTypeByDataClient(dataClient);
            if (existingRootLocationType != null) {
                throw new ApplicationException("One root location type already exists[" + existingRootLocationType.getName() + "," + existingRootLocationType.getId()
                        + "], you can not create more then one root location Type. If you are trying to create child location type then make sure you set parentLocationType");
            }
        }
    }

    private LocationType getOrCreateRootLocationTypeIndiaEswaraj(DataClient dataClient) {
        LocationType locationType = locationTypeRepository.getRootLocationTypeByDataClient(dataClient);
        if (locationType == null) {
            locationType = new LocationType();
            locationType.setName(indiaEswarajRootLocationTypeName);
            locationType.setDataClient(dataClient);
            locationType.setRoot(true);
            locationType = locationTypeRepository.save(locationType);
        }
        return locationType;
    }

    @Override
    public Location getRootLocationForSwarajIndia() throws ApplicationException {
        DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
        LocationType locationType = getOrCreateRootLocationTypeIndiaEswaraj(dataClient);
        Location location = locationRepository.getRootLocationByLocationType(locationType.getId());
        if (location == null) {
            // create default location India
            location = new Location();
            location.setLocationType(locationType);
            location.setName("India");
            location = locationRepository.save(location);
        }
        return location;
    }

    @Override
    public List<Location> getChildLocationsOfParent(Long parentLocationId) throws ApplicationException {
        return locationRepository.findLocationByParentLocation(parentLocationId);
    }

    @Override
    public List<LocationBoundaryFile> getLocationBoundaryFiles(Long locationId) throws ApplicationException {
        return locationBoundaryFileRepository.getAllLocationBoundaryFile(locationId);
    }

    @Override
    public List<Location> findLocationByParentLocationAndLocationType(Long parentLocationId, Long locationTypeId) throws ApplicationException {
        System.out.println("findLocationByParentLocationAndLocationType (" + parentLocationId + "," + locationTypeId + ")");
        return locationRepository.findLocationByParentLocationAndLocationType(parentLocationId, locationTypeId);
    }

    @Override
    public Location saveLocation(Location location) throws ApplicationException {
        location.setUrlIdentifier(getLocationUrlIdentifier(location));
        checkParentChildRule(location);
        location = locationRepository.save(location); 
        queueService.sendLocationUpdateMessage(location.getId());
        return location;
    }

    private void checkParentChildRule(Location location) throws ApplicationException {
        // LocationType parentLocationType = locationTypeRepository.findOne(location.getParentLocation().getLocationType().getId());
        if (location.getParentLocation() == null) {
            if (location.getLocationType().getParentLocationType() != null) {
                throw new ApplicationException("Can not create a Location of type [" + location.getLocationType().getName() + "], without a parent Location");
            }
        } else {
            Location parentLocation = locationRepository.findOne(location.getParentLocation().getId());
            logger.info("Location : {}", location);
            logger.info("parentLocation : {}", parentLocation);
            LocationType locationType = locationTypeRepository.findOne(location.getLocationType().getId());
            logger.info("locationType : {}", locationType);
            if (!locationType.getParentLocationType().getId().equals(parentLocation.getLocationType().getId())) {
                LocationType parentLocationType = locationTypeRepository.findOne(parentLocation.getLocationType().getId());
                throw new ApplicationException("Can not create a Location of type [" + location.getLocationType().getName() + "], under location type [" + parentLocationType.getName() + "]");
            }
        }
    }

    @Override
    public Location getLocationById(Long locationId) throws ApplicationException {
        return locationRepository.findOne(locationId);
    }

    private String getLocationUrlIdentifier(Location oneLocation) {

        if (oneLocation.getParentLocation() == null) {
            String urlIdentifier = removeExtraChars(oneLocation.getName());
            return urlIdentifier;
        }

        Location location = oneLocation;
        LocationType locationType;
        String locationTypeUrlId;
        String locationTypeNameUrl;
        String urlIdentifier = "";
        while (location != null) {
            locationType = locationTypeRepository.findOne(location.getLocationType().getId());
            locationTypeUrlId = getLocationTypeUrlIdentifier(locationType);
            if (!locationTypeUrlId.equals("country")) {// No need to add country
                                                       // in url
                locationTypeNameUrl = removeExtraChars(location.getName());
                urlIdentifier = "/" + locationTypeUrlId + "/" + locationTypeNameUrl + urlIdentifier;
            }

            if (location.getParentLocation() == null) {
                break;
            }
            location = locationRepository.findOne(location.getParentLocation().getId());
        }
        return urlIdentifier;
    }

    private String removeExtraChars(String str) {
        String urlIdentifier = str.toLowerCase();
        urlIdentifier = urlIdentifier.replace(' ', '-');
        urlIdentifier = urlIdentifier.replace("&", "");
        return urlIdentifier;
    }

    private String getLocationTypeUrlIdentifier(LocationType oneLocationType) {
        String urlIdentifier = oneLocationType.getName().toLowerCase();
        urlIdentifier = urlIdentifier.replace("&", "");
        if (urlIdentifier.contains(" ")) {
            // names like assembly constituency
            String parts[] = urlIdentifier.split(" ");
            String id = "";
            for (String onePart : parts) {
                if (StringUtils.isEmpty(onePart)) {
                    continue;
                }
                id = id + onePart.charAt(0);
            }
            urlIdentifier = id;
        }
        return urlIdentifier;
    }

    @Override
    public List<Category> getAllRootCategories() throws ApplicationException {
        List<Category> rootCategories = categoryRepository.getAllRootCategories();
        return rootCategories;
    }

    @Override
    public List<Category> getAllCategories() throws ApplicationException {
        List<Category> rootCategories = categoryRepository.getAllCategories();
        return rootCategories;
    }

    @Override
    public List<Category> getChildCategories(Long parentCategoryId) throws ApplicationException {
        List<Category> childCategories = categoryRepository.findAllChildCategoryOfParentCategory(parentCategoryId);
        return childCategories;
    }

    @Override
    public Category saveCategory(Category category) throws ApplicationException {
        String urlIdentifier = category.getName().toLowerCase();
        urlIdentifier = urlIdentifier.replace("&", "");
        urlIdentifier = urlIdentifier.replace(" ", "-");
        category.setUrlIdentifier(urlIdentifier);

        category = categoryRepository.save(category);
        return category;
    }

    @Override
    public List<LocationType> getAllLocationTypes() throws ApplicationException {
        DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
        return locationTypeRepository.getAllLocationTypeOfDataClient(dataClient.getId());
    }

    @Override
    public List<PoliticalBodyType> getAllPoliticalBodyTypes() throws ApplicationException {
        List<PoliticalBodyType> dbPoliticalTypes = politicalBodyTypeRepository.getAllPoliticalBodyTypes();
        return dbPoliticalTypes;
    }

    @Override
    public List<PoliticalBodyType> getAllPoliticalBodyTypeOfLocation(Long locationId) throws ApplicationException {
        return politicalBodyTypeRepository.getAllPoliticalBodyTypesOfLocation(locationId);
    }

    private <T> List<T> convertToList(EndResult<T> dbResult) {
        List<T> returnList = new ArrayList<>();
        for (T oneT : dbResult) {
            returnList.add(oneT);
        }
        return returnList;
    }

    private <T> List<T> convertToList(Page<T> dbResult) {
        List<T> returnList = new ArrayList<>();
        for (T oneT : dbResult) {
            returnList.add(oneT);
        }
        return returnList;
    }

    @Override
    public PoliticalBodyType savePoliticalBodyType(PoliticalBodyType politicalBodyType) throws ApplicationException {
        if (politicalBodyType.getLocationType() == null) {
            throw new ApplicationException("Location Type can not be null");
        }
        return politicalBodyTypeRepository.save(politicalBodyType);
    }

    @Override
    public PoliticalBodyType getPoliticalBodyTypeById(Long politicalBodyTypeId) throws ApplicationException {
        return politicalBodyTypeRepository.findOne(politicalBodyTypeId);
    }


    @Override
    public List<Party> getAllParties() throws ApplicationException {
        List<Party> parties = partyRepository.getAllParties();
        return parties;
    }

    @Override
    public Party saveParty(Party party) throws ApplicationException {
        return partyRepository.save(party);
    }

    @Override
    public Party getPartyById(Long partyId) throws ApplicationException {
        return partyRepository.findOne(partyId);
    }

    @Override
    public List<LocationSearchResult> searchLocationByName(String name) throws ApplicationException {
        System.out.println("Searching for Name : " + "name:*" + name + "*");
        return convertToList(locationRepository.searchLocationByLocationName("name:*" + name + "*"));
    }

    @Override
    public List<PoliticalBodyAdminExtended> getPoliticalAdminOfLocationAndAdminType(Long locationId, Long politicalBodyAdminTypeId) throws ApplicationException {
        System.out.println("locationId : " + locationId + ", politicalBodyAdminTypeId:" + politicalBodyAdminTypeId);
        EndResult<PoliticalBodyAdminExtended> result = politicalBodyAdminRepository.getAllPoliticalAdminByLocationAndPoliticalBodyType(locationId, politicalBodyAdminTypeId);
        return convertToList(result);
    }

    @Override
    public List<Person> searchPersonByName(String name) throws ApplicationException {
        
        if(name.contains(" ")){
            String[] names = name.split(" ");
            StringBuffer sb = new StringBuffer();
            sb.append("(");
            int count = 0;
            for(String oneName : names){
                if(count > 0){
                    sb.append(" AND ");
                }
                sb.append(oneName);
                sb.append("*");
                count++;
            }
            sb.append(")");
            name = sb.toString();
        }else{
            name = "*" + name + "*";
        }
        
        return personRepository.searchPersonByName("name:" + name);
    }

    @Override
    public Person savePerson(Person person) throws ApplicationException {
        return personRepository.save(person);
    }

    @Override
    public PoliticalBodyAdmin savePoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException {
        politicalBodyAdmin.setActive(isActive(politicalBodyAdmin));
        if (politicalBodyAdmin.getLocation().getUrlIdentifier().startsWith("/")) {
            politicalBodyAdmin.setUrlIdentifier("/leader" + politicalBodyAdmin.getLocation().getUrlIdentifier() + "/" + politicalBodyAdmin.getPoliticalBodyType().getShortName().toLowerCase());
        } else {
            politicalBodyAdmin.setUrlIdentifier("/leader/" + politicalBodyAdmin.getLocation().getUrlIdentifier() + "/" + politicalBodyAdmin.getPoliticalBodyType().getShortName().toLowerCase());
        }
        validateWithExistingData(politicalBodyAdmin);
        return politicalBodyAdminRepository.save(politicalBodyAdmin);
    }

    @Override
    public Person getPersonById(Long personId) throws ApplicationException {
        return personRepository.findOne(personId);
    }

    private boolean isActive(PoliticalBodyAdmin politicalBodyAdmin) {
        if (politicalBodyAdmin.getStartDate() == null) {
            throw new ValidationException("Start date can not be null");
        }
        boolean active = false;
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(politicalBodyAdmin.getStartDate());
        startDate = DateUtil.getStartOfDay(startDate);
        Calendar today = Calendar.getInstance();
        if (today.after(startDate) && (politicalBodyAdmin.getEndDate() == null || politicalBodyAdmin.getEndDate().after(today.getTime()))) {
            active = true;
        }
        return active;
    }

    private void validateWithExistingData(PoliticalBodyAdmin politicalBodyAdmin) throws ApplicationException {
        if (politicalBodyAdmin.getLocation() != null && politicalBodyAdmin.getPoliticalBodyType() != null) {
            Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation = politicalBodyAdminRepository.getAllPoliticalAdminByLocationAndPoliticalBodyType(politicalBodyAdmin.getLocation(),
                    politicalBodyAdmin.getPoliticalBodyType());
            adjustActivePoliticalAdminForLocation(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
            checkForDateOverlap(politicalBodyAdmin, allPoliticalBodyAdminsForLocation);
        }
    }

    private void adjustActivePoliticalAdminForLocation(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException {
        if (!politicalBodyAdmin.isActive()) {
            // if this is not active just go back
            return;
        }

        for (PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation) {
            if (!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin.getId())) {
                if (onePoliticalBodyAdmin.isActive()) {
                    // throw new ApplicationException("Another Active Political Admin exists [id="+onePoliticalBodyAdmin.getId()+"], please make him/her inactive first and then make this active");
                    // instead of throwing exception we are just turning other active Admin to inactive
                    onePoliticalBodyAdmin.setActive(false);
                    politicalBodyAdminRepository.save(onePoliticalBodyAdmin);
                }

            }
        }
    }

    private void checkForDateOverlap(PoliticalBodyAdmin politicalBodyAdmin, Collection<PoliticalBodyAdmin> allPoliticalBodyAdminsForLocation) throws ApplicationException {
        for (PoliticalBodyAdmin onePoliticalBodyAdmin : allPoliticalBodyAdminsForLocation) {
            if (!onePoliticalBodyAdmin.getId().equals(politicalBodyAdmin.getId())) {
                // We need to check political admin being saved with other admins only
                if (checkIfDatesAreOverlapped(onePoliticalBodyAdmin.getStartDate(), onePoliticalBodyAdmin.getEndDate(), politicalBodyAdmin.getStartDate(), politicalBodyAdmin.getEndDate())) {
                    throw new ApplicationException("Start date and end dates of two Political admin for this location overallped [id1=" + onePoliticalBodyAdmin.getId() + ", startDate="
                            + onePoliticalBodyAdmin.getStartDate() + ", endDate=" + onePoliticalBodyAdmin.getEndDate() + "] and [id2=" + politicalBodyAdmin.getId() + ", startDat="
                            + politicalBodyAdmin.getStartDate() + ", endDate=" + politicalBodyAdmin.getEndDate());
                }
            }
        }
    }

    private boolean checkIfDatesAreOverlapped(Date startDate1, Date endDate1, Date startDate2, Date endDate2) {
        if (endDate1 == null && endDate2 == null) {
            return true;
        }
        if (endDate2 == null && (startDate1.after(startDate2) || endDate1.after(startDate2))) {
            return true;
        }
        if (endDate1 == null && (startDate2.after(startDate1) || endDate2.after(startDate1))) {
            return true;
        }
        if (endDate1 != null && endDate2 != null && ((startDate1.after(startDate2) && startDate1.before(endDate2))) || (endDate1.after(startDate2) && endDate1.before(endDate2))
                || (endDate2.after(startDate1) && endDate2.before(endDate1)) || (startDate2.after(startDate1) && startDate2.before(endDate1))) {
            return true;
        }

        return false;
    }

    @Override
    public LocationBoundaryFile createNewLocationBoundaryFile(Long locationId, String originalFilename, InputStream inputStream, FileService fileService) throws ApplicationException {
        Location location = locationRepository.findOne(locationId);
        if (location == null) {
            throw new ApplicationException("No such location exists[id=" + locationId + "]");
        }
        System.out.println("createNewLocationBoundaryFile Started");
        LocationBoundaryFile existingLocationBoundayrFile = locationBoundaryFileRepository.getActiveLocationBoundaryFile(location);
        String currenttime = dateTimeUtil.getCurrentTimeYYYYMMDDHHMMSS();
        if (existingLocationBoundayrFile != null) {
            if (existingLocationBoundayrFile.getStatus().equals("Pending") || existingLocationBoundayrFile.getStatus().equals("Processing")) {
                throw new ApplicationException("Another file is being processed you can not upload new file until the previous file is processed");
            }
            existingLocationBoundayrFile.setActive(false);
            existingLocationBoundayrFile = locationBoundaryFileRepository.save(existingLocationBoundayrFile);
        }
        String fileDir = awsDirectoryForLocationFiles + "/" + location.getId();
        String fileName = UUID.randomUUID().toString() + "_" + currenttime + ".kml";
        logger.info("saving file {}", fileName);
        // save file to a storage
        String httpPath = fileService.saveFile(fileDir, fileName, inputStream);
        System.out.println("httpPath Saved = " + httpPath);
        // create LocationBoudaryFile
        LocationBoundaryFile locationBoundaryFile = new LocationBoundaryFile();
        locationBoundaryFile.setLocation(location);
        locationBoundaryFile.setFileNameAndPath(httpPath);
        locationBoundaryFile.setStatus("Pending");
        locationBoundaryFile.setUploadDate(new Date());
        locationBoundaryFile.setActive(true);
        locationBoundaryFile.setOriginalFileName(originalFilename);

        locationBoundaryFile = locationBoundaryFileRepository.save(locationBoundaryFile);
        System.out.println("locationBoundaryFile Saved = " + locationBoundaryFile);
        // Updaye Locaion Object with KMl file
        location.setBoundaryFile(httpPath);
        location = locationRepository.save(location);

        Long existingLocationBoundaryFileId = null;
        if (existingLocationBoundayrFile != null) {
            existingLocationBoundaryFileId = existingLocationBoundayrFile.getId();
        }

        queueService.sendLocationFileUploadMessage(existingLocationBoundaryFileId, locationBoundaryFile.getId(), locationId);

        return locationBoundaryFile;
    }


    @Override
    public List<ElectionType> getAllElectionTypes() throws ApplicationException {
        return convertToList(electionTypeRepository.findAll());
    }

    @Override
    public ElectionType saveElectionType(ElectionType electionType) throws ApplicationException {
        return electionTypeRepository.save(electionType);
    }

    @Override
    public List<Election> getAllElections() throws ApplicationException {
        return convertToList(electionRepository.findAll());
    }

    @Override
    public Election saveElection(Election election) throws ApplicationException {
        return electionRepository.save(election);
    }

    @Override
    public Election getElectionById(Long electionId) throws ApplicationException {
        return electionRepository.findOne(electionId);
    }

    @Override
    public List<Election> getElectionsByPoliticalAdminType(PoliticalBodyType politicalBodyType) throws ApplicationException {
        return electionRepository.findAllElectionsOfPoliticalBodyType(politicalBodyType);
    }

    @Override
    public List<ElectionManifesto> getElectionManifestos() throws ApplicationException {
        return convertToList(electionManifestoRepository.findAll());
    }

    @Override
    public ElectionManifesto saveElectionManifesto(ElectionManifesto electionManifesto) throws ApplicationException {
        return electionManifestoRepository.save(electionManifesto);
    }

    @Override
    public ElectionManifestoPromise saveElectionManifestoPromise(ElectionManifestoPromise electionManifestoPromise) throws ApplicationException {
        return electionManifestoPromiseRepository.save(electionManifestoPromise);
    }

    @Override
    public List<ElectionManifestoPromise> getElectionManifestoPromisesOfManifesto(Long electionManifestoId) throws ApplicationException {
        return electionManifestoPromiseRepository.getAllPromisesOfManifesto(electionManifestoId);
    }

    @Override
    public SystemCategory saveSystemCategory(SystemCategory systemCategory) throws ApplicationException {
        return systemCategoryRepository.save(systemCategory);
    }

    @Override
    public List<SystemCategory> getAllSystemCategories() throws ApplicationException {
        return convertToList(systemCategoryRepository.findAll());
    }

    @Override
    public void reprocessLocationFile(LocationBoundaryFile locationBoundaryFile) throws ApplicationException {
        queueService.sendLocationFileUploadMessage(null, locationBoundaryFile.getId(), locationBoundaryFile.getLocation().getId());

    }

    @Override
    public List<TimelineItem> getTimelineItems(int first, int pageSize) throws ApplicationException {
        return timelineItemRepository.getTimelineItems(first, pageSize);
    }

    @Override
    public List<PoliticalBodyAdminSearchResult> searchPoliticalAdmin(String searchQuery) throws ApplicationException {
        return convertToList(politicalBodyAdminRepository.searchPoliticalAdminByName("name:*" + searchQuery + "*"));
    }

    @Override
    public List<PoliticalBodyAdminSearchResult> getAllActivePoliticalAdmin() throws ApplicationException {
        return convertToList(politicalBodyAdminRepository.getAllCurrentPoliticalAdmins());
    }

    @Override
    public List<ElectionManifestoPromise> getAllPromisesOfPoliticalAdmin(Long electionManifestoId) throws ApplicationException {
        return electionManifestoPromiseRepository.getAllPromisesOfPoliticalAdmin(electionManifestoId);
    }

    @Override
    public List<ElectionManifestoPromise> getAllPromisesOfPoliticalAdmin(Collection<Long> electionManifestoIds) throws ApplicationException {
        return electionManifestoPromiseRepository.getAllPromisesOfPoliticalAdmin(electionManifestoIds);
    }

    @Override
    public List<ElectionManifestoPromise> getAllPromises() throws ApplicationException {
        return convertToList(electionManifestoPromiseRepository.findAll());
    }

    @Override
    public TimelineItem saveTimelineItem(TimelineItem timelineItem, List<PoliticalBodyAdminSearchResult> politicalBodyAdminSearchResults, Set<Location> locations,
            List<ElectionManifestoPromise> promises) throws ApplicationException {
        timelineItem = timelineItemRepository.save(timelineItem);

        // Add Political Admin to Timeline Item
        addTimelineItemToPoliticalBodyAdmins(timelineItem, politicalBodyAdminSearchResults);

        // Add Locations to TimeLine
        addTimelineItemToLocations(timelineItem, locations);

        // Add Promises to Timeline
        addTimelineItemToElectionManifestoPromises(timelineItem, promises);

        return timelineItem;
    }

    private void addTimelineItemToElectionManifestoPromises(TimelineItem timelineItem, List<ElectionManifestoPromise> promises) {
        List<ElectionManifestoPromise> dbElectionManifestoPromises = promiseTimelineItemRepository.getAllElectionManifestoPromisesOfTimelineItem(timelineItem);
        PromiseTimelineItem onePromiseTimelineItem;
        for (ElectionManifestoPromise oneElectionManifestoPromise : promises) {
            onePromiseTimelineItem = promiseTimelineItemRepository.getPromiseTimelineItemRelation(oneElectionManifestoPromise, timelineItem);
            if (onePromiseTimelineItem == null) {
                onePromiseTimelineItem = new PromiseTimelineItem(oneElectionManifestoPromise, timelineItem);
                onePromiseTimelineItem = promiseTimelineItemRepository.save(onePromiseTimelineItem);
            }
            removeFromList(dbElectionManifestoPromises, oneElectionManifestoPromise);
        }
        // Any remaining dbLocationTimelineItems shud be deleted from Db
        for (ElectionManifestoPromise oneElectionManifestoPromise : dbElectionManifestoPromises) {
            onePromiseTimelineItem = promiseTimelineItemRepository.getPromiseTimelineItemRelation(oneElectionManifestoPromise, timelineItem);
            if (onePromiseTimelineItem != null) {
                promiseTimelineItemRepository.delete(onePromiseTimelineItem);
            }
        }
    }

    private void addTimelineItemToLocations(TimelineItem timelineItem, Collection<Location> locations) {
        List<Location> dbLocations = locationTimelineItemRepository.getAllLocationOfTimelineItem(timelineItem);
        LocationTimelineItem oneLocationTimelineItem;
        for (Location oneLocation : locations) {
            System.out.println("Searching LocationTimelineItem for " + oneLocation.getName() + " , " + timelineItem.getTitle());
            oneLocationTimelineItem = locationTimelineItemRepository.getLocationTimelineItemRelation(oneLocation, timelineItem);
            if (oneLocationTimelineItem == null) {
                System.out.println("Creating LocationTimelineItem for " + oneLocation.getName() + " , " + timelineItem.getTitle());
                oneLocationTimelineItem = new LocationTimelineItem(oneLocation, timelineItem);
                oneLocationTimelineItem = locationTimelineItemRepository.save(oneLocationTimelineItem);
            }
            removeFromList(dbLocations, oneLocation);
        }
        // Any remaining dbLocationTimelineItems shud be deleted from Db
        for (Location oneLocation : dbLocations) {
            oneLocationTimelineItem = locationTimelineItemRepository.getLocationTimelineItemRelation(oneLocation, timelineItem);
            if (oneLocationTimelineItem != null) {
                System.out.println("Removin LocationTimelineItem for " + oneLocationTimelineItem.getLocation().getId() + " , " + oneLocationTimelineItem.getTimelineItem().getId() + " , "
                        + oneLocationTimelineItem.getId());
                locationTimelineItemRepository.delete(oneLocationTimelineItem);
            }
        }
    }
    private void addTimelineItemToPoliticalBodyAdmins(TimelineItem timelineItem, List<PoliticalBodyAdminSearchResult> politicalBodyAdminSearchResults) {
        List<PoliticalBodyAdmin> dbPoliticalAdminTimelineItems = politicalAdminTimelineItemRepository.getAllPoliticalBodyAdminOfTimelineItem(timelineItem);
        PoliticalAdminTimelineItem onePoliticalAdminTimelineItem;
        for (PoliticalBodyAdminSearchResult onePoliticalBodyAdminSearchResult : politicalBodyAdminSearchResults) {
            onePoliticalAdminTimelineItem = politicalAdminTimelineItemRepository.getPoliticalAdminTimelineItemRelation(onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin(), timelineItem);
            if (onePoliticalAdminTimelineItem == null) {
                onePoliticalAdminTimelineItem = new PoliticalAdminTimelineItem(onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin(), timelineItem);
                onePoliticalAdminTimelineItem = politicalAdminTimelineItemRepository.save(onePoliticalAdminTimelineItem);
            }
            removeFromList(dbPoliticalAdminTimelineItems, onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin());
        }
        // Any remaining dbPoliticalAdminTimelineItems shud be deleted from Db
        for (PoliticalBodyAdmin onePoliticalBodyAdmin : dbPoliticalAdminTimelineItems) {
            onePoliticalAdminTimelineItem = politicalAdminTimelineItemRepository.getPoliticalAdminTimelineItemRelation(onePoliticalBodyAdmin, timelineItem);
            if (onePoliticalAdminTimelineItem != null) {
                politicalAdminTimelineItemRepository.delete(onePoliticalAdminTimelineItem);
            }
        }
    }
    private <T extends BaseNode> void removeFromList(List<T> list, T itemToRemove) {
        for (T oneItem : list) {
            if (itemToRemove.getId().equals(oneItem.getId())) {
                list.remove(oneItem);
                break;
            }
        }
    }

    @Override
    public TimelineItem saveTimelineItem(TimelineItem timelineItem) throws ApplicationException {
        timelineItem = timelineItemRepository.save(timelineItem);
        return timelineItem;
    }

    @Override
    public List<LocationSearchResult> getTimelineLocations(TimelineItem timelineItem) throws ApplicationException {
        return convertToList(locationTimelineItemRepository.getAllLocationSearchResultOfTimelineItem(timelineItem));
    }

    @Override
    public List<ElectionManifestoPromise> getTimelinePromises(TimelineItem timelineItem) throws ApplicationException {
        return promiseTimelineItemRepository.getAllElectionManifestoPromisesOfTimelineItem(timelineItem);
    }

    @Override
    public List<PoliticalBodyAdminSearchResult> getTimelineAdmins(TimelineItem timelineItem) throws ApplicationException {
        return convertToList(politicalAdminTimelineItemRepository.getAllPoliticalBodyAdminSearchResultOfTimelineItem(timelineItem));
    }

    @Override
    public FacebookAccount getFacebookAccountByPerson(Person person) throws ApplicationException {
        return facebookAccountRepository.getFacebookAccountByPerson(person);
    }

    @Override
    public LeaderTempFacebookAccount addFacebookAccountEmailForPerson(Person person, String email) throws ApplicationException {
        LeaderTempFacebookAccount existingLeaderTempFacebookAccount = leaderTempFacebookAccountRepository.findByPropertyValue("email", email);

        LeaderTempFacebookAccount leaderTempFacebookAccount = leaderTempFacebookAccountRepository.getLeaderTempFacebookAccountByPerson(person);
        if (leaderTempFacebookAccount == null) {
            leaderTempFacebookAccount = new LeaderTempFacebookAccount();
        } else {
            if (existingLeaderTempFacebookAccount != null && !existingLeaderTempFacebookAccount.getId().equals(leaderTempFacebookAccount.getId())) {
                throw new ApplicationException("A User Request already exists for email " + email + " in our system which i slinked to person " + person.getId() + ", " + person.getName()
                        + "and it cant be linked");
            }
        }
        leaderTempFacebookAccount.setEmail(email);
        leaderTempFacebookAccount.setPerson(person);
        leaderTempFacebookAccount.setRequestId(UUID.randomUUID().toString());
        leaderTempFacebookAccount.setDateCreated(new Date());
        leaderTempFacebookAccount.setUrl(leaderServerBaseUrl + "/web/join/eswaraj?pid=" + person.getId() + "&rid=" + leaderTempFacebookAccount.getRequestId() + "&eid=" + email);
        leaderTempFacebookAccount = leaderTempFacebookAccountRepository.save(leaderTempFacebookAccount);

        return leaderTempFacebookAccount;
    }

    @Override
    public List<FacebookAppPermission> getFacebookAppPermission(FacebookAccount facebookAccount) throws ApplicationException {
        return facebookAppPermissionRepository.getFacebookAppPermissionByFacebookAccount(facebookAccount);
    }

    @Override
    public LeaderTempFacebookAccount getFacebookAccountRequestForPerson(Person person) throws ApplicationException {
        return leaderTempFacebookAccountRepository.getLeaderTempFacebookAccountByPerson(person);
    }

    @Override
    public List<Department> getAllRootDepartmentsOfcategory(Category category) throws ApplicationException {
        return departmentRepository.getAllRootDepartmentsOfCategory(category);
    }

    @Override
    public List<Department> getAllRootDepartmentsOfcategory(Long categoryId) throws ApplicationException {
        return departmentRepository.getAllRootDepartmentsOfCategory(categoryId);
    }

    @Override
    public List<Department> getAllChildDepartments(Department department) throws ApplicationException {
        return departmentRepository.getAllChildDepartments(department);
    }

    @Override
    public List<Department> getAllChildDepartments(Long departmentId) throws ApplicationException {
        return departmentRepository.getAllChildDepartments(departmentId);
    }

    @Override
    public Department saveDepartment(Department department, List<Location> locations, List<Category> categories) throws ApplicationException {
        if (department.getDateCreated() == null) {
            department.setDateCreated(new Date());
        }
        department.setDateModified(new Date());
        if (department.getParentDepartment() == null) {
            department.setLevel(1);
        } else {
            department.setLevel(department.getParentDepartment().getLevel() + 1);
        }
        logger.info("Saving Address : {}", department.getAddress());
        department.setAddress(addressRepository.save(department.getAddress()));
        logger.info("Saving Department : {}", department);
        department = departmentRepository.save(department);

        saveDepartmentLocations(department, locations);
        saveDepartmentCategories(department, categories);

        return department;
    }

    private void saveDepartmentCategories(Department department, List<Category> categories) {
        List<DepartmentCategory> departmentCategories = departmentCategoryRepository.getAllDepartmentCategoryRelationOfDepartment(department);

        // first Delete Department Categories
        boolean categoryFound;
        for (DepartmentCategory oneDepartmentCategory : departmentCategories) {
            categoryFound = false;
            for (Category oneCategory : categories) {
                if (oneCategory.getId().equals(oneDepartmentCategory.getCategory().getId())) {
                    categoryFound = true;
                }
            }
            if (!categoryFound) {
                departmentCategoryRepository.delete(oneDepartmentCategory);
            }
        }
        // Now create new one
        for (Category oneCategory : categories) {
            categoryFound = false;
            for (DepartmentCategory oneDepartmentCategory : departmentCategories) {
                if (oneCategory.getId().equals(oneDepartmentCategory.getCategory().getId())) {
                    categoryFound = true;
                }
            }

            if (!categoryFound) {
                if (!oneCategory.isRoot()) {// save only for non root categories
                    DepartmentCategory oneDepartmentCategory = new DepartmentCategory(department, oneCategory);
                    oneDepartmentCategory = departmentCategoryRepository.save(oneDepartmentCategory);
                }
            }
        }

    }

    private void saveDepartmentLocations(Department department, List<Location> locations) {
        List<DepartmentLocation> departmentLocations = departmentLocationRepository.getAllDepartmentLocationRelationOfDepartment(department);

        // first Delete Department Locations
        boolean locationFound;
        for (DepartmentLocation oneDepartmentLocation : departmentLocations) {
            locationFound = false;
            for (Location oneLocation : locations) {
                if (oneLocation.getId().equals(oneDepartmentLocation.getLocation().getId())) {
                    locationFound = true;
                }
            }
            if (!locationFound) {
                departmentLocationRepository.delete(oneDepartmentLocation);
            }
        }
        // Now create new one
        for (Location oneLocation : locations) {
            locationFound = false;
            for (DepartmentLocation oneDepartmentLocation : departmentLocations) {
                if (oneLocation.getId().equals(oneDepartmentLocation.getLocation().getId())) {
                    locationFound = true;
                }
            }

            if (!locationFound) {
                DepartmentLocation oneDepartmentLocation = new DepartmentLocation(department, oneLocation);
                oneDepartmentLocation = departmentLocationRepository.save(oneDepartmentLocation);
            }
        }

    }

    @Override
    public List<Location> getAllLocations() throws ApplicationException {
        return locationRepository.getAllLocations();
    }

    @Override
    public List<Location> getAllLocationsOfDepartment(Department department) throws ApplicationException {
        return departmentLocationRepository.getAllLocationOfDepartment(department);
    }

    @Override
    public EswarajAccount savePersonLoginDetail(Person person, String userName, String password) throws ApplicationException {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        if (StringUtils.isEmpty(password)) {
            logger.info("No Password Provided so no update");
        }
        User user = userRepository.getUserByPerson(person);
        if (user == null) {
            user = new User();
            user.setPerson(person);
            user.setDateCreated(new Date());
            user = userRepository.save(user);
        }
        EswarajAccount eswarajAccount = eswarajAccountRepository.getEswarajAccountByUser(user);
        if (eswarajAccount == null) {
            eswarajAccount = new EswarajAccount();
            eswarajAccount.setUserName(userName);
            eswarajAccount.setUser(user);
        }
        eswarajAccount.setPassword(passwordUtil.encryptPassword(password));
        eswarajAccount = eswarajAccountRepository.save(eswarajAccount);
        return eswarajAccount;
    }

    @Override
    public EswarajAccount getPersonEswarajAccount(Person person) throws ApplicationException {
        User user = userRepository.getUserByPerson(person);
        if (user == null) {
            return null;
        }
        EswarajAccount eswarajAccount = eswarajAccountRepository.getEswarajAccountByUser(user);
        return eswarajAccount;
    }

    @Override
    public List<Person> getDepartmentStaffMembers(Department department) throws ApplicationException {
        return personRepository.getDepartmentStaffMembers(department);
    }

    @Override
    public DepartmentAdmin addDepartmentStaff(Department department, Person person) throws ApplicationException {
        DepartmentAdmin departmentAdmin = departmentAdminRepository.getDepartmentAdminByDepartmentAndPerson(department, person);
        if (departmentAdmin == null) {
            departmentAdmin = new DepartmentAdmin();
            departmentAdmin.setDepartment(department);
            departmentAdmin.setPerson(person);
            departmentAdmin.setDateCreated(new Date());
        }
        departmentAdmin.setDateModified(new Date());
        departmentAdmin = departmentAdminRepository.save(departmentAdmin);
        return departmentAdmin;
    }

    @Override
    public DepartmentAdmin deleteDepartmentStaff(Department department, Person person) throws ApplicationException {
        DepartmentAdmin departmentAdmin = departmentAdminRepository.getDepartmentAdminByDepartmentAndPerson(department, person);
        if (departmentAdmin != null) {
            departmentAdminRepository.delete(departmentAdmin);
        }
        return departmentAdmin;
    }

    @Override
    public List<Department> getAllRootDepartments() throws ApplicationException {
        return departmentRepository.getAllRootDepartments();
    }

    @Override
    public List<Category> getAllCategoriesOfDepartment(Department department) throws ApplicationException {
        return departmentCategoryRepository.getAllCategoryOfDepartment(department);
    }

}
