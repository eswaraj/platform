package com.eswaraj.web.controller.beans;

import java.util.Calendar;
import java.util.Locale;


public class Leader extends BaseBean {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long startDate;
    private String fbAccount;
    private String fbPage;
    private String twitterHandle;
    private String officeEmail;
    private String urlIdentifier;
    private String name;
    private String personExternalId;
    private String gender;
    private String profilePhoto;
    private String landlineNumber1;
    private String landlineNumber2;
    private String mobileNumber1;
    private String mobileNumber2;
    private Party party;
    private PoliticalAdminType politicalAdminType;
    private Address homeAddress;
    private Address officeAddress;

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public String getFbAccount() {
        return fbAccount;
    }

    public void setFbAccount(String fbAccount) {
        this.fbAccount = fbAccount;
    }

    public String getFbPage() {
        return fbPage;
    }

    public void setFbPage(String fbPage) {
        this.fbPage = fbPage;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public String getOfficeEmail() {
        return officeEmail;
    }

    public void setOfficeEmail(String officeEmail) {
        this.officeEmail = officeEmail;
    }

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

    public void setUrlIdentifier(String urlIdentifier) {
        this.urlIdentifier = urlIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonExternalId() {
        return personExternalId;
    }

    public void setPersonExternalId(String personExternalId) {
        this.personExternalId = personExternalId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public PoliticalAdminType getPoliticalAdminType() {
        return politicalAdminType;
    }

    public void setPoliticalAdminType(PoliticalAdminType politicalAdminType) {
        this.politicalAdminType = politicalAdminType;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getOfficeAddress() {
        return officeAddress;
    }

    public void setOfficeAddress(Address officeAddress) {
        this.officeAddress = officeAddress;
    }

    public String getSince() {
        if (startDate == null) {
            return "";
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDate);
        return cal.getDisplayName(Calendar.MONDAY, Calendar.SHORT, Locale.US) + " " + cal.get(Calendar.YEAR);
    }
    
    
}
