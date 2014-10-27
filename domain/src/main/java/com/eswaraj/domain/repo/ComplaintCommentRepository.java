package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.relationships.ComplaintComment;

/**
 * Repository for ComplaintComment node
 * 
 * @author anuj
 * @date Jan 22, 2014
 *
 */
public interface ComplaintCommentRepository extends GraphRepository<ComplaintComment> {

}
