package com.eswaraj.domain.nodes;

import java.util.Date;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;

import com.eswaraj.domain.base.BaseNode;

/**
 * Person making or resolving the complaint
 * @author anuj
 * @date Jan 18, 2014
 *
 */
@NodeEntity
public class Person extends BaseNode {

	@Indexed(indexName="PersonNameFt", indexType=IndexType.FULLTEXT)
	private String name;
	private String biodata;
	private Date dob;
	private String gender;
	private String email;
	private String landlineNumber1;
	private String landlineNumber2;
	private String mobileNumber1;
	private String mobileNumber2;
    private String profilePhoto;
	private Address address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBiodata() {
		return biodata;
	}
	public void setBiodata(String biodata) {
		this.biodata = biodata;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLandlineNumber1() {
		return landlineNumber1;
	}
	public void setLandlineNumber1(String landlineNumber1) {
		this.landlineNumber1 = landlineNumber1;
	}
	public String getLandlineNumber2() {
		return landlineNumber2;
	}
	public void setLandlineNumber2(String landlineNumber2) {
		this.landlineNumber2 = landlineNumber2;
	}
	public String getMobileNumber1() {
		return mobileNumber1;
	}
	public void setMobileNumber1(String mobileNumber1) {
		this.mobileNumber1 = mobileNumber1;
	}
	public String getMobileNumber2() {
		return mobileNumber2;
	}
	public void setMobileNumber2(String mobileNumber2) {
		this.mobileNumber2 = mobileNumber2;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", biodata=" + biodata + ", dob=" + dob + ", gender=" + gender + ", email=" + email + ", landlineNumber1=" + landlineNumber1 + ", landlineNumber2="
                + landlineNumber2 + ", mobileNumber1=" + mobileNumber1 + ", mobileNumber2=" + mobileNumber2 + ", profilePhoto=" + profilePhoto + ", address=" + address + ", id=" + id + "]";
    }
	
	

}
