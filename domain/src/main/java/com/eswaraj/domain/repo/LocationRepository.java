package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ExecutiveBody;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;

/**
 * Repo for location queries
 * @author anuj
 * @data Jan 28, 2014
 */
public interface LocationRepository extends GraphRepository<Location>{
	
	@Query("start location=node({0})" +
			"match (location)<-[:SERVED_BY]-(executiveBody) return executiveBody")
	public Set<ExecutiveBody> findExecutiveBodies(Location location);

    @Query("start n=node:LocationNameFt({0}) return n")
    Collection<Location> searchLocationByName(String name);
	/*
	@Query("start location=node({0}) " +
			"match location<--politicalBody return politicalBody")
	public Set<PoliticalBody> findPoliticalBodies(Location location);
	*/
	
	@Query("start location=node:Location(name={0}) where location.locationType={1} return location")
    public Location getLocationByNameAndType(String name, LocationType locationType);

    @Query("start locationType=node({0}) match (locationType)<-[:OF_TYPE]-(location) where (NOT HAS(location.parentLocation)) OR location.parentLocation is null return location")
    public Location getRootLocationByLocationType( Long locationTypeId);

	@Query("start location=node:Location(locationType={0}) return location")
    public List<Location> findLocationByLocationtype(LocationType locationType);

	@Query("start location=node:Location(name={0}) match (location)-[:PART_OF]->(parentlocation) return parentlocation")
    public Location getParentLocation(String locationName);

	@Query("start location=node:Location(name={0}) match (location)<-[:PART_OF]-(childlocation) return childlocation")
    public Collection<Location> findLocationByParentLocation(String parentLocationName);
	
    @Query("start location=node({0}) match (location)<-[:PART_OF]-(childlocation) return childlocation")
    public List<Location> findLocationByParentLocation(Long parentLocationId);

    @Query("start location=node({0}) match (location)<-[:PART_OF]-(childlocation) return childlocation")
    public Collection<Location> findLocationByParentLocation(Location location);

	@Query("start location=node:Location(name={0}) match (location)<-[:PART_OF]-(childlocation) where childlocation.locationType={1}  return childlocation")
    public Collection<Location> findLocationByParentLocationAndLocationType(String parentLocationName, LocationType locationType);

    @Query("start location=node({0}) match (location)<-[:PART_OF]-(childlocation) where childlocation.urlIdentifier={1} return childlocation")
    public Location findLocationByParentLocationAndUrlId(Location parentLocation, String urlId);

    @Query("match location where location.__type__ = 'com.eswaraj.domain.nodes.Location' return location order by location.id ASC " + "skip {0} limit {1}")
    public List<Location> getAllPagedLocations(long start, long pageSize);

}
