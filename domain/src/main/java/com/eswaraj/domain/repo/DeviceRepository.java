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

    @Query("start user=node({0}) match (user)-[:USER_DEVICE]->(device) where device.__type__ = 'com.eswaraj.domain.nodes.Device' return device")
    public List<Device> getAllDevicesOfUser(User user);

    @Query("start complaint=node({0}) match (complaint)-[:LODGED_BY]->(person)-[:ATTACHED_TO]-(user)-[:USER_DEVICE]-(device) where device.__type__ = 'com.eswaraj.domain.nodes.Device' return device")
    public List<Device> getDevicesForComplaint(Long complaintId);

    @Query("start person=node({0}) match (person)-[:ATTACHED_TO]-(user)-[:USER_DEVICE]->(device) where device.__type__ = 'com.eswaraj.domain.nodes.Device' or device.__type__ = 'Device' return device")
    public List<Device> getAllDevicesOfPerson(Long personId);

}
