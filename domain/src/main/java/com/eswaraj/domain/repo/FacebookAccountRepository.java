package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.FacebookAccount;

public interface FacebookAccountRepository extends GraphRepository<FacebookAccount> {
	
    // @Query("start n=node:FacebookUserIdIdx({0}) return n")
    @Query("start Fa=node:FacebookAccount(facebookUserId={0}) return Fa")
    public FacebookAccount getFacebookAccountByFacebookUserId(String facebookUserId);

}
