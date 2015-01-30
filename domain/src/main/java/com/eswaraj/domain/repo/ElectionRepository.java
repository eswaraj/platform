package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Election;
import com.eswaraj.domain.nodes.PoliticalBodyType;

public interface ElectionRepository extends GraphRepository<Election> {

    @Query("start politicalBodyType=node({0}) match (politicalBodyType)-[:ELECTED_VIA]-(electionType)-[:OF_ELECTION_TYPE]-(elections) return elections")
    public List<Election> findAllElectionsOfPoliticalBodyType(PoliticalBodyType politicalBodyType);

}
