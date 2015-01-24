package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminStaffSearchResult;

public interface PoliticalBodyAdminStaffRepository extends GraphRepository<PoliticalBodyAdminStaff> {
	
    @Query("start politicalBodyAdmin=node({0})  match (politicalBodyAdminStaff)-[:WORKING_FOR]->politicalBodyAdmin return politicalBodyAdminStaff")
    Collection<PoliticalBodyAdminStaff> getAllPoliticalAdminStaffByPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin);

    // @Query("start location=node:LocationNameFt({0}) match (location)-[:OF_TYPE]->(locationType) return distinct location, locationType limit 10")
    @Query("start politicalBodyAdmin=node({0})  match (person)<-[:IS]-(politicalBodyAdminStaff)-[:WORKING_FOR]->politicalBodyAdmin return politicalBodyAdminStaff, person")
    List<PoliticalBodyAdminStaffSearchResult> searchPoliticalAdminStaffForAdmin(Long politicalBodyAdminId);

}
