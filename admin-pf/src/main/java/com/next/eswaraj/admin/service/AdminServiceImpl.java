package com.next.eswaraj.admin.service;

import java.util.List;

import org.neo4j.cypher.MissingIndexException;
import org.neo4j.rest.graphdb.RestResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.DataClient;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.repo.DataClientRepository;
import com.eswaraj.domain.repo.LocationBoundaryFileRepository;
import com.eswaraj.domain.repo.LocationRepository;
import com.eswaraj.domain.repo.LocationTypeRepository;

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
    private LocationBoundaryFileRepository locationBoundaryFileRepository;

    @Override
    public LocationType saveLocationType(LocationType locationType) throws ApplicationException {
        DataClient dataClient = getOrCreateDataClientIndiaEswaraj();
        locationType.setDataClient(dataClient);
        if (locationType.getId() == null) {
            // means we are trying to create new location type
            checkIfRootLocationAlreadyExists(locationType, dataClient);
        }
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
        // TODO Auto-generated method stub
        return null;
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

}
