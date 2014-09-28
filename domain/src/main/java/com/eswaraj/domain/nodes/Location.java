package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import com.eswaraj.domain.base.BaseNode;

/**
 * Location of the complaint
 * @author anuj
 * @date Jan 28, 2014
 *
 */
@NodeEntity
public class Location extends BaseNode {

    @Indexed(indexName = "LocationNameFt", indexType = IndexType.FULLTEXT)
	private String name;
	
	@RelatedTo(type="OF_TYPE")
    @Fetch
	private LocationType locationType;
	
	@RelatedTo(type="PART_OF")
	private Location parentLocation;
	
    private String urlIdentifier;

    private String boundaryFile;

	private Double latitude;
	
	private Double longitude;
	
	private Long totalNumberOfHouses;
	
	private Long totalPopulation;
	
	private Long totalMalePopulation;
	
	private Long totalFemalePopulation;
	
	private Long totalLiteratePopulation;
	
	private Long totalMaleLiteratePopulation;
	
	private Long totalFemaleLiteratePopulation;
	
	private Long totalWorkingPopulation;
	
	private Long totalMaleWorkingPopulation;
	
	private Long totalFemaleWorkingPopulation;
	
    private Double area;

    private Double perimeter;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public Location getParentLocation() {
		return parentLocation;
	}

	public void setParentLocation(Location parentLocation) {
		this.parentLocation = parentLocation;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Long getTotalNumberOfHouses() {
		return totalNumberOfHouses;
	}

	public void setTotalNumberOfHouses(Long totalNumberOfHouses) {
		this.totalNumberOfHouses = totalNumberOfHouses;
	}

	public Long getTotalPopulation() {
		return totalPopulation;
	}

	public void setTotalPopulation(Long totalPopulation) {
		this.totalPopulation = totalPopulation;
	}

	public Long getTotalMalePopulation() {
		return totalMalePopulation;
	}

	public void setTotalMalePopulation(Long totalMalePopulation) {
		this.totalMalePopulation = totalMalePopulation;
	}

	public Long getTotalFemalePopulation() {
		return totalFemalePopulation;
	}

	public void setTotalFemalePopulation(Long totalFemalePopulation) {
		this.totalFemalePopulation = totalFemalePopulation;
	}

	public Long getTotalLiteratePopulation() {
		return totalLiteratePopulation;
	}

	public void setTotalLiteratePopulation(Long totalLiteratePopulation) {
		this.totalLiteratePopulation = totalLiteratePopulation;
	}

	public Long getTotalMaleLiteratePopulation() {
		return totalMaleLiteratePopulation;
	}

	public void setTotalMaleLiteratePopulation(Long totalMaleLiteratePopulation) {
		this.totalMaleLiteratePopulation = totalMaleLiteratePopulation;
	}

	public Long getTotalFemaleLiteratePopulation() {
		return totalFemaleLiteratePopulation;
	}

	public void setTotalFemaleLiteratePopulation(Long totalFemaleLiteratePopulation) {
		this.totalFemaleLiteratePopulation = totalFemaleLiteratePopulation;
	}

	public Long getTotalWorkingPopulation() {
		return totalWorkingPopulation;
	}

	public void setTotalWorkingPopulation(Long totalWorkingPopulation) {
		this.totalWorkingPopulation = totalWorkingPopulation;
	}

	public Long getTotalMaleWorkingPopulation() {
		return totalMaleWorkingPopulation;
	}

	public void setTotalMaleWorkingPopulation(Long totalMaleWorkingPopulation) {
		this.totalMaleWorkingPopulation = totalMaleWorkingPopulation;
	}

	public Long getTotalFemaleWorkingPopulation() {
		return totalFemaleWorkingPopulation;
	}

	public void setTotalFemaleWorkingPopulation(Long totalFemaleWorkingPopulation) {
		this.totalFemaleWorkingPopulation = totalFemaleWorkingPopulation;
	}

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Double getPerimeter() {
        return perimeter;
    }

    public void setPerimeter(Double perimeter) {
        this.perimeter = perimeter;
    }

    public String getBoundaryFile() {
        return boundaryFile;
    }

    public void setBoundaryFile(String boundaryFile) {
        this.boundaryFile = boundaryFile;
    }

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(String urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }

    @Override
    public String toString() {
        return "Location [name=" + name + ", locationType=" + locationType + ", parentLocation=" + parentLocation + ", urlIdentifier=" + urlIdentifier + ", boundaryFile=" + boundaryFile
                + ", latitude=" + latitude + ", longitude=" + longitude + ", totalNumberOfHouses=" + totalNumberOfHouses + ", totalPopulation=" + totalPopulation + ", totalMalePopulation="
                + totalMalePopulation + ", totalFemalePopulation=" + totalFemalePopulation + ", totalLiteratePopulation=" + totalLiteratePopulation + ", totalMaleLiteratePopulation="
                + totalMaleLiteratePopulation + ", totalFemaleLiteratePopulation=" + totalFemaleLiteratePopulation + ", totalWorkingPopulation=" + totalWorkingPopulation
                + ", totalMaleWorkingPopulation=" + totalMaleWorkingPopulation + ", totalFemaleWorkingPopulation=" + totalFemaleWorkingPopulation + ", area=" + area + ", perimeter=" + perimeter
                + ", id=" + id + "]";
    }

	
}
