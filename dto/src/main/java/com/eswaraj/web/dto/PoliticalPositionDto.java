package com.eswaraj.web.dto;


/**
 * A political elected representative
 * 
 * @author ravi
 * @date Oct 09, 2014
 *
 */
public class PoliticalPositionDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;

    private String politicalBodyType;
    private String politicalBodyTypeShort;

    private String locationName;

    private String partyName;

    public String getPoliticalBodyType() {
        return politicalBodyType;
    }

    public void setPoliticalBodyType(String politicalBodyType) {
        this.politicalBodyType = politicalBodyType;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPoliticalBodyTypeShort() {
        return politicalBodyTypeShort;
    }

    public void setPoliticalBodyTypeShort(String politicalBodyTypeShort) {
        this.politicalBodyTypeShort = politicalBodyTypeShort;
    }
	

}
