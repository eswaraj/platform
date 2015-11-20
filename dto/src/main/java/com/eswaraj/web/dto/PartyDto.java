package com.eswaraj.web.dto;


/**
 * Represents a political party
 * @author ravi
 * @date Jun 15, 2014
 *
 */

public class PartyDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	private String name;
	private String shortName;
    private String imageUrl;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
