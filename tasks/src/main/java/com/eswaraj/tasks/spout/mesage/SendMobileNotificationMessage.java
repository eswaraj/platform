package com.eswaraj.tasks.spout.mesage;

import java.io.Serializable;
import java.util.List;

public class SendMobileNotificationMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String message;
    private String messageType;
    private List<String> deviceList;

    public SendMobileNotificationMessage(String message, String messageType, List<String> deviceList) {
        super();
        this.message = message;
        this.messageType = messageType;
        this.deviceList = deviceList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<String> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<String> deviceList) {
        this.deviceList = deviceList;
    }
}
