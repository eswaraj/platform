package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;

public interface ElectionManifestoPromiseRepository extends GraphRepository<ElectionManifestoPromise> {
	
    @Query("start electionManifesto=node({0}) match (electionManifesto)<-[:OF_ELECTION_MANIFESTO]-(electionManifestoPromise) where electionManifestoPromise.__type__ = 'ElectionManifestoPromise' return electionManifestoPromise")
    public List<ElectionManifestoPromise> getAllPromisesOfManifesto(Long electionManifestoId);

}
