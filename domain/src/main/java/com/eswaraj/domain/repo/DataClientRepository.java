package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.DataClient;

/**
 * Repo for location queries
 * @author ravi
 * @data May 30, 2014
 */
public interface DataClientRepository extends GraphRepository<DataClient>{
	
	//@Query("start dataClient=node:DataClient(name={0}) return dataClient")
	//@Query("MATCH (n:DataClient { name: {0}}) RETURN n")
	@Query("MATCH (n) where n.name={0} RETURN n")
    public DataClient getDataClientByName(String name);
	
}
