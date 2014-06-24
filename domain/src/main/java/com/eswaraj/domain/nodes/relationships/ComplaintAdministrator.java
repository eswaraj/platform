package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.nodes.Complaint;

@RelationshipEntity(type="SERVED_BY")
public class ComplaintAdministrator {

	@StartNode Complaint complaint;
	
	public Complaint getComplaint() {
		return complaint;
	}
	public void setComplaint(Complaint complaint) {
		this.complaint = complaint;
	}
}
