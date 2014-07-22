package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Device;

/**
 * Repo for Device
 * @author ravi
 * @data Jul 22, 2014
 */
public interface DeviceRepository extends GraphRepository<Device>{
	
	@Query("start Device=node:Device(deviceId={0}) return Device")
    public Device getDeviceByDeviceId(String deviceId);
	
}
