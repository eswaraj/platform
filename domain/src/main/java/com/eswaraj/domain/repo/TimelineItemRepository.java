package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.TimelineItem;

public interface TimelineItemRepository extends GraphRepository<TimelineItem> {

    @Query("match timelineItem where timelineItem.__type__='TimelineItem' return timelineItem order by timelineItem.updateTime skip {0} limit {1} desc")
    public List<TimelineItem> getTimelineItems(int first, int size);

}
