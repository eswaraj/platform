package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Election;

public interface ElectionRepository extends GraphRepository<Election> {
	
}
