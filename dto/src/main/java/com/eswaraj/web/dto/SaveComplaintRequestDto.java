package com.eswaraj.web.dto;




/**
 * DTO for a complaint that has been made by a user.
 * @author anuj
 * @data Jun 22, 2014
 */

public class SaveComplaintRequestDto extends ComplaintDto {

	private static final long serialVersionUID = 1L;
    private String userExternalid;
    private String googleLocationJson;
    private boolean anonymous;

    public String getUserExternalid() {
        return userExternalid;
    }

    public void setUserExternalid(String userExternalid) {
        this.userExternalid = userExternalid;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getGoogleLocationJson() {
        return googleLocationJson;
    }

    public void setGoogleLocationJson(String googleLocationJson) {
        this.googleLocationJson = googleLocationJson;
    }

    @Override
    public String toString() {
        return "SaveComplaintRequestDto [userExternalid=" + userExternalid + ", anonymous=" + anonymous + ", toString()=" + super.toString() + "]";
    }
	
}
