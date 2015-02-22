package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.TimelineItem;

@RelationshipEntity(type = "LC_TIMELINE")
public class LocationTimelineItem extends BaseRelationship {

    @StartNode
    Location location;
    @EndNode
    TimelineItem timelineItem;

    public LocationTimelineItem() {
    }

    public LocationTimelineItem(Location location, TimelineItem timelineItem) {
        this.location = location;
        this.timelineItem = timelineItem;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TimelineItem getTimelineItem() {
        return timelineItem;
    }

    public void setTimelineItem(TimelineItem timelineItem) {
        this.timelineItem = timelineItem;
    }
	
}
