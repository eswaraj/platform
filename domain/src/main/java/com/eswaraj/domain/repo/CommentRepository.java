package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;

public interface CommentRepository extends GraphRepository<Comment> {
	
    @Query("start complaint=node({0}) match (complaint)-[:COMPLAINT_COMMENT]->(comment) return comment")
    public List<Comment> findAllCommentsByComplaintId(Long complaintId);

    @Query("start complaint=node({0}) match (complaint)-[:COMPLAINT_COMMENT]->(comment) return count(comment)")
    public int findTotalCommentsByComplaintId(Long complaintId);

    @Query("start complaint=node({0}) match (complaint)-[:COMPLAINT_COMMENT]->(comment) return count(comment)")
    public int findTotalCommentsByComplaint(Complaint complaint);

}
