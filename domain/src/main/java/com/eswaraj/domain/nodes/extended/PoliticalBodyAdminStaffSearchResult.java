package com.eswaraj.domain.nodes.extended;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;

@QueryResult
public interface PoliticalBodyAdminStaffSearchResult {

    @ResultColumn("politicalBodyAdminStaff")
    PoliticalBodyAdminStaff getPoliticalBodyAdminStaff();

    @ResultColumn("person")
    Person getPerson();

}
