package com.eswaraj.web.dto;


public class AddressDto extends BaseDto {

	private static final long serialVersionUID = 1L;
	private String line1;
	private String line2;
	private String line3;
	private String postalCode;
	
	private Long villageId;
	private Long wardId;
	private Long cityId;
	private Long districtId;
	private Long stateId;
	private Long countryId;
    private Double lattitude;
    private Double longitude;

    private LocationDto country;
    private LocationDto state;
    private LocationDto district;
    private LocationDto ac;
    private LocationDto pc;
    private LocationDto ward;
    private LocationDto village;
    private LocationDto city;
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
	public Long getVillageId() {
		return villageId;
	}
	public void setVillageId(Long villageId) {
		this.villageId = villageId;
	}
	public Long getWardId() {
		return wardId;
	}
	public void setWardId(Long wardId) {
		this.wardId = wardId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Long getStateId() {
		return stateId;
	}
	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
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

    public LocationDto getCountry() {
        return country;
    }

    public void setCountry(LocationDto country) {
        this.country = country;
    }

    public LocationDto getState() {
        return state;
    }

    public void setState(LocationDto state) {
        this.state = state;
    }

    public LocationDto getDistrict() {
        return district;
    }

    public void setDistrict(LocationDto district) {
        this.district = district;
    }

    public LocationDto getAc() {
        return ac;
    }

    public void setAc(LocationDto ac) {
        this.ac = ac;
    }

    public LocationDto getPc() {
        return pc;
    }

    public void setPc(LocationDto pc) {
        this.pc = pc;
    }

    public LocationDto getWard() {
        return ward;
    }

    public void setWard(LocationDto ward) {
        this.ward = ward;
    }

    public LocationDto getVillage() {
        return village;
    }

    public void setVillage(LocationDto village) {
        this.village = village;
    }

    public LocationDto getCity() {
        return city;
    }

    public void setCity(LocationDto city) {
        this.city = city;
    }
	
}
