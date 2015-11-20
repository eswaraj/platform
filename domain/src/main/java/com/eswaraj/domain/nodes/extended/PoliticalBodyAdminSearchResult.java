package com.eswaraj.domain.nodes.extended;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;

@QueryResult
public interface PoliticalBodyAdminSearchResult {

    @ResultColumn("politicalBodyAdmin")
    PoliticalBodyAdmin getPoliticalBodyAdmin();

    @ResultColumn("politicalBodyType")
    PoliticalBodyType getPoliticalBodyType();
    
    @ResultColumn("location")
    Location getLocation();

    @ResultColumn("locationType")
    LocationType getLocationType();

    @ResultColumn("person")
    Person getPerson();

    @ResultColumn("party")
    Party getParty();

}
