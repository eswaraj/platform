package com.eswaraj.web.dto;

public class PoliticalBodyAdminStaffDto extends BaseDto {

    private static final long serialVersionUID = 1L;
    private PersonDto personDto;
    private Long politicalBodyAdminId;
    private String post;

    public PersonDto getPersonDto() {
        return personDto;
    }

    public void setPersonDto(PersonDto personDto) {
        this.personDto = personDto;
    }

    public Long getPoliticalBodyAdminId() {
        return politicalBodyAdminId;
    }

    public void setPoliticalBodyAdminId(Long politicalBodyAdminId) {
        this.politicalBodyAdminId = politicalBodyAdminId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
