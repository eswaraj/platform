package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ExecutivePost;

public interface PostRepository extends GraphRepository<ExecutivePost>{

}
