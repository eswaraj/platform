package com.eswaraj.web.controller.beans;

public class CommentBean {

    // "{\"text\":\"We will let you know soon\",\"creationTime\":1413954273664,\"id\":84108,\"postedBy\":{\"name\":\"Ravi Sharma\",\"id\":84066,\"externalId\":\"fb451cd9-605d-4efe-ad7a-96a8a9e8f1ca\"},\"adminComment\":true,\"admin\":{\"name\":\"Ravi Sharma\",\"id\":84069,\"externalId\":null,\"type\":\"political\"}}"
    private String text;
    private Long creationTime;
    private Long id;
    private boolean adminComment;
    private PersonBean postedBy;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAdminComment() {
        return adminComment;
    }

    public void setAdminComment(boolean adminComment) {
        this.adminComment = adminComment;
    }

    public PersonBean getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(PersonBean postedBy) {
        this.postedBy = postedBy;
    }
}
