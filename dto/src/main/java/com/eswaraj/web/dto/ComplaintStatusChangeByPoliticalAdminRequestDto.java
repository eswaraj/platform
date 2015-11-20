package com.eswaraj.web.dto;

public class ComplaintStatusChangeByPoliticalAdminRequestDto {

    private Long personId;
    private Long politicalAdminId;
    private Long complaintId;
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
