package com.eswaraj.web.controller.beans;

public class Address extends BaseBean {
    
    private static final long serialVersionUID = 1L;
    private String line1;
    private String line2;
    private String line3;
    private String postalCode;
    private Double lattitude;
    private Double longitude;

    private String ward;
    private String ac;
    private String pc;
    private String city;
    private String district;
    private String state;
    private String country;

    private String wardUrl;
    private String acUrl;
    private String pcUrl;
    private String cityUrl;
    private String districtUrl;
    private String stateUrl;
    private String countryUrl;

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

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWardUrl() {
        return wardUrl;
    }

    public void setWardUrl(String wardUrl) {
        this.wardUrl = wardUrl;
    }

    public String getAcUrl() {
        return acUrl;
    }

    public void setAcUrl(String acUrl) {
        this.acUrl = acUrl;
    }

    public String getPcUrl() {
        return pcUrl;
    }

    public void setPcUrl(String pcUrl) {
        this.pcUrl = pcUrl;
    }

    public String getCityUrl() {
        return cityUrl;
    }

    public void setCityUrl(String cityUrl) {
        this.cityUrl = cityUrl;
    }

    public String getDistrictUrl() {
        return districtUrl;
    }

    public void setDistrictUrl(String districtUrl) {
        this.districtUrl = districtUrl;
    }

    public String getStateUrl() {
        return stateUrl;
    }

    public void setStateUrl(String stateUrl) {
        this.stateUrl = stateUrl;
    }

    public String getCountryUrl() {
        return countryUrl;
    }

    public void setCountryUrl(String countryUrl) {
        this.countryUrl = countryUrl;
    }

}
