package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminExtended;

public interface PoliticalBodyAdminRepository extends GraphRepository<PoliticalBodyAdmin>{
	
    @Query("start location=node({0}) match (location)<-[:BELONGS_TO]-(PoliticalAdmin) where PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' and PoliticalAdmin.active=true return PoliticalAdmin")
	Collection<PoliticalBodyAdmin> getCurrentPoliticalAdminByLocation(Location location);

    @Query("start location=node({0}),politicalBodyType=node({1})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin)-[:OF_TYPE]->politicalBodyType where PoliticalAdmin.active=true and PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' return PoliticalAdmin")
	PoliticalBodyAdmin getCurrentPoliticalAdminByLocationAndPoliticalBodyType(Location location, PoliticalBodyType politicalBodyType);

    @Query("start location=node({0})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin) where PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' and PoliticalAdmin.active=true return PoliticalAdmin")
    Collection<PoliticalBodyAdmin> getAllCurrentPoliticalAdminByLocationAndPoliticalBodyType(Location location);

    // @Query("start location=node({0}) match (location)<-[:BELONGS_TO]-(PoliticalAdmin) return PoliticalAdmin")
	//Collection<PoliticalBodyAdmin> getAllPoliticalAdminByLocation(Location location);

    @Query("start location=node({0}),politicalBodyType=node({1})  match (location)<-[:BELONGS_TO]-(PoliticalAdmin)-[:OF_TYPE]->politicalBodyType where PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' return PoliticalAdmin")
	Collection<PoliticalBodyAdmin> getAllPoliticalAdminByLocationAndPoliticalBodyType(Location location, PoliticalBodyType politicalBodyType);

    @Query("start person=node({0}) match (person)<-[:IS]-(PoliticalAdmin) where PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' return PoliticalAdmin")
    Collection<PoliticalBodyAdmin> getAllPoliticalAdminHistoryByPerson(Person person);

    @Query("start person=node({0}) match (person)<-[:IS]-(PoliticalAdmin) where PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' and PoliticalAdmin.active=true return PoliticalAdmin")
    Collection<PoliticalBodyAdmin> getActivePoliticalAdminHistoryByPerson(Person person);

    @Query("start complaint=node({0}) match (complaint)-[PSB:POLITICAL_SERVED_BY]->(PoliticalAdmin) where PoliticalAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' return PoliticalAdmin")
    Collection<PoliticalBodyAdmin> getAllPoliticalAdminOfComplaint(Complaint complaint);

    @Query("start location=node({0}),politicalBodyType=node({1})  match (location)-[:BELONGS_TO]-(politicalBodyAdmin)-[:OF_TYPE]->(politicalBodyType) with politicalBodyAdmin, location,  politicalBodyType optional match (politicalBodyAdmin)-[:IS]-(person) with politicalBodyAdmin, location,  politicalBodyType, person optional match (party)-[:OF]-(politicalBodyAdmin) where politicalBodyAdmin.__type__='com.eswaraj.domain.nodes.PoliticalBodyAdmin' or politicalBodyAdmin.__type__='PoliticalBodyAdmin' return politicalBodyAdmin, politicalBodyType, location, person, party")
    EndResult<PoliticalBodyAdminExtended> getAllPoliticalAdminByLocationAndPoliticalBodyType(Long locationId, Long politicalBodyTypeId);

}
