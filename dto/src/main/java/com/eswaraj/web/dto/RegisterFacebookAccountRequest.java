package com.eswaraj.web.dto;

import java.io.Serializable;
import java.util.Date;

public class RegisterFacebookAccountRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userExternalId;
    private String token;
    private Date expireTime;
    private String facebookAppId;
    private String deviceTypeRef;
    private String deviceId;

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }

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

    public String getDeviceTypeRef() {
        return deviceTypeRef;
    }

    public void setDeviceTypeRef(String deviceTypeRef) {
        this.deviceTypeRef = deviceTypeRef;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
