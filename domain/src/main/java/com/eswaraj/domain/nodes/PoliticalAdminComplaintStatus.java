package com.eswaraj.domain.nodes;

public enum PoliticalAdminComplaintStatus {

    PENDING("Complaint has not been looked upon by an Administrator yet"), 
    ACKNOWLEDGED("Complaint has been looked upon and understood by an Administrator"), 
    QUERY("An Administrator has a query about this complaint"), 
    DUPLICATE("Administrator thinks this is a duplicate of another complaint"), 
    ASSIGNED("Administrator has assigned a task force to this complaint"), 
    IN_PROGRESS("Your complaint is being worked upon"), 
    IN_REVIEW("Your complaint has been worked upon waiting for your review."), 
    DONE("This complaint has been resolved!"), 
    UNFINISHED("This complaint has been neglected far too long. Name and shame time!"), 
    ESCLATED("Your complaint has been escalated");

    private String description;

    PoliticalAdminComplaintStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
