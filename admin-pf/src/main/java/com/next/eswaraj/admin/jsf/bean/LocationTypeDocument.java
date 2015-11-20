package com.next.eswaraj.admin.jsf.bean;

import java.io.Serializable;

import com.eswaraj.domain.nodes.LocationType;

public class LocationTypeDocument implements Serializable, Comparable<LocationTypeDocument> {

    private static final long serialVersionUID = 1L;

    private String name;

    private String size;

    private String type;

    private LocationType locationType;

    public LocationTypeDocument(String name, String size, String type, LocationType locationType) {
        this.name = name;
        this.size = size;
        this.type = type;
        this.locationType = locationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }

    // Eclipse Generated hashCode and equals
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocationTypeDocument other = (LocationTypeDocument) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(LocationTypeDocument document) {
        return this.getName().compareTo(document.getName());
    }
}