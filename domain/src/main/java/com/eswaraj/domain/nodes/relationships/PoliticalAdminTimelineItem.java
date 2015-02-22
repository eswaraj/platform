package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.TimelineItem;

@RelationshipEntity(type = "PA_TIMELINE")
public class PoliticalAdminTimelineItem extends BaseRelationship {

    @StartNode
    PoliticalBodyAdmin politicalBodyAdmin;
    @EndNode
    TimelineItem timelineItem;

    public PoliticalAdminTimelineItem() {
    }

    public PoliticalAdminTimelineItem(PoliticalBodyAdmin politicalBodyAdmin, TimelineItem timelineItem) {
        this.politicalBodyAdmin = politicalBodyAdmin;
        this.timelineItem = timelineItem;
    }

    public PoliticalBodyAdmin getPoliticalBodyAdmin() {
        return politicalBodyAdmin;
    }

    public void setPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin) {
        this.politicalBodyAdmin = politicalBodyAdmin;
    }

    public TimelineItem getTimelineItem() {
        return timelineItem;
    }

    public void setTimelineItem(TimelineItem timelineItem) {
        this.timelineItem = timelineItem;
    }
	
}
