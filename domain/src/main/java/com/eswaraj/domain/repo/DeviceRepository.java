package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.User;

/**
 * Repo for Device
 * 
 * @author ravi
 * @data Jul 22, 2014
 */
public interface DeviceRepository extends GraphRepository<Device> {

    @Query("start Device=node:Device(deviceId={0}) return Device")
    public Device getDeviceByDeviceId(String deviceId);

    @Query("start user=node({0}) match (user)-[:USER_DEVICE]->(device) where device.__type__ = 'com.eswaraj.domain.nodes.Device' return device")
    public List<Device> getAllDevicesOfUser(User user);

}
