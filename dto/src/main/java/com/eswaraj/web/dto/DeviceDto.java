package com.eswaraj.web.dto;

public class DeviceDto extends BaseDto {

    private static final long serialVersionUID = 1L;
    private String deviceTypeRef;
    private String deviceId;
    private String gcmId;

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

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
}
