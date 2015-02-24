package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.FacebookAccount;
import com.eswaraj.domain.nodes.FacebookApp;
import com.eswaraj.domain.nodes.relationships.FacebookAppPermission;

public interface FacebookAppPermissionRepository extends GraphRepository<FacebookAppPermission> {
	
    @Query("start FacebookAccount=node({0}), facebookApp=node({1}) match (FacebookAccount)-[facebookAppPermission]-(facebookApp) return facebookAppPermission")
    FacebookAppPermission getFacebookAccountAndAppRelation(FacebookAccount facebookAccount, FacebookApp facebookApp);

    @Query("start FacebookAccount=node({0}) match (FacebookAccount)-[facebookAppPermission]-(facebookApp) return facebookAppPermission")
    List<FacebookAppPermission> getFacebookAppPermissionByFacebookAccount(FacebookAccount facebookAccount);

}
