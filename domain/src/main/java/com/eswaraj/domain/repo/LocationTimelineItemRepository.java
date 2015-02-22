package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.relationships.LocationTimelineItem;

public interface LocationTimelineItemRepository extends GraphRepository<LocationTimelineItem> {
	
    @Query("start location=node({0}), timelineItem=node({1}) match (location)-[locationTimelineItem:LC_TIMELINE]-(timelineItem) return locationTimelineItem")
    LocationTimelineItem getLocationTimelineItemRelation(Location location, TimelineItem timelineItem);

    @Query("start location=node({0}) match (location)-[locationTimelineItem:LC_TIMELINE]-(timelineItem) return locationTimelineItem")
    List<LocationTimelineItem> getAllLocationTimelineItemRelationOfLocation(Location location);

    @Query("start location=node({0}) match (location)-[locationTimelineItem:LC_TIMELINE]-(timelineItem) return timelineItem order by timelineItem.updateTime desc")
    List<TimelineItem> getAllTimelineItemOfLocation(Location location);

    @Query("start location=node({0}) match (location)-[locationTimelineItem:LC_TIMELINE]-(timelineItem) return timelineItem order by timelineItem.updateTime desc skip {1} limit {2}")
    List<TimelineItem> getPagedTimelineItemOfLocation(Long locationid, int start, int size);

    @Query("start timelineItem=node({0}) match (location)-[locationTimelineItem:LC_TIMELINE]-(timelineItem) return location")
    List<Location> getAllLocationOfTimelineItem(TimelineItem timelineItem);

    @Query("start timelineItem=node({0}) match (locationType)-[:OF_TYPE]-(location)-[locationTimelineItem:LC_TIMELINE]-(timelineItem) return location, locationType")
    EndResult<LocationSearchResult> getAllLocationSearchResultOfTimelineItem(TimelineItem timelineItem);

}
