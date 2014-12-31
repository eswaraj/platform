package com.eswaraj.web.dto;

public class UpdateMobileUserRequestDto extends UpdateUserRequestWebDto {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
