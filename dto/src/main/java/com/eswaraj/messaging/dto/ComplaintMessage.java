package com.eswaraj.messaging.dto;

import java.io.Serializable;
import java.util.List;

public class ComplaintMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
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
    private String imageUrl;
    private long complaintTime;

    private List<Long> politicalAdminIds;
    private List<Long> locationIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        return politicalAdminIds;
    }

    public void setPoliticalAdminIds(List<Long> politicalAdminIds) {
        this.politicalAdminIds = politicalAdminIds;
    }

    public long getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(long complaintTime) {
        this.complaintTime = complaintTime;
    }

    public List<Long> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Long> locationIds) {
        this.locationIds = locationIds;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ComplaintCreatedMessage [title=" + title + ", description=" + description + ", lattitude=" + lattitude + ", longitude=" + longitude + ", categoryIds=" + categoryIds + ", personId="
                + personId + ", userId=" + userId + ", deviceIds=" + deviceIds + ", adminId=" + adminId + ", status=" + status + ", complaintTime=" + complaintTime + ", PoliticalAdminIds="
                + politicalAdminIds + "]";
    }

}
