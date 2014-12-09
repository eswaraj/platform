package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.PoliticalBodyType;

public interface PoliticalBodyTypeRepository extends GraphRepository<PoliticalBodyType>{
	
    @Query("match politicalBodyType where (politicalBodyType.__type__ = 'com.eswaraj.domain.nodes.PoliticalBodyType' or politicalBodyType.__type__ = 'PoliticalBodyType') return politicalBodyType order by politicalBodyType.name ASC ")
    public List<PoliticalBodyType> getAllPoliticalBodyTypes();

    @Query("start location=node({0}) match (location)-[:OF_TYPE]->(locationType)-[]-(politicalBodyType)  where (politicalBodyType.__type__ = 'com.eswaraj.domain.nodes.PoliticalBodyType' or politicalBodyType.__type__ = 'PoliticalBodyType') return politicalBodyType order by politicalBodyType.name ASC ")
    public List<PoliticalBodyType> getAllPoliticalBodyTypesOfLocation(Long locationId);

}
