package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Device of a user
 * 
 * @author ravi
 * @date Jul 20, 2014
 *
 */
@NodeEntity
public class Device extends BaseNode {

	private DeviceType deviceType;
	@Indexed(unique=true)
	private String deviceId;

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public enum DeviceType {
		Android, Iphone;
	}

    @Override
    public String toString() {
        return "Device [deviceType=" + deviceType + ", deviceId=" + deviceId + ", id=" + id + "]";
    }

}
