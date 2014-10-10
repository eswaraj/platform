package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;

/**
 * Repo for User Device
 * 
 * @author ravi
 * @data Sep 15, 2014
 */
public interface ComplaintLoggedByPersonRepository extends GraphRepository<ComplaintLoggedByPerson> {
	
    @Query("start complaint=node({0}), person=node({1}) match (complaint)-[complaintLoggedByPerson:LODGED_BY]->(person) return complaintLoggedByPerson")
    ComplaintLoggedByPerson getComplaintLoggedByPersonRelation(Complaint complaint, Person person);

}
