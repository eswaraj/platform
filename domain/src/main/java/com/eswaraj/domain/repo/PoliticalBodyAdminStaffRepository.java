package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;

public interface PoliticalBodyAdminStaffRepository extends GraphRepository<PoliticalBodyAdminStaff> {
	
    @Query("start politicalBodyAdmin=node({0})  match (politicalBodyAdminStaff)-[:WORKING_FOR]->politicalBodyAdmin return politicalBodyAdminStaff")
    Collection<PoliticalBodyAdminStaff> getAllPoliticalAdminStaffByPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin);

}
