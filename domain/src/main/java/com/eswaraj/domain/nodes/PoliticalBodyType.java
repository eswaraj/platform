package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Represents type of the political body
 * @author ravi
 * @data Jun 13, 2014
 */
@NodeEntity
@TypeAlias("PoliticalBodyType")
public class PoliticalBodyType extends BaseNode{
	private String shortName;
	private String name;
	private String description;
    @Fetch
	private LocationType locationType;

    @RelatedTo(type = "ELECTED_VIA")
    private ElectionType electionType;
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocationType getLocationType() {
		return locationType;
	}
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

    public ElectionType getElectionType() {
        return electionType;
    }

    public void setElectionType(ElectionType electionType) {
        this.electionType = electionType;
    }

    @Override
	public String toString() {
		return "PoliticalBodyType [shortName=" + shortName + ", name=" + name + ", description=" + description + ", locationType=" + locationType + ", id="
				+ id + "]";
	}
}
