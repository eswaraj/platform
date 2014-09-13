package com.eswaraj.web.dto;

import java.util.Date;

public class FacebookAppPermissionDto extends BaseDto {

    private static final long serialVersionUID = 1L;
    private String token;
    private Date expireTime;
    private String facebookAppId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public void setFacebookAppId(String facebookAppId) {
        this.facebookAppId = facebookAppId;
    }

}
