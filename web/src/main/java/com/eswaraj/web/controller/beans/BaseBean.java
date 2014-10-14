package com.eswaraj.web.controller.beans;

import java.io.Serializable;

public class BaseBean implements Serializable {

    private static final long serialVersionUID = 1L;
    protected Long id;
    protected String externalId;

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

    @Override
    public String toString() {
        return "BaseBean [id=" + id + ", externalId=" + externalId + "]";
    }

}
