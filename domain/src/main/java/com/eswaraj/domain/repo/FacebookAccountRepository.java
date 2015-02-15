package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.User;

public interface FacebookAccountRepository extends GraphRepository<FacebookAccount> {
	
    @Query("start user=node({0}) match (user)-[:OF_USER]->(facebookAccount) where facebookAccount.__type__ = 'FacebookAccount' return facebookAccount")
    public FacebookAccount getFacebookAccountByUser(User user);

}
