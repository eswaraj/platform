package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * A political elected representative
 * @author ravi
 * @date Jun 14, 2014
 *
 */
@NodeEntity
public class PoliticalBodyAdmin extends BaseNode {

	@RelatedTo(type="OF_TYPE")
    @Fetch
	private PoliticalBodyType politicalBodyType;

	@RelatedTo(type="BELONGS_TO")
	private Location location;

	@RelatedTo(type="IS")
	private Person person;

	@RelatedTo(type="OF")
	private Party party;

	private Address officeAddress;
	private Address homeAddress;
	
	private String email;
	
    private String fbPage;
    private String fbAccount;
    private String twitterHandle;

	private String landLine1;
	private String landLine2;
	private String mobile1;
	private String mobile2;
	private Date startDate;
	private Date endDate;
	private boolean active;
    private String urlIdentifier;
	
	public PoliticalBodyType getPoliticalBodyType() {
		return politicalBodyType;
	}
	public void setPoliticalBodyType(PoliticalBodyType politicalBodyType) {
		this.politicalBodyType = politicalBodyType;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Party getParty() {
		return party;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	public Address getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(Address officeAddress) {
		this.officeAddress = officeAddress;
	}
	public Address getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
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
        return "PoliticalBodyAdmin [politicalBodyType=" + politicalBodyType + ", location=" + location + ", person=" + person + ", party=" + party + ", officeAddress=" + officeAddress
                + ", homeAddress=" + homeAddress + ", email=" + email + ", fbPage=" + fbPage + ", fbAccount=" + fbAccount + ", twitterHandle=" + twitterHandle + ", landLine1=" + landLine1
                + ", landLine2=" + landLine2 + ", mobile1=" + mobile1 + ", mobile2=" + mobile2 + ", startDate=" + startDate + ", endDate=" + endDate + ", active=" + active + ", urlIdentifier="
                + urlIdentifier + ", id=" + id + "]";
    }
	
	
}
