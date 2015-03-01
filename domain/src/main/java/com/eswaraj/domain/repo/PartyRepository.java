package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Party;

public interface PartyRepository extends GraphRepository<Party>{
	
	public Party getById(Long id);

    @Query("match party where party.__type__ = 'Party' return party order by party.name ASC ")
    public List<Party> getAllParties();

    @Query("match party where party.__type__ = 'Party' and party.name={0} return party")
    public Party findPartyByName(String name);

}
