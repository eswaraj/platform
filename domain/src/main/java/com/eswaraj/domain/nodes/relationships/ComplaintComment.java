package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Comment;
import com.eswaraj.domain.nodes.Complaint;

@RelationshipEntity(type = "COMPLAINT_COMMENT")
public class ComplaintComment extends BaseRelationship {

	@StartNode Complaint complaint;
	@EndNode Comment comment;

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
	

}
