package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.LeaderTempFacebookAccount;
import com.eswaraj.domain.nodes.Person;

public interface LeaderTempFacebookAccountRepository extends GraphRepository<LeaderTempFacebookAccount> {
	
    @Query("start person=node({0}) match (person)-[:OF_PERSON]-(leaderTempFacebookAccount) where leaderTempFacebookAccount.__type__ = 'LeaderTempFacebookAccount' return leaderTempFacebookAccount")
    public LeaderTempFacebookAccount getLeaderTempFacebookAccountByPerson(Person person);

}
