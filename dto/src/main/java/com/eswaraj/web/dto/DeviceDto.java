package com.eswaraj.web.dto;

public class DeviceDto extends BaseDto {

    private static final long serialVersionUID = 1L;
    private String deviceTypeRef;
    private String deviceId;

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
