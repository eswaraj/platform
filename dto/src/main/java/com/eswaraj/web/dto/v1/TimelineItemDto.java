package com.eswaraj.web.dto.v1;

import java.util.Date;
import java.util.List;

public class TimelineItemDto {

    private Long id;
    private String title;
    private String description;
    private Date updateTime;
    private Date creationTime;
    private PersonDto createdBy;
    private List<String> youtubeUrl;
    private List<String> images;
    private List<String> documents;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public PersonDto getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(PersonDto createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(List<String> youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }
}
