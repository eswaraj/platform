package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Person;

/**
 * 
 * @author ravisharma
 *
 */
@RelationshipEntity(type = "ENDORSED_BY")
public class ComplaintEndorsedBy extends BaseRelationship {

    @StartNode
    Complaint complaint;
    @EndNode
    Person person;

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

    @Override
    public String toString() {
        return "ComplaintEndorsedBy [complaint=" + complaint + ", person=" + person + ", id=" + id + "]";
    }
	

}
