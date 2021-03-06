package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Device;
import com.eswaraj.domain.nodes.User;
import com.eswaraj.domain.nodes.relationships.UserDevice;

/**
 * Repo for User Device
 * 
 * @author ravi
 * @data Sep 15, 2014
 */
public interface UserDeviceRepository extends GraphRepository<UserDevice> {
	
    @Query("start user=node({0}), device=node({1}) match (user)-[userDevice:USER_DEVICE]-(device) return userDevice")
    UserDevice getUserDeviceRelation(User user, Device device);

    @Query("start user=node({0}) match (user)-[userDevice:USER_DEVICE]-(device) return userDevice")
    List<UserDevice> getAllUserDeviceRelationOfUser(User user);

}
