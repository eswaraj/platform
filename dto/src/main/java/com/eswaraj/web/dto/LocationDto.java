package com.eswaraj.web.dto;


public class LocationDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	private String name;
	private Long parentLocationId;
	private Long locationTypeId;
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
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getParentLocationId() {
		return parentLocationId;
	}
	public void setParentLocationId(Long parentLocationId) {
		this.parentLocationId = parentLocationId;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Long getLocationTypeId() {
		return locationTypeId;
	}
	public void setLocationTypeId(Long locationTypeId) {
		this.locationTypeId = locationTypeId;
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
	@Override
	public String toString() {
		return "LocationDto [name=" + name + ", parentLocationId=" + parentLocationId + ", locationTypeId=" + locationTypeId + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", getId()=" + getId() + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result + ((locationTypeId == null) ? 0 : locationTypeId.hashCode());
		result = prime * result + ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parentLocationId == null) ? 0 : parentLocationId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationDto other = (LocationDto) obj;
		if (latitude == null) {
			if (other.latitude != null)
				return false;
		} else if (!latitude.equals(other.latitude))
			return false;
		if (locationTypeId == null) {
			if (other.locationTypeId != null)
				return false;
		} else if (!locationTypeId.equals(other.locationTypeId))
			return false;
		if (longitude == null) {
			if (other.longitude != null)
				return false;
		} else if (!longitude.equals(other.longitude))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentLocationId == null) {
			if (other.parentLocationId != null)
				return false;
		} else if (!parentLocationId.equals(other.parentLocationId))
			return false;
		return true;
	}

	
}
