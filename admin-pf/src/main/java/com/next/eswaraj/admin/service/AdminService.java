package com.next.eswaraj.admin.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;

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

    PoliticalBodyType savePoliticalBodyType(PoliticalBodyType politicalBodyType) throws ApplicationException;

    List<Party> getAllParties() throws ApplicationException;

    Party saveParty(Party party) throws ApplicationException;

    List<LocationSearchResult> searchLocationByName(String name) throws ApplicationException;
}
