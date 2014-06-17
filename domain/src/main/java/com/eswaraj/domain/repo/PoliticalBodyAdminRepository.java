package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;

public interface PoliticalBodyAdminRepository extends GraphRepository<PoliticalBodyAdmin>{
	
	@Query("start location=node({0}) match (location)<-[:BELONGS_TO]-(PoliticalAdmin) where PoliticalAdmin.active=true return PoliticalAdmin")
	PoliticalBodyAdmin getCurrentPoliticalAdminByLocation(Location location);

	@Query("start location=node({0}) match (location)<-[:BELONGS_TO]-(PoliticalAdmin) return PoliticalAdmin")
	Collection<PoliticalBodyAdmin> getAllPoliticalAdminByLocation(Location location);
}
