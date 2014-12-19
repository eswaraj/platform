package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Comment;

public interface CommentRepository extends GraphRepository<Comment> {
	
    @Query("start complaint=node({0}) match (complaint)-[:COMPLAINT_COMMENT]->(comment) return comment")
    public List<Comment> findAllCommentsByComplaintId(Long complaintId);

}
