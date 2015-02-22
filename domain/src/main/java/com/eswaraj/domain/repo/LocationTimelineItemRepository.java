package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.relationships.LocationTimelineItem;

public interface LocationTimelineItemRepository extends GraphRepository<LocationTimelineItem> {
	
    @Query("start location=node({0}), timelineItem=node({1}) match (location)-[locationTimelineItem:LC_TIMEINE]-(timelineItem) return locationTimelineItem")
    LocationTimelineItem getLocationTimelineItemRelation(Location location, TimelineItem timelineItem);

    @Query("start location=node({0}) match (location)-[locationTimelineItem:LC_TIMEINE]-(timelineItem) return locationTimelineItem")
    List<LocationTimelineItem> getAllLocationTimelineItemRelationOfLocation(Location location);

    @Query("start location=node({0}) match (location)-[locationTimelineItem:LC_TIMEINE]-(timelineItem) return timelineItem order by updateTime desc")
    List<TimelineItem> getAllTimelineItemOfLocation(Location location);

    @Query("start timelineItem=node({0}) match (location)-[locationTimelineItem:LC_TIMEINE]-(timelineItem) return location")
    List<Location> getAllLocationOfTimelineItem(TimelineItem timelineItem);

}
