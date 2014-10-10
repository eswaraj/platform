package com.eswaraj.web.dto.comment;

public class CommentSaveResponseDto {

    private Long id;
    private Long personId;
    private Long politicalAdminId;
    private Long complaintId;
    private String commentText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getPoliticalAdminId() {
        return politicalAdminId;
    }

    public void setPoliticalAdminId(Long politicalAdminId) {
        this.politicalAdminId = politicalAdminId;
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

}
