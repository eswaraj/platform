package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;

public interface ElectionManifestoPromiseRepository extends GraphRepository<ElectionManifestoPromise> {
	
    @Query("start electionManifesto=node({0}) match (category)<-[:BELONGS_TO]-(childCategory) where childCategory.__type__ = 'com.eswaraj.domain.nodes.Category' or childCategory.__type__ = 'Category' return childCategory")
    public List<ElectionManifestoPromise> getAllPromisesOfManifesto(Long electionManifestoId);

}
