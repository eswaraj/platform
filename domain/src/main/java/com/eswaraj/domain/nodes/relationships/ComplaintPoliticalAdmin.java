package com.eswaraj.domain.nodes.relationships;

import java.util.Date;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.PoliticalAdminComplaintStatus;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;

@RelationshipEntity(type = "POLITICAL_SERVED_BY")
public class ComplaintPoliticalAdmin extends BaseRelationship {

    @StartNode
    private Complaint complaint;
    @EndNode
    private PoliticalBodyAdmin politicalBodyAdmin;
    private PoliticalAdminComplaintStatus status;
    private boolean viewed;
    private Date viewDate;

    public Complaint getComplaint() {
        return complaint;
    }

    public void setComplaint(Complaint complaint) {
        this.complaint = complaint;
    }

    public PoliticalBodyAdmin getPoliticalBodyAdmin() {
        return politicalBodyAdmin;
    }

    public void setPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) {
        this.politicalBodyAdmin = politicalBodyAdmin;
    }

    public PoliticalAdminComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(PoliticalAdminComplaintStatus status) {
        this.status = status;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }

}
