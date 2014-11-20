package com.next.eswaraj.admin.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;

public interface AdminService {

    LocationType saveLocationType(LocationType locationType) throws ApplicationException;

    LocationType getRootLocationType() throws ApplicationException;
    
    LocationType getLocationTypeById(Long locationTypeId) throws ApplicationException;

    Location getLocationById(Long locationId) throws ApplicationException;

    List<LocationType> getChildLocationsTypeOfParent(Long parentLocationTypeId) throws ApplicationException;

    Location getRootLocationForSwarajIndia() throws ApplicationException;

    List<Location> getChildLocationsOfParent(Long parentLocationId) throws ApplicationException;

    List<LocationBoundaryFile> getLocationBoundaryFiles(Long locationId) throws ApplicationException;

    List<Location> findLocationByParentLocationAndLocationType(Long parentLocationId, Long locationTypeId) throws ApplicationException;

    Location saveLocation(Location location) throws ApplicationException;
}
