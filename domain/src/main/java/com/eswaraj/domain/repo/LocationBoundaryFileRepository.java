package com.eswaraj.domain.repo;

import java.util.Collection;

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
    public Collection<LocationBoundaryFile> getAllLocationBoundaryFile(Location location);
}
