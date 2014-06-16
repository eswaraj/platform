package com.eswaraj.domain.repo;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Person;

public interface PersonRepository extends GraphRepository<Person>{
	
	@Query("start n=node:PersonNameFt({0}) return n")
	//@Query("start n = node(*) where n.name =~ '.*avi.*' return n")
	//@Query("start n=node:PersonNameFt(name:*avi*) return n")
	Collection<Person> searchPersonByName(String name);
}
