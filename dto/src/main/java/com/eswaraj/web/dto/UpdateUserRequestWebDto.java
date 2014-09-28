package com.eswaraj.web.dto;

public class UpdateUserRequestWebDto extends BaseDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private Double lattitude;
    private Double longitude;
    private String voterId;
    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "UpdateUserRequestWebDto [name=" + name + ", lattitude=" + lattitude + ", longitude=" + longitude + ", voterId=" + voterId + ", userId=" + userId + "]";
    }

}
