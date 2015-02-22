package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.domain.nodes.relationships.PoliticalAdminTimelineItem;

public interface PoliticalAdminTimelineItemRepository extends GraphRepository<PoliticalAdminTimelineItem> {
	
    @Query("start politicalBodyAdmin=node({0}), timelineItem=node({1}) match (politicalBodyAdmin)-[politicalAdminTimelineItem:PA_TIMEINE]-(timelineItem) return politicalAdminTimelineItem")
    PoliticalAdminTimelineItem getPoliticalAdminTimelineItemRelation(PoliticalBodyAdmin politicalBodyAdmin, TimelineItem timelineItem);

    @Query("start politicalBodyAdmin=node({0}) match (politicalBodyAdmin)-[politicalAdminTimelineItem:PA_TIMEINE]-(timelineItem) return politicalAdminTimelineItem")
    List<PoliticalAdminTimelineItem> getAllPoliticalAdminTimelineItemRelationOfPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin);

    @Query("start politicalBodyAdmin=node({0}) match (politicalBodyAdmin)-[politicalAdminTimelineItem:PA_TIMEINE]-(timelineItem) return timelineItem order by updateTime desc")
    List<TimelineItem> getAllTimelineItemOfPoliticalBodyAdmin(PoliticalBodyAdmin politicalBodyAdmin);

    @Query("start timelineItem=node({0}) match (politicalBodyAdmin)-[politicalAdminTimelineItem:PA_TIMEINE]-(timelineItem) return politicalBodyAdmin")
    List<PoliticalBodyAdmin> getAllPoliticalBodyAdminOfTimelineItem(TimelineItem timelineItem);

    @Query("start timeline=node({0}) match (timeline)-[:PA_TIMEINE]-(politicalBodyAdmin)-[:IS]-(person) where politicalBodyAdmin.__type__='PoliticalBodyAdmin' and politicalBodyAdmin.active=true with politicalBodyAdmin, person  match (location)-[:BELONGS_TO]-(politicalBodyAdmin)-[:OF_TYPE]->(politicalBodyType) with politicalBodyAdmin, location,  politicalBodyType,person  optional match (party)-[:OF]-(politicalBodyAdmin)  return politicalBodyAdmin, politicalBodyType, location, person, party")
    EndResult<PoliticalBodyAdminSearchResult> getAllPoliticalBodyAdminSearchResultOfTimelineItem(TimelineItem timelineItem);

}
