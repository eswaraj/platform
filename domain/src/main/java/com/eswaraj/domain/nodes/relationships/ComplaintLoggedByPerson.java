package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;

@RelationshipEntity(type="LODGED_BY")
public class ComplaintLoggedByPerson extends BaseRelationship {

	@StartNode Complaint complaint;
    @EndNode
    @Fetch
    Person person;
    private boolean anonymous;
	
	public Complaint getComplaint() {
		return complaint;
	}
	public void setComplaint(Complaint complaint) {
		this.complaint = complaint;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
    @Override
    public String toString() {
        return "ComplaintLoggedByPerson [complaint=" + complaint + ", person=" + person + ", id=" + id + "]";
    }

}
