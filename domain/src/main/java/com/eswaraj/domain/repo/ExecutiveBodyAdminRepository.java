package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ExecutiveBody;
import com.eswaraj.domain.nodes.ExecutiveBodyAdmin;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.PoliticalBodyType;

public interface ExecutiveBodyAdminRepository extends GraphRepository<ExecutiveBodyAdmin>{

	@Query("start executiveBody=node({0}) match (executiveBody)<-[:WORKS_FOR]-(executiveBodyAdmins) return executiveBodyAdmins")
	public Collection<ExecutiveBodyAdmin> getAllAdminsOfExecutiveBody(ExecutiveBody executiveBody);

    @Query("start location=node({0}),politicalBodyType=node({1})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin)-[:OF_TYPE]->politicalBodyType return PoliticalAdmin")
    Collection<ExecutiveBodyAdmin> getAllExecutiveAdminByLocationAndPoliticalBodyType(Location location, PoliticalBodyType politicalBodyType);

}
