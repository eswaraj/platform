package com.eswaraj.domain.repo;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.eswaraj.domain.nodes.Setting;

public interface SettingRepository extends GraphRepository<Setting> {
	
    @Query("match setting where setting.__type__='Setting' return setting")
    public List<Setting> getAllSettings();

}
