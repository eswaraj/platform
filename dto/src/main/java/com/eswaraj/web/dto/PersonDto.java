package com.eswaraj.web.dto;

import java.util.Date;

/**
 * Person making or resolving the complaint
 * @author anuj
 * @date Jan 18, 2014
 *
 */
public class PersonDto extends BaseDto {

	private static final long serialVersionUID = 1L;
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
    private String voterId;
	private AddressDto personAddress;
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
	public AddressDto getPersonAddress() {
		return personAddress;
	}
	public void setPersonAddress(AddressDto personAddress) {
		this.personAddress = personAddress;
	}

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }
    @Override
	public String toString() {
		return "PersonDto [name=" + name + ", \nbiodata=" + biodata + ", \ndob=" + dob + ", \ngender=" + gender + ", \nemail=" + email + ", \nlandlineNumber1="
				+ landlineNumber1 + ", \nlandlineNumber2=" + landlineNumber2 + ", \nmobileNumber1=" + mobileNumber1 + ", \nmobileNumber2=" + mobileNumber2
				+ ", \npersonAddress=" + personAddress + ", \nid=" + id + "]";
	}
	
	
}
