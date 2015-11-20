package com.eswaraj.web.dto;

import java.util.Date;

/**
 * A political elected representative
 * @author ravi
 * @date Jun 14, 2014
 *
 */
public class PoliticalBodyAdminDto extends BaseDto {
	
	private static final long serialVersionUID = 1L;

	private Long politicalBodyTypeId;

	private Long locationId;

	private Long personId;

	private Long partyId;

	private AddressDto officeAddressDto;
	private AddressDto homeAddressDto;
	
	private String email;
	
	private String landLine1;
	private String landLine2;
	private String mobile1;
	private String mobile2;
	private Date startDate;
	private Date endDate;
	private boolean active;
    private String fbPage;
    private String fbAccount;
    private String twitterHandle;
    private String urlIdentifier;
	
	public Long getPoliticalBodyTypeId() {
		return politicalBodyTypeId;
	}
	public void setPoliticalBodyTypeId(Long politicalBodyTypeId) {
		this.politicalBodyTypeId = politicalBodyTypeId;
	}
	public Long getLocationId() {
		return locationId;
	}
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public Long getPartyId() {
		return partyId;
	}
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}
	public AddressDto getOfficeAddressDto() {
		return officeAddressDto;
	}
	public void setOfficeAddressDto(AddressDto officeAddressDto) {
		this.officeAddressDto = officeAddressDto;
	}
	public AddressDto getHomeAddressDto() {
		return homeAddressDto;
	}
	public void setHomeAddressDto(AddressDto homeAddressDto) {
		this.homeAddressDto = homeAddressDto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLandLine1() {
		return landLine1;
	}
	public void setLandLine1(String landLine1) {
		this.landLine1 = landLine1;
	}
	public String getLandLine2() {
		return landLine2;
	}
	public void setLandLine2(String landLine2) {
		this.landLine2 = landLine2;
	}
	public String getMobile1() {
		return mobile1;
	}
	public void setMobile1(String mobile1) {
		this.mobile1 = mobile1;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this.mobile2 = mobile2;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

    public String getFbPage() {
        return fbPage;
    }

    public void setFbPage(String fbPage) {
        this.fbPage = fbPage;
    }

    public String getFbAccount() {
        return fbAccount;
    }

    public void setFbAccount(String fbAccount) {
        this.fbAccount = fbAccount;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(String urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }
    @Override
    public String toString() {
        return "PoliticalBodyAdminDto [politicalBodyTypeId=" + politicalBodyTypeId + ", locationId=" + locationId + ", personId=" + personId + ", partyId=" + partyId + ", officeAddressDto="
                + officeAddressDto + ", homeAddressDto=" + homeAddressDto + ", email=" + email + ", landLine1=" + landLine1 + ", landLine2=" + landLine2 + ", mobile1=" + mobile1 + ", mobile2="
                + mobile2 + ", startDate=" + startDate + ", endDate=" + endDate + ", active=" + active + ", fbPage=" + fbPage + ", fbAccount=" + fbAccount + ", twitterHandle=" + twitterHandle
                + ", id=" + id + "]";
    }
	

}
