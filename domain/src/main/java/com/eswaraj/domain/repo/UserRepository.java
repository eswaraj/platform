package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.User;

/**
 * Repo for User
 * @author ravi
 * @data Jul 22, 2014
 */
public interface UserRepository extends GraphRepository<User>{
	
    @Query("start person=node({0}) match (user)-[:ATTACHED_TO]->(person) where user.__type__ = 'com.eswaraj.domain.nodes.User' return user")
    public User getUserByPerson(Person person);

    @Query("start device=node({0}) match (user)-[:USER_DEVICE]->(device) where user.__type__ = 'com.eswaraj.domain.nodes.User' return user")
    public List<User> getUserByDevice(Device device);

    // @Query("start facebookAccount=node:FacebookUserIdIdx({0}) match (user)<-[:OF_USER]-(facebookAccount) where user.__type__ = 'User' or user.__type__ = 'com.eswaraj.domain.nodes.User' return user")
    // public User getUserByFacebookUserId(String facebookUserId);

    @Query("start facebookAccount=node({0}) match (user)<-[:OF_USER]-(facebookAccount) where user.__type__ = 'User' or user.__type__ = 'com.eswaraj.domain.nodes.User' return user")
    public User getUserByFacebookUser(FacebookAccount facebookAccount);
}
