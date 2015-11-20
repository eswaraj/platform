package com.eswaraj.web.dto;

public class RegisterDeviceRequest extends DeviceDto {

    private static final long serialVersionUID = 1L;
    private String userExternalId;

    public String getUserExternalId() {
        return userExternalId;
    }

    public void setUserExternalId(String userExternalId) {
        this.userExternalId = userExternalId;
    }
}
