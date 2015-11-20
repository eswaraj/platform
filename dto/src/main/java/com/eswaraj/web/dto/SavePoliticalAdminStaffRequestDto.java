package com.eswaraj.web.dto;

public class SavePoliticalAdminStaffRequestDto {

    private Long personId;
    private Long politicalAdminId;
    private String post;

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

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
