package com.eswaraj.domain.nodes.extended;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Complaint;
import com.eswaraj.domain.nodes.relationships.ComplaintPoliticalAdmin;

@QueryResult
public interface ComplaintSearchResult {

    @ResultColumn("complaint")
    Complaint getComplaint();

    @ResultColumn("complaintPoliticalAdmin")
    ComplaintPoliticalAdmin getComplaintPoliticalAdmin();

}

