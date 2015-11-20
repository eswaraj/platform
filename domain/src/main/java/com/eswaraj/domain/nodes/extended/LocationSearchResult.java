package com.eswaraj.domain.nodes.extended;

import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;

@QueryResult
public interface LocationSearchResult {

    @ResultColumn("location")
    Location getLocation();

    @ResultColumn("locationType")
    LocationType getLocationType();

}
