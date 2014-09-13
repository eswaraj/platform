package com.eswaraj.web.dto;

import java.io.Serializable;
import java.util.Date;

public class RegisterFacebookAccountWebRequest implements Serializable {

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

    @Override
    public String toString() {
        return "RegisterFacebookAccountWebRequest [token=" + token + ", expireTime=" + expireTime + ", facebookAppId=" + facebookAppId + "]";
    }

}
