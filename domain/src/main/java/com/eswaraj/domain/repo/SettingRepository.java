package com.eswaraj.domain.repo;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Setting;

public interface SettingRepository extends GraphRepository<Setting> {
	
}
