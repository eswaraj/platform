package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.relationships.ComplaintPhoto;

/**
 * Repo for User Device
 * 
 * @author ravi
 * @data Sep 15, 2014
 */
public interface ComplaintPhotoRepository extends GraphRepository<ComplaintPhoto> {
	
    // @Query("start complaint=node({0}), politicalBodyAdmin=node({1}) match (complaint)-[complaintPoliticalAdmin:POLITICAL_SERVED_BY]->(politicalBodyAdmin) return complaintPoliticalAdmin")
    // ComplaintPoliticalAdmin getComplaintPoliticalAdminRelation(Complaint
    // complaint, PoliticalBodyAdmin politicalBodyAdmin);

}
