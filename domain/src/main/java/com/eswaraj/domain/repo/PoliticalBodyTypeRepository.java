package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.PoliticalBodyType;

public interface PoliticalBodyTypeRepository extends GraphRepository<PoliticalBodyType>{
	
    @Query("match politicalBodyType where politicalBodyType.__type__ = 'PoliticalBodyType' return politicalBodyType order by politicalBodyType.name ASC ")
    public List<PoliticalBodyType> getAllPoliticalBodyTypes();

    @Query("start location=node({0}) match (location)-[:OF_TYPE]->(locationType)-[]-(politicalBodyType)  where politicalBodyType.__type__ = 'PoliticalBodyType' return politicalBodyType order by politicalBodyType.name ASC ")
    public List<PoliticalBodyType> getAllPoliticalBodyTypesOfLocation(Long locationId);

    @Query("match politicalBodyType where politicalBodyType.__type__ = 'PoliticalBodyType' and politicalBodyType.name=~{0}  return politicalBodyType")
    public PoliticalBodyType findByName(String name);

    @Query("match politicalBodyType where politicalBodyType.__type__ = 'PoliticalBodyType' and politicalBodyType.shortName=~{0}  return politicalBodyType")
    public PoliticalBodyType findByShortName(String name);

}
