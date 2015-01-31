package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;

public interface ElectionManifestoPointRepository extends GraphRepository<ElectionManifestoPromise> {
	
}
