package com.eswaraj.domain.nodes.relationships;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.User;

@RelationshipEntity(type = "USER_DEVICE")
public class UserDevice {

    @GraphId
    private Long id;
    @StartNode private User user;
    @EndNode private Device device;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserDevice [id=" + id + ", user=" + user + ", device=" + device + "]";
    }

}
