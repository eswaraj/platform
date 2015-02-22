package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.base.BaseRelationship;
import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.nodes.TimelineItem;

@RelationshipEntity(type = "PR_TIMEINE")
public class PromiseTimelineItem extends BaseRelationship {

    @StartNode
    ElectionManifestoPromise electionManifestoPromise;
    @EndNode
    TimelineItem timelineItem;

    public PromiseTimelineItem() {
    }

    public PromiseTimelineItem(ElectionManifestoPromise electionManifestoPromise, TimelineItem timelineItem) {
        this.electionManifestoPromise = electionManifestoPromise;
        this.timelineItem = timelineItem;
    }

    public ElectionManifestoPromise getElectionManifestoPromise() {
        return electionManifestoPromise;
    }

    public void setElectionManifestoPromise(ElectionManifestoPromise electionManifestoPromise) {
        this.electionManifestoPromise = electionManifestoPromise;
    }

    public TimelineItem getTimelineItem() {
        return timelineItem;
    }

    public void setTimelineItem(TimelineItem timelineItem) {
        this.timelineItem = timelineItem;
    }
	
}
