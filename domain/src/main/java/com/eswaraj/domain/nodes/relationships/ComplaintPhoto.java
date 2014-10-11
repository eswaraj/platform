package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Photo;

@RelationshipEntity(type = "PHOTOS_OF_COMPLAINT")
public class ComplaintPhoto {

	@StartNode Complaint complaint;
	@EndNode Photo photo;
    public Complaint getComplaint() {
        return complaint;
    }
    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }


}
