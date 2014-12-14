package com.eswaraj.domain.nodes.extended;

import java.util.List;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.relationships.ComplaintLoggedByPerson;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;

@QueryResult
public interface ComplaintSearchResult {

    @ResultColumn("complaint")
    Complaint getComplaint();

    @ResultColumn("location")
    List<Location> getLocation();

    @ResultColumn("complaintPoliticalAdmin")
    List<ComplaintPoliticalAdmin> getComplaintPoliticalAdmin();

    @ResultColumn("complaintLoggedByPerson")
    List<ComplaintLoggedByPerson> getComplaintLoggedByPerson();

    @ResultColumn("photo")
    List<Photo> getComplaintPhoto();

}
