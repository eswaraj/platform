package com.eswaraj.core.service.impl;

import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.neo4j.cypher.MissingIndexException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eswaraj.core.convertors.LocationBoundaryFileConvertor;
import com.eswaraj.core.convertors.LocationConvertor;
import com.eswaraj.core.convertors.LocationTypeConvertor;
import com.eswaraj.core.convertors.LocationTypeJsonConvertor;
import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;
import com.eswaraj.core.service.LocationService;
import com.eswaraj.core.util.AwsQueueProducer;
import com.eswaraj.core.util.DateTimeUtil;
import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.repo.DataClientRepository;
import com.eswaraj.domain.repo.LocationBoundaryFileRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;
import com.eswaraj.web.dto.BoundaryDto;
import com.eswaraj.web.dto.GeoPointDto;
import com.eswaraj.web.dto.LocationBoundaryFileDto;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;
import com.eswaraj.web.dto.LocationTypeJsonDto;
import com.google.gson.JsonObject;

@Component
@Transactional
public class LocationServiceImpl implements LocationService {

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
    private AwsQueueProducer awsQueueProducer;
	
    @Value("${aws_s3_directory_for_location_files:locations}")
	private String awsDirectoryForLocationFiles;

    @Value("${aws_location_file_queue_name}")
    private String awsLocationQueueName;

	private String indiaEswarajClientName = "Eswaraj-India";
	private String indiaEswarajRootLocationTypeName = "Country";
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public LocationDto saveLocation(LocationDto locationDto)  throws ApplicationException{
		Location location = locationConvertor.convert(locationDto);
		//Check Parent child rule
		checkParentChildRule(location);
		locationRepository.save(location);
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
	public LocationBoundaryFileDto createNewLocationBoundaryFile(Long locationId, InputStream inputStream, FileService fileService) throws ApplicationException {
		Location location = locationRepository.findOne(locationId);
		if(location == null){
			throw new ApplicationException("No such location exists[id="+locationId+"]");
		}
        LocationBoundaryFile existingLocationBoundayrFile = locationBoundaryFileRepository.getActiveLocationBoundaryFile(location);
        String currenttime = dateTimeUtil.getCurrentTimeYYYYMMDDHHMMSS();
        if (existingLocationBoundayrFile != null) {
            if (existingLocationBoundayrFile.getStatus().equals("Pending")) {
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

		//create LocationBoudaryFile
		LocationBoundaryFile locationBoundaryFile = new LocationBoundaryFile();
		locationBoundaryFile.setLocation(location);
		locationBoundaryFile.setFileNameAndPath(httpPath);
		locationBoundaryFile.setStatus("Pending");
		locationBoundaryFile.setUploadDate(new Date());
        locationBoundaryFile.setActive(true);
		
		locationBoundaryFile = locationBoundaryFileRepository.save(locationBoundaryFile);

        // Updaye Locaion Object with KMl file
        location.setBoundaryFile(httpPath);

        JsonObject jsonObject = new JsonObject();
        if (existingLocationBoundayrFile != null) {
            jsonObject.addProperty("oldLocationBoundaryFileId", existingLocationBoundayrFile.getId());
        }
        jsonObject.addProperty("newLocationBoundaryFileId", locationBoundaryFile.getId());
        jsonObject.addProperty("locationId", location.getId());

        logger.info("Sending message {} to queue {}", jsonObject.toString(), awsLocationQueueName);

        awsQueueProducer.sendMessage(awsLocationQueueName, jsonObject.toString());

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
			
		}catch(MissingIndexException|InvalidDataAccessResourceUsageException mie){
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
		LocationType locationType = locationTypeRepository.getRootLocationTypeByDataClient(dataClient.getName());
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
			LocationType existingRootLocationType = locationTypeRepository.getRootLocationTypeByDataClient(dataClient.getName());
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

}
