package com.eswaraj.web.dto;

import java.io.Serializable;

public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    protected String externalId;
    private PersonDto person;
    private FacebookAccountDto facebookAccount;
    private DeviceDto device;
    private FacebookAppPermissionDto facebookAppPermission;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }

    public FacebookAccountDto getFacebookAccount() {
        return facebookAccount;
    }

    public void setFacebookAccount(FacebookAccountDto facebookAccount) {
        this.facebookAccount = facebookAccount;
    }

    public DeviceDto getDevice() {
        return device;
    }

    public void setDevice(DeviceDto device) {
        this.device = device;
    }

    public FacebookAppPermissionDto getFacebookAppPermission() {
        return facebookAppPermission;
    }

    public void setFacebookAppPermission(FacebookAppPermissionDto facebookAppPermission) {
        this.facebookAppPermission = facebookAppPermission;
    }

    @Override
    public String toString() {
        return "UserDto [externalId=" + externalId + ", person=" + person + ", facebookAccount=" + facebookAccount + ", device=" + device + ", facebookAppPermission=" + facebookAppPermission + "]";
    }

}
