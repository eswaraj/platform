package com.eswaraj.domain.nodes;

public enum ExecutiveAdminComplaintStatus {

    Pending("Complaint has not been looked upon by an Administrator yet"), 
    Viewed("Complaint has been looked upon and understood by an Administrator"),
    Query("An Administrator has a query about this complaint"),
    Duplicate("Administrator thinks this is a duplicate of another complaint"), 
    Assigned("Administrator has assigned a task force to this complaint"), 
    InProgress("Your complaint is being worked upon"), 
    InReview("Your complaint has been worked upon waiting for your review."), 
    Done("This complaint has been resolved!"), 
    Unfinished("This complaint has been neglected far too long. Name and shame time!"), 
    Esclated("Your complaint has been escalated");

    private String description;

    ExecutiveAdminComplaintStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
