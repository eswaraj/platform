package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;

/**
 * Repo for User Device
 * 
 * @author ravi
 * @data Sep 15, 2014
 */
public interface ComplaintPoliticalAdminRepository extends GraphRepository<ComplaintPoliticalAdmin> {
	
    @Query("start complaint=node({0}), politicalBodyAdmin=node({1}) match (complaint)-[complaintPoliticalAdmin]-(politicalBodyAdmin) return complaintPoliticalAdmin")
    ComplaintPoliticalAdmin getComplaintPoliticalAdminRelation(Complaint complaint, PoliticalBodyAdmin politicalBodyAdmin);

}
