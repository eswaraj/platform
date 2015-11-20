package com.eswaraj.messaging.dto;

import java.io.Serializable;

public class ComplaintViewedByPoliticalAdminMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long complaintId;
    private Long politicalAdminId;
    private Long personId;

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public Long getPoliticalAdminId() {
        return politicalAdminId;
    }

    public void setPoliticalAdminId(Long politicalAdminId) {
        this.politicalAdminId = politicalAdminId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    @Override
    public String toString() {
        return "ComplaintViewedByPoliticalAdminMessage [complaintId=" + complaintId + ", politicalAdminId=" + politicalAdminId + ", personId=" + personId + "]";
    }

}
