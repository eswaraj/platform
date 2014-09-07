package com.eswaraj.web.controller.beans;

public class CategoryBean {

    private String externalId;
    private String headerImageurl;
    private String imageUrl;
    private String name;
    private String videoUrl;
    private Long locationCount;
    private Long globalCount;
    private boolean root;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getHeaderImageurl() {
        return headerImageurl;
    }

    public void setHeaderImageurl(String headerImageurl) {
        this.headerImageurl = headerImageurl;
    }

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
}
