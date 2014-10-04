package com.eswaraj.domain.nodes;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

@NodeEntity
public class Address extends BaseNode {

	private String line1;
	private String line2;
	private String line3;
	private String postalCode;
    private Double lattitude;
    private Double longitude;
    @RelatedTo(elementClass = Location.class, direction = Direction.BOTH)
    @Fetch
    Set<Location> locations = new HashSet<>();

    public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this.line2 = line2;
	}
	public String getLine3() {
		return line3;
	}
	public void setLine3(String line3) {
		this.line3 = line3;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Address [line1=" + line1 + ", line2=" + line2 + ", line3=" + line3 + ", postalCode=" + postalCode + ", lattitude=" + lattitude + ", longitude=" + longitude + ", locations="
                + locations + ", id=" + id + ", externalId=" + externalId + "]";
    }

}
