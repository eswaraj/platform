package com.eswaraj.domain.nodes;

import java.util.Set;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.eswaraj.domain.base.BaseNode;

/**
 * Complaint made by a person
 * 
 * @author anuj
 * @date Jan 18, 2014
 *
 */
@NodeEntity
@TypeAlias("Complaint")
public class Complaint extends BaseNode {

    private String title;
    private String description;
    private double lattitude;
    private double longitude;
    @RelatedTo(type = "BELONGS_TO")
    @Fetch
    private Set<Category> categories;
    @RelatedTo(type = "SERVED_BY")
    private DepartmentAdmin administrator;
    private Status status;
    @RelatedTo(type = "AT")
    private Set<Location> locations;
    @Indexed
    private Long complaintTime;
    @Indexed
    private String nearByKey;
    private String locationAddress;
    private String shortUrl;

    public Complaint() {
        this.status = Status.Pending;
    }

    public Complaint(String title) {
        this.title = title;
        this.status = Status.Pending;
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

    public DepartmentAdmin getAdministrator() {
        return administrator;
    }

    public void setAdministrator(DepartmentAdmin administrator) {
        this.administrator = administrator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {

        Pending("Complaint has not been looked upon by an Administrator yet"),  
        Duplicate("Administrator thinks this is a duplicate of another complaint"), 
        Merged("This complaint has one or more Complaint merged into"), 
        Done("This complaint has been resolved!");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
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

    public Long getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Long complaintTime) {
        this.complaintTime = complaintTime;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public String getNearByKey() {
        return nearByKey;
    }

    public void setNearByKey(String nearByKey) {
        this.nearByKey = nearByKey;
    }

    @Override
    public String toString() {
        return "Complaint [title=" + title + ", description=" + description + ", lattitude=" + lattitude + ", longitude=" + longitude + ", categories=" + categories + ", administrator="
                + administrator + ", status=" + status + ", locations=" + locations + ", complaintTime=" + complaintTime + ", nearByKey=" + nearByKey + ", id=" + id
                + ", externalId=" + externalId + "]";
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

}
