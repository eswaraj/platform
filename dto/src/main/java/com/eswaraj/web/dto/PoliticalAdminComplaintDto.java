package com.eswaraj.web.dto;

import java.util.Date;




/**
 * DTO for a complaint is assigned to a political Admin
 * 
 * @author Ravi
 * @data Jun 22, 2014
 */

public class PoliticalAdminComplaintDto extends ComplaintDto {

	private static final long serialVersionUID = 1L;
	
    private boolean viewed;
    private String politicalAdminComplaintStatus;
    private Date viewDate;

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public String getPoliticalAdminComplaintStatus() {
        return politicalAdminComplaintStatus;
    }

    public void setPoliticalAdminComplaintStatus(String politicalAdminComplaintStatus) {
        this.politicalAdminComplaintStatus = politicalAdminComplaintStatus;
    }

    public Date getViewDate() {
        return viewDate;
    }

    public void setViewDate(Date viewDate) {
        this.viewDate = viewDate;
    }
	
	
}
