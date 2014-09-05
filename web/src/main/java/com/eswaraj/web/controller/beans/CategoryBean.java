package com.eswaraj.web.controller.beans;

public class CategoryBean {

    private String externalId;
    private String headerImageurl;
    private String imageUrl;
    private String name;
    private String videoUrl;
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

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
