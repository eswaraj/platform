package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.User;

/**
 * Repo for User
 * @author ravi
 * @data Jul 22, 2014
 */
public interface UserRepository extends GraphRepository<User>{
	
	@Query("start user=node:User(externalId={0}) return user")
    public User getUserByUserExternalId(String userExternalId);

    @Query("start person=node({0}) match (user)<-[:ATTACHED_TO]-(person) where user.__type__ = 'com.eswaraj.domain.nodes.User' return user")
    public User getUserByPerson(Person person);
}
