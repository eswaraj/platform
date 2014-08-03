package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.User;

public interface PersonRepository extends GraphRepository<Person>{
	
	@Query("start n=node:PersonNameFt({0}) return n")
	//@Query("start n = node(*) where n.name =~ '.*avi.*' return n")
	//@Query("start n=node:PersonNameFt(name:*avi*) return n")
	Collection<Person> searchPersonByName(String name);

    @Query("start user=node({0}) match (user)-[:ATTACHED_TO]->(person) where person.__type__ = 'com.eswaraj.domain.nodes.Person' return person")
    public Person getPersonByUser(User user);

}
