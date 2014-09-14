package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.FacebookAccount;

public interface FacebookAccountRepository extends GraphRepository<FacebookAccount> {
	

}
