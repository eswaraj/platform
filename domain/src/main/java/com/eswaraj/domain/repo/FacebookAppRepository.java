package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.FacebookApp;

public interface FacebookAppRepository extends GraphRepository<FacebookApp> {
	
    // @Query("start n=node:FacebookAppIdIdx({0}) return n")
    // @Query("start facebookApp=node:FacebookApp(appId={0}) return facebookApp")
    // public FacebookApp getFacebookAppByFacebookAppId(String facebookAppId);

}
