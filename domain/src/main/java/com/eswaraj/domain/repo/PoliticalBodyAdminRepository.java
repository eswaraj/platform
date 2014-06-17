package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;

public interface PoliticalBodyAdminRepository extends GraphRepository<PoliticalBodyAdmin>{
	
	@Query("start location=node({0}) match (location)<-[:BELONGS_TO]-(PoliticalAdmin) where PoliticalAdmin.active=true return PoliticalAdmin")
	Collection<PoliticalBodyAdmin> getCurrentPoliticalAdminByLocation(Location location);

	@Query("start location=node({0}),politicalBodyType=node({1})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin)-[:OF_TYPE]->politicalBodyType where PoliticalAdmin.active=true return PoliticalAdmin")
	PoliticalBodyAdmin getCurrentPoliticalAdminByLocationAndPoliticalBodyType(Location location, PoliticalBodyType politicalBodyType);

	//@Query("start location=node({0}) match (location)<-[:BELONGS_TO]-(PoliticalAdmin) return PoliticalAdmin")
	//Collection<PoliticalBodyAdmin> getAllPoliticalAdminByLocation(Location location);

	@Query("start location=node({0}),politicalBodyType=node({1})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin)-[:OF_TYPE]->politicalBodyType return PoliticalAdmin")
	Collection<PoliticalBodyAdmin> getAllPoliticalAdminByLocationAndPoliticalBodyType(Location location, PoliticalBodyType politicalBodyType);

}
