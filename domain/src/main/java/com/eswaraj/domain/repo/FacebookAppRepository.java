package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.FacebookApp;

public interface FacebookAppRepository extends GraphRepository<FacebookApp> {
	
    @Query("start n=node:FacebookAppIdIdx({0}) return n")
    public FacebookApp getFacebookAppByFacebookAppId(String facebookAppId);

}
