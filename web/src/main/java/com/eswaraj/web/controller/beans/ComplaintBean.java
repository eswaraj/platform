package com.eswaraj.web.controller.beans;

import java.util.List;


public class ComplaintBean extends BaseBean {

    private static final long serialVersionUID = 1L;
    private Long complaintTime;
    private String complaintTimeIso;
    private String title;
    private String description;
    private Double lattitude;
    private Double longitude;
    private String status;
    private String categoryTitle;
    private CategoryBean[] categories;
    private LocationBean[] locations;
    private PhotoBean[] photos;
    private List<PersonBean> loggedBy;
    private Long totalComments;
    
    public Long getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Long complaintTime) {
        this.complaintTime = complaintTime;
    }

    public String getComplaintTimeIso() {
        return complaintTimeIso;
    }

    public void setComplaintTimeIso(String complaintTimeIso) {
        this.complaintTimeIso = complaintTimeIso;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public CategoryBean[] getCategories() {
        return categories;
    }

    public void setCategories(CategoryBean[] categories) {
        this.categories = categories;
    }

    public LocationBean[] getLocations() {
        return locations;
    }

    public void setLocations(LocationBean[] locations) {
        this.locations = locations;
    }

    public PhotoBean[] getPhotos() {
        return photos;
    }

    public void setPhotos(PhotoBean[] photos) {
        this.photos = photos;
    }

    public List<PersonBean> getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(List<PersonBean> loggedBy) {
        this.loggedBy = loggedBy;
    }

    public Long getSubCategoryId() {
        Long categoryId = null;
        if (categories != null) {
            for (CategoryBean oneCategoryBean : categories) {
                if (!oneCategoryBean.isRoot()) {
                    categoryId = oneCategoryBean.getId();
                    break;
                }
            }
        }
        return categoryId;
    }

    public Long getCategoryId() {
        Long categoryId = null;
        if (categories != null) {
            for (CategoryBean oneCategoryBean : categories) {
                if (oneCategoryBean.isRoot()) {
                    categoryId = oneCategoryBean.getId();
                    break;
                }
            }
        }
        return categoryId;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }

}
