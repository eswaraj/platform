package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;

/**
 * Repo for location queries
 * @author anuj
 * @data Jan 28, 2014
 */
public interface LocationBoundaryFileRepository extends GraphRepository<LocationBoundaryFile>{
	
    @Query("start location=node({0}) match (locationBoundaryFile)-[:FOR]->(location) where locationBoundaryFile.active = 'true' return locationBoundaryFile ")
    public LocationBoundaryFile getActiveLocationBoundaryFile(Location location);

    @Query("start location=node({0}) match (locationBoundaryFile)-[:FOR]->(location) return locationBoundaryFile order by location.uploadDate DESC")
    public List<LocationBoundaryFile> getAllLocationBoundaryFile(Location location);

    @Query("start location=node({0}) match (locationBoundaryFile)-[:FOR]->(location) return locationBoundaryFile order by location.uploadDate DESC")
    public List<LocationBoundaryFile> getAllLocationBoundaryFile(Long locationId);

    @Query("match (locationBoundaryFile)-[:FOR]->(location) where locationBoundaryFile.__type__='LocationBoundaryFile' and locationBoundaryFile.active = 'true' return locationBoundaryFile ")
    public List<LocationBoundaryFile> getAllActiveLocationBoundaryFiles();

    @Query("match (locationBoundaryFile)-[:FOR]->(location) where locationBoundaryFile.__type__='LocationBoundaryFile' and locationBoundaryFile.status!='Done' and locationBoundaryFile.active = 'true' return locationBoundaryFile ")
    public List<LocationBoundaryFile> getAllActiveFailedLocationBoundaryFiles();

}
