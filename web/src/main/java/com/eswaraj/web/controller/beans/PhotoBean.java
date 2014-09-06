package com.eswaraj.web.controller.beans;

public class PhotoBean {
    private String imageUrlLarge;
    private String imageUrlMedium;
    private String imageUrlSmall;
    private String imageUrlSquare;
    private String orgUrl;

    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    public String getImageUrlMedium() {
        return imageUrlMedium;
    }

    public void setImageUrlMedium(String imageUrlMedium) {
        this.imageUrlMedium = imageUrlMedium;
    }

    public String getImageUrlSmall() {
        return imageUrlSmall;
    }

    public void setImageUrlSmall(String imageUrlSmall) {
        this.imageUrlSmall = imageUrlSmall;
    }

    public String getImageUrlSquare() {
        return imageUrlSquare;
    }

    public void setImageUrlSquare(String imageUrlSquare) {
        this.imageUrlSquare = imageUrlSquare;
    }

    public String getOrgUrl() {
        return orgUrl;
    }

    public void setOrgUrl(String orgUrl) {
        this.orgUrl = orgUrl;
    }
}
