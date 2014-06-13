package com.eswaraj.web.dto;


/**
 * Represents type of the political body
 * @author ravi
 * @data Jun 13, 2014
 */
public class PoliticalBodyTypeDto extends BaseDto{
	private static final long serialVersionUID = 1L;
	private String shortName;
	private String name;
	private String description;
	private Long locationTypeId;
	
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
	public Long getLocationTypeId() {
		return locationTypeId;
	}
	public void setLocationTypeId(Long locationTypeId) {
		this.locationTypeId = locationTypeId;
	}
	@Override
	public String toString() {
		return "PoliticalBodyTypeDto [shortName=" + shortName + ", name=" + name + ", description=" + description + ", locationTypeId=" + locationTypeId
				+ ", id=" + id + "]";
	}
	
}
