package com.eswaraj.domain.nodes.extended;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;
import com.eswaraj.domain.nodes.relationships.ComplaintPhoto;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;

@QueryResult
public interface ComplaintSearchResult {

    @ResultColumn("complaint")
    Complaint getComplaint();

    @ResultColumn("location")
    Location getLocation();

    @ResultColumn("complaintPoliticalAdmin")
    ComplaintPoliticalAdmin getComplaintPoliticalAdmin();

    @ResultColumn("complaintLoggedByPerson")
    List<ComplaintLoggedByPerson> getComplaintLoggedByPerson();

    @ResultColumn("complaintPhoto")
    List<ComplaintPhoto> getComplaintPhoto();

}
