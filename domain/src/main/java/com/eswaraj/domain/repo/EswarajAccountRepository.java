package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.EswarajAccount;
import com.eswaraj.domain.nodes.User;

public interface EswarajAccountRepository extends GraphRepository<EswarajAccount> {
	
    @Query("start user=node({0}) match (user)-[:OF_USER]-(eswarajAccount) where eswarajAccount.__type__ = 'EswarajAccount' return eswarajAccount")
    public EswarajAccount getEswarajAccountByUser(User user);

}
