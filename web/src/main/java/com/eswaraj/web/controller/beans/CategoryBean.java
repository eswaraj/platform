package com.eswaraj.web.controller.beans;

import java.util.List;

public class CategoryBean extends BaseBean {

    private static final long serialVersionUID = 1L;
    private String headerImageUrl;
    private String imageUrl;
    private String name;
    private String videoUrl;
    private Long locationCount;
    private Long globalCount;
    private boolean root;
    private String description;
    private List<CategoryBean> childCategories;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Long getLocationCount() {
        return locationCount;
    }

    public void setLocationCount(Long locationCount) {
        this.locationCount = locationCount;
    }

    public Long getGlobalCount() {
        return globalCount;
    }

    public void setGlobalCount(Long globalCount) {
        this.globalCount = globalCount;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

    public List<CategoryBean> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<CategoryBean> childCategories) {
        this.childCategories = childCategories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeaderImageUrl() {
        return headerImageUrl;
    }

    public void setHeaderImageUrl(String headerImageUrl) {
        this.headerImageUrl = headerImageUrl;
    }

    @Override
    public String toString() {
        return "CategoryBean [headerImageUrl=" + headerImageUrl + ", imageUrl=" + imageUrl + ", name=" + name + ", videoUrl=" + videoUrl + ", locationCount=" + locationCount + ", globalCount="
                + globalCount + ", root=" + root + ", description=" + description + ", childCategories=" + childCategories + ", id=" + id + ", externalId=" + externalId + "]";
    }

}
