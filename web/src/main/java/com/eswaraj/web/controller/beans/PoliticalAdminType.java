package com.eswaraj.web.controller.beans;

public class PoliticalAdminType extends BaseBean {

    private static final long serialVersionUID = 1L;
    private String shortName;
    private String name;
    private String description;

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
