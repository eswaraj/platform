package com.eswaraj.messaging.dto;

import java.io.Serializable;
import java.util.List;

public class ComplaintCreatedMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    private double lattitude;
    private double longitude;
    private List<Long> categoryIds;
    private Long personId;
    private Long userId;
    private List<String> deviceIds;
    private Long adminId;
    private String status;

    private List<Long> PoliticalAdminIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Long> getPoliticalAdminIds() {
        return PoliticalAdminIds;
    }

    public void setPoliticalAdminIds(List<Long> politicalAdminIds) {
        PoliticalAdminIds = politicalAdminIds;
    }
}
