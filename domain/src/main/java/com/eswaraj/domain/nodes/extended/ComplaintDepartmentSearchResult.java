package com.eswaraj.domain.nodes.extended;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Complaint;

@QueryResult
public interface ComplaintDepartmentSearchResult {

    @ResultColumn("complaint")
    Complaint getComplaint();

}

