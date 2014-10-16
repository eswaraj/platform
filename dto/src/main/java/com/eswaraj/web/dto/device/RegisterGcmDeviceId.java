package com.eswaraj.web.dto.device;

import java.io.Serializable;

public class RegisterGcmDeviceId implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userExternalId;
    private String deviceId;
    private String gcmId;

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

}
