package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.User;

public interface PersonRepository extends GraphRepository<Person>{
	
    @Query("start n=node:PersonNameFt({0}) return n limit 10")
	//@Query("start n = node(*) where n.name =~ '.*avi.*' return n")
	//@Query("start n=node:PersonNameFt(name:*avi*) return n")
    List<Person> searchPersonByName(String name);

    @Query("start user=node({0}) match (user)-[:ATTACHED_TO]->(person) where person.__type__ = 'Person' return person")
    public Person getPersonByUser(User user);

    @Query("start complaint=node({0}) match (complaint)-[:LODGED_BY]->(person) where person.__type__ = 'Person' return person")
    public Collection<Person> getPersonsLoggedComplaint(Complaint complaint);

    @Query("start complaint=node({0}) match (complaint)-[:LODGED_BY]->(person) where person.__type__ = 'Person' return person")
    public List<Person> getPersonsLoggedComplaint(Long complaintId);

    @Query("match (person)-[:IS]-(admin) where person.__type__ = 'Person'  and admin.__type__='PoliticalBodyAdmin' return person")
    public List<Person> getAllAdminPersons();

    @Query("match (person) where person.__type__ = 'Person' and person.name={0} return person")
    public List<Person> findPersonsByName(String name);

    @Query("start department=node({0}) match (department)-[:WORKS_FOR]-(departmentAdmin)-[:IS]-(person) where person.__type__ = 'Person'  and departmentAdmin.__type__='DepartmentAdmin' return person")
    public List<Person> getDepartmentStaffMembers(Department department);

}
