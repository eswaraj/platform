package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.relationships.ComplaintEndorsedBy;

/**
 * Repo for User Device
 * 
 * @author ravi
 * @date Oct 14, 2014
 */
public interface ComplaintEndorsedByRepository extends GraphRepository<ComplaintEndorsedBy> {
	
    @Query("start complaint=node({0}), person=node({1}) match (complaint)-[complaintEndorsedBy:ENDORSED_BY]->(person) return complaintEndorsedBy")
    ComplaintEndorsedBy getComplaintEndorsedByRelation(Complaint complaint, Person person);

}
