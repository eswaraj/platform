package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;

/**
 * Repository for Complaint node
 * @author anuj
 * @date Jan 22, 2014
 *
 */
public interface ComplaintRepository extends GraphRepository<Complaint>{

	@Query("start category=node({0}) match (complaint)-[:BELONGS_TO]->(category) return complaint ") 
	public List<Complaint> getByCategory(Category category);
	
	@Query("start person=node({0}) " +
			"match (person)<-[:LODGED_BY]-(complaint) return complaint " + 
			"order by complaint.dateCreated DESC")
	public List<Complaint> getAllComplaintsLodgedByPerson(Person person);
	
    @Query("start person=node({0}) match (person)<-[:LODGED_BY]-(complaint) return complaint " +
			"order by complaint.dateCreated DESC " +
			"skip {1} limit {2}")
	public List<Complaint> getPagedComplaintsLodgedByPerson(Person person, int start, int end);
	
	@Query("start location=node({0}) " +
			"match (location)<-[:LODGED_BY]-(complaint) return complaint " + 
			"order by complaint.dateCreated DESC")
	public List<Complaint> getAllComplaintsLodgedForLocation(Location location);
	
	@Query("start person=node({0}) " +
			"match (person)<-[:LODGED_BY]-(complaint) return complaint " +
			"order by complaint.dateCreated DESC " +
			"skip {1} limit {2}")
	public List<Complaint> getPagedComplaintsLodgedForLocation(Location location, int start, int end);
	
	
    @Query("match complaint where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' return complaint order by complaint.dateCreated ASC " + "skip {0} limit {1}")
    public List<Complaint> getAllPagedComplaints(long start, long end);

    @Query("start location=node({0}) match complaint-[:AT]-(location) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' return complaint order by complaint.dateCreated ASC "
            + "skip {1} limit {2}")
    public List<Complaint> getAllPagedComplaintsOfLocation(long locationId, long start, long end);

    @Query("start politicalAdmin=node({0}) match complaint-[:POLITICAL_SERVED_BY]->(politicalAdmin) where complaint.__type__ = 'com.eswaraj.domain.nodes.Complaint' return complaint order by complaint.dateCreated ASC "
            + "skip {1} limit {2}")
    public List<Complaint> getAllPagedComplaintsOfPoliticalAdmin(long politicalAdminId, long start, long end);
}
