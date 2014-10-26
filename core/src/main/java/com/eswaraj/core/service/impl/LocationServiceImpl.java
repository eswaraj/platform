package com.eswaraj.core.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.eswaraj.core.convertors.LocationBoundaryFileConvertor;
import com.eswaraj.core.convertors.LocationConvertor;
import com.eswaraj.core.convertors.LocationTypeConvertor;
import com.eswaraj.core.convertors.LocationTypeJsonConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.util.DateTimeUtil;
import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.repo.DataClientRepository;
import com.eswaraj.domain.repo.LocationBoundaryFileRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.domain.repo.PoliticalBodyAdminRepository;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.BoundaryDto;
import com.eswaraj.web.dto.GeoPointDto;
import com.eswaraj.web.dto.LocationBoundaryFileDto;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.LocationTypeJsonDto;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
@Transactional
public class LocationServiceImpl extends BaseService implements LocationService {

    private static final long serialVersionUID = 1L;
    @Autowired
	private DateTimeUtil dateTimeUtil;
	@Autowired 
	private LocationRepository locationRepository;
	@Autowired
	private LocationConvertor locationConvertor;
	@Autowired
	private LocationBoundaryFileRepository locationBoundaryFileRepository;
	@Autowired
	private LocationBoundaryFileConvertor locationBoundaryFileConvertor;
	@Autowired
	private LocationTypeRepository locationTypeRepository;
	@Autowired
	private LocationTypeConvertor locationTypeConvertor;
	@Autowired
	private LocationTypeJsonConvertor locationTypeJsonConvertor;
	@Autowired
	private DataClientRepository dataClientRepository;
    @Autowired
    private PoliticalBodyAdminRepository politicalBodyAdminRepository;
    @Autowired
    private QueueService queueService;

	
    @Value("${aws_s3_directory_for_location_files:locations}")
	private String awsDirectoryForLocationFiles;


	private String indiaEswarajClientName = "Eswaraj-India";
	private String indiaEswarajRootLocationTypeName = "Country";
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public LocationDto saveLocation(LocationDto locationDto)  throws ApplicationException{
		Location location = locationConvertor.convert(locationDto);
		//Check Parent child rule
		checkParentChildRule(location);
        location.setUrlIdentifier(getLocationUrlIdentifier(location));
		locationRepository.save(location);
        queueService.sendLocationUpdateMessage(location.getId());
		return locationConvertor.convertBean(location);
	}
	private void checkParentChildRule(Location location) throws ApplicationException{
		//LocationType parentLocationType = locationTypeRepository.findOne(location.getParentLocation().getLocationType().getId());
		if(location.getParentLocation() == null){
			if(location.getLocationType().getParentLocationType() != null){
				throw new ApplicationException("Can not create a Location of type ["+location.getLocationType().getName()+"], without a parent Location");
			}
		}else{
			Location parentLocation = location.getParentLocation();
			if(!location.getLocationType().getParentLocationType().getId().equals(parentLocation.getLocationType().getId())){
				LocationType parentLocationType = locationTypeRepository.findOne(parentLocation.getLocationType().getId());
				throw new ApplicationException("Can not create a Location of type ["+location.getLocationType().getName()+"], under location type ["+parentLocationType.getName()+"]");
			}
		}
	}

	@Override
	public LocationDto getLocationById(Long id)  throws ApplicationException{
		Location dbLocation = locationRepository.findOne(id);
		return locationConvertor.convertBean(dbLocation);
	}

	@Override
	public List<LocationDto> getChildLocationsOfParent(Long parentLocationId)  throws ApplicationException{
		Location parenLocation = locationRepository.findOne(parentLocationId);
		Collection<Location> childLocations = locationRepository.findLocationByParentLocation(parenLocation);
		return locationConvertor.convertBeanList(childLocations);
	}

	@Override
    public LocationBoundaryFileDto createNewLocationBoundaryFile(Long locationId, String originalFilename, InputStream inputStream, FileService fileService) throws ApplicationException {
		Location location = locationRepository.findOne(locationId);
		if(location == null){
			throw new ApplicationException("No such location exists[id="+locationId+"]");
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
        String fileDir = awsDirectoryForLocationFiles+"/" +location.getId(); 
        String fileName = UUID.randomUUID().toString() + "_" + currenttime + ".kml";
        logger.info("saving file {}", fileName);
        //save file to a storage
        String httpPath = fileService.saveFile(fileDir, fileName, inputStream);
        System.out.println("httpPath Saved = " + httpPath);
		//create LocationBoudaryFile
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

		return locationBoundaryFileConvertor.convertBean(locationBoundaryFile);
	}

	@Override
	public BoundaryDto saveBoundary(BoundaryDto boundaryDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeoPointDto saveBoundaryPoint(GeoPointDto geoPointDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private DataClient getOrCreateDataClientIndiaEswaraj(){
		DataClient dataClient = null;
		try{
			dataClient = dataClientRepository.getDataClientByName(indiaEswarajClientName);
			
        } catch (MissingIndexException | InvalidDataAccessResourceUsageException | RestResultException mie) {
			//for first time this exception gets thrown as we havent created a single node of type DataClient.
			//just catch it and ignore it
			dataClient = null;
		}
		if(dataClient == null){
			dataClient = new DataClient();
			dataClient.setName(indiaEswarajClientName);
			dataClient = dataClientRepository.save(dataClient);
		}
		return dataClient;
	}
	private LocationType getOrCreateRootLocationTypeIndiaEswaraj(DataClient dataClient){
        LocationType locationType = locationTypeRepository.getRootLocationTypeByDataClient(dataClient);
		if(locationType == null){
			locationType = new LocationType();
			locationType.setName(indiaEswarajRootLocationTypeName);
			locationType.setDataClient(dataClient);
			locationType.setRoot(true);
			locationType = locationTypeRepository.save(locationType);
		}
		return locationType;
	}
	
	

	private void checkIfRootLocationAlreadyExists(LocationType locationType, DataClient dataClient) throws ApplicationException {
		if(locationType.getParentLocationType() == null){
			//now check if one ROOT Location already exists for this Data client
			//If yes then throw exception as we can not have more then one Root Location(usually country) for a Data client
            LocationType existingRootLocationType = locationTypeRepository.getRootLocationTypeByDataClient(dataClient);
			if(existingRootLocationType != null){
				throw new ApplicationException("One root location type already exists["+ existingRootLocationType.getName()+","+existingRootLocationType.getId()+"], you can not create more then one root location Type. If you are trying to create child location type then make sure you set parentLocationType");
			}
		}
	}
	@Override
	public LocationTypeDto saveLocationType(LocationTypeDto locationTypeDto) throws ApplicationException {
		return saveRootLocationType(locationTypeDto, false);
	}

	@Override
	public LocationDto getRootLocationForSwarajIndia() throws ApplicationException {
		DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
		LocationType locationType = getOrCreateRootLocationTypeIndiaEswaraj(dataClient);
		Location location = locationRepository.getRootLocationByLocationType(locationType.getId());
		if(location == null){
			//create default location India
			location = new Location();
			location.setLocationType(locationType);
			location.setName("India");
			location = locationRepository.save(location);
		}
		return locationConvertor.convertBean(location);
	}

	@Override
	public LocationTypeJsonDto getLocationTypes(String clientName) throws ApplicationException {
		//Ignoring passed client name and will use hardcoded client name
		DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
		Collection<LocationType> locationTypes = locationTypeRepository.getAllLocationTypeOfDataClient(dataClient.getId());
		return locationTypeJsonConvertor.convertToJsonBean(locationTypes);
	}
	@Override
	public List<LocationTypeDto> getChildLocationsTypeOfParent(Long parentLocationTypeId) throws ApplicationException {
		LocationType parentLocationType = locationTypeRepository.findOne(parentLocationTypeId);
		/*
		if(locationType == null){
			return null;
		}*/
		Collection<LocationType> locationTypes = locationTypeRepository.findLocationTypeByParentLocation(parentLocationType);
		return locationTypeConvertor.convertBeanList(locationTypes);
	}
	public LocationTypeDto saveRootLocationType(LocationTypeDto locationTypeDto, boolean isRoot) throws ApplicationException {
		LocationType locationType = locationTypeConvertor.convert(locationTypeDto);
		//get the data Client, right now its hard coded for eswaraj-India, in future we will get it from client
		DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
		locationType.setDataClient(dataClient);
		if(locationType.getId() == null){
			//means we are trying to create new location type
			checkIfRootLocationAlreadyExists(locationType, dataClient);
		}
		locationType.setRoot(isRoot);
		locationType = locationTypeRepository.save(locationType);
		
		return locationTypeConvertor.convertBean(locationType);
	}
	@Override
	public LocationTypeDto saveRootLocationType(LocationTypeDto locationTypeDto) throws ApplicationException {
		
		return saveRootLocationType(locationTypeDto, true);
	}

    @Override
    public LocationTypeDto getLocationTypeById(Long locationTypeId) throws ApplicationException {
        LocationType locationType = locationTypeRepository.findOne(locationTypeId);
        return locationTypeConvertor.convertBean(locationType);
    }

    @Override
    public List<LocationDto> getLocations(Collection<Long> locations) throws ApplicationException {
        Iterable<Location> locationIterbale = locationRepository.findAll(locations);
        return locationConvertor.convertBeanList(locationIterbale);
    }

    private void loadAllLocationTypes() throws IOException {
        DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
        String categoryJson = readFile("/data/locationType.json");
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(categoryJson);
        saveLocationType(jsonObject, dataClient, null);
    }

    private void saveLocationType(JsonObject jsonObject, DataClient dataClient, LocationType parent) {
        LocationType locationType = new LocationType();
        locationType.setDataClient(dataClient);
        locationType.setName(jsonObject.get("name").getAsString());
        if(parent == null){
            locationType.setRoot(true);
        }else{
            locationType.setRoot(false);
        }
        locationType.setParentLocationType(parent);
        locationType = locationTypeRepository.save(locationType);

        if (jsonObject.get("children").getClass() != JsonNull.class) {
            JsonArray childArray = (JsonArray) jsonObject.get("children");
            if (childArray != null) {
                for (int j = 0; j < childArray.size(); j++) {
                    JsonObject jsonChildObject = (JsonObject) childArray.get(j);
                    saveLocationType(jsonChildObject, dataClient, locationType);
                }
            }
        }

    }

    @Override
    public void initializeData() throws ApplicationException {
        try {
            loadAllLocationTypes();
        } catch (IOException e) {
            throw new ApplicationException(e);
        }

    }

    @Override
    public LocationBoundaryFileDto getLocationBoundaryFileById(Long locationBoundaryFileId) throws ApplicationException {
        LocationBoundaryFile locationBoundaryFile = locationBoundaryFileRepository.findOne(locationBoundaryFileId);
        return locationBoundaryFileConvertor.convertBean(locationBoundaryFile);
    }

    @Override
    public List<LocationDto> getAllParents(Long locationId) throws ApplicationException {
        List<LocationDto> list = new ArrayList<>();
        Location location = locationRepository.findOne(locationId);
        while (location != null) {
            list.add(locationConvertor.convertBean(location));
            if (location.getParentLocation() != null) {
                location = locationRepository.findOne(location.getParentLocation().getId());
            } else {
                location = null;// break from the loop
            }
        }
        return list;
    }



    @Override
    public void updateAllLocationUrls() throws ApplicationException {

        EndResult<LocationType> allLocationTypeResultSet = locationTypeRepository.findAllByPropertyValue("__type__", "com.eswaraj.domain.nodes.LocationType");
        try {
            String urlIdentifier;
            for (LocationType oneLocationType : allLocationTypeResultSet) {
                urlIdentifier = getLocationTypeUrlIdentifier(oneLocationType);
                oneLocationType.setUrlIdentifier(urlIdentifier);
                System.out.println("updaing location Type : " + oneLocationType);
                logger.info("updaing location Type : {}", oneLocationType);
                locationTypeRepository.save(oneLocationType);
            }
        } finally {
            try {
                allLocationTypeResultSet.finish();
            } catch (Exception ex) {

            }
        }
        System.out.println("***LocationType  Done, starting Location");
        EndResult<Location> allLocationResultSet = locationRepository.findAll();
        try {
            String urlIdentifier;
            Set<String> alreadyUsedUrlids = new HashSet<>();
            List<Location> locations = new ArrayList<>();
            for (Location oneLocation : allLocationResultSet) {
                System.out.println("updaing location : " + oneLocation);
                logger.info("updaing location : {}", oneLocation);
                if (oneLocation.getName() == null) {
                    continue;
                }
                urlIdentifier = getLocationUrlIdentifier(oneLocation);
                if (alreadyUsedUrlids.contains(urlIdentifier)) {
                    System.out.println("\n***** Already used : " + urlIdentifier);
                }
                oneLocation.setUrlIdentifier(urlIdentifier);
                logger.info("updaing location : {}", oneLocation);
                locations.add(oneLocation);

            }
            for (Location location : locations) {
                locationRepository.save(location);
            }
        } finally {
            try {
                allLocationResultSet.finish();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }


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
            if (!locationTypeUrlId.equals("country")) {//No need to add country in url
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
    public List<LocationDto> getLocations(long start, long pageSize) throws ApplicationException {
        try {
            logger.info("Getting locations using own query, start = {}, pageSize = {}", start, pageSize);
            List<Location> locations = locationRepository.getAllPagedLocations(start, pageSize);
            return locationConvertor.convertBeanList(locations);

        } catch (Exception ex) {
            logger.error("Unable to get Locations", ex);
        }
        Pageable pageable = new PageRequest((int) (start / pageSize), (int) pageSize, Sort.Direction.ASC, "id");
        logger.info("Getting locations using system generated query, oage = {}, pageSize = {}, pageable={}", start / pageSize, pageSize, pageable);
        Page<Location> pagedLocations = locationRepository.findAll(pageable);
        return locationConvertor.convertBeanList(pagedLocations);

    }

    @Override
    public List<LocationDto> searchLocationByName(String name) throws ApplicationException {
        Collection<Location> locations = locationRepository.searchLocationByName("name:*" + name + "*");
        return locationConvertor.convertBeanList(locations);
    }

    @Override
    public List<LocationBoundaryFileDto> GetLocationAllBoundaryFile(Long locationId) throws ApplicationException {
        Location location = locationRepository.findOne(locationId);
        Collection<LocationBoundaryFile> locationBoundaryFiles = locationBoundaryFileRepository.getAllLocationBoundaryFile(location);
        return locationBoundaryFileConvertor.convertBeanList(locationBoundaryFiles);
    }

    @Override
    public LocationBoundaryFileDto setLocationBoundaryFileStatus(Long locationBoundaryFileId, String status, boolean active) throws ApplicationException {
        LocationBoundaryFile locationBoundaryFile = locationBoundaryFileRepository.findOne(locationBoundaryFileId);
        Collection<LocationBoundaryFile> allExistingLocationBoudaryFiles = locationBoundaryFileRepository.getAllLocationBoundaryFile(locationBoundaryFile.getLocation());
        for (LocationBoundaryFile oneLocationBoundaryFile : allExistingLocationBoudaryFiles) {
            if (oneLocationBoundaryFile.getId().equals(locationBoundaryFile.getId())) {
                // Update it as per parameters
                oneLocationBoundaryFile.setStatus(status);
                oneLocationBoundaryFile.setActive(active);
                locationBoundaryFile = oneLocationBoundaryFile;
            } else {
                oneLocationBoundaryFile.setStatus("Done");
                if (active) {
                    oneLocationBoundaryFile.setActive(false);
                }
            }
            locationBoundaryFileRepository.save(oneLocationBoundaryFile);
        }
        return locationBoundaryFileConvertor.convertBean(locationBoundaryFile);
    }

}
