package com.eswaraj.domain.repo;

import java.util.Collection;
import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;

public interface ElectionManifestoPromiseRepository extends GraphRepository<ElectionManifestoPromise> {
	
    @Query("start electionManifesto=node({0}) match (electionManifesto)<-[:OF_ELECTION_MANIFESTO]-(electionManifestoPromise) where electionManifestoPromise.__type__ = 'ElectionManifestoPromise' return electionManifestoPromise")
    public List<ElectionManifestoPromise> getAllPromisesOfManifesto(Long electionManifestoId);

    @Query("start politicalAdmin=node({0}) match (politicalAdmin)-[:OF]-(party), (politicalAdmin)-[:ELECTED_BY]-(election) with party,election,politicalAdmin match (election)-[:OF_ELECTION]-(electionManifesto)-[:OF_PARTY]-(party) with  party,election,politicalAdmin,electionManifesto match (electionManifesto)-[:OF_ELECTION_MANIFESTO]-(electionPromises) return electionPromises;")
    public List<ElectionManifestoPromise> getAllPromisesOfPoliticalAdmin(Long electionManifestoId);

    @Query("start politicalAdmin=node({0}) match (politicalAdmin)-[:OF]-(party), (politicalAdmin)-[:ELECTED_BY]-(election) with party,election,politicalAdmin match (election)-[:OF_ELECTION]-(electionManifesto)-[:OF_PARTY]-(party) with  party,election,politicalAdmin,electionManifesto match (electionManifesto)-[:OF_ELECTION_MANIFESTO]-(electionPromises) return electionPromises;")
    public List<ElectionManifestoPromise> getAllPromisesOfPoliticalAdmin(Collection<Long> electionManifestoIds);

}
