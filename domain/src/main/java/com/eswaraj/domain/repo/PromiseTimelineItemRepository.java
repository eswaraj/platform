package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.relationships.PromiseTimelineItem;

public interface PromiseTimelineItemRepository extends GraphRepository<PromiseTimelineItem> {
	
    @Query("start electionManifestoPromise=node({0}), timelineItem=node({1}) match (electionManifestoPromise)-[promiseTimelineItem:LC_TIMEINE]-(timelineItem) return promiseTimelineItem")
    PromiseTimelineItem getPromiseTimelineItemRelation(ElectionManifestoPromise electionManifestoPromise, TimelineItem timelineItem);

    @Query("start electionManifestoPromise=node({0}) match (electionManifestoPromise)-[promiseTimelineItem:LC_TIMEINE]-(timelineItem) return promiseTimelineItem")
    List<PromiseTimelineItem> getAllPromiseTimelineItemRelationOfElectionManifestoPromise(ElectionManifestoPromise electionManifestoPromise);

    @Query("start electionManifestoPromise=node({0}) match (electionManifestoPromise)-[promiseTimelineItem:LC_TIMEINE]-(timelineItem) return timelineItem order by updateTime desc")
    List<TimelineItem> getAllTimelineItemOfElectionManifestoPromise(ElectionManifestoPromise electionManifestoPromise);

    @Query("start timelineItem=node({0}) match (electionManifestoPromise)-[promiseTimelineItem:LC_TIMEINE]-(timelineItem) return electionManifestoPromise")
    List<ElectionManifestoPromise> getAllElectionManifestoPromisesOfTimelineItem(TimelineItem timelineItem);

}
