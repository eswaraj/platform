package com.eswaraj.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.SettingService;
import com.eswaraj.domain.nodes.Setting;
import com.eswaraj.domain.nodes.Setting.SettingNames;
import com.eswaraj.domain.repo.SettingRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class SettingServiceImpl extends DbBaseService implements SettingService {

    private static final long serialVersionUID = 1L;
    @Autowired
    private SettingRepository settingRepository;

    LoadingCache<String, Map<String, Setting>> settingCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterWrite(30, TimeUnit.SECONDS).build(new CacheLoader<String, Map<String, Setting>>() {
        @Override
        public Map<String, Setting> load(String key) throws Exception {
            EndResult<Setting> allSettings = settingRepository.findAll();
            Map<String, Setting> settingMap = new HashMap<String, Setting>();
            for (Setting oneSetting : allSettings) {
                settingMap.put(oneSetting.getName(), oneSetting);
            }
            return settingMap;
        }
    });

    @Override
    // @CachePut(value = "settings")
    public Setting saveSetting(Setting setting) throws ApplicationException {
        setting = settingRepository.save(setting);
        settingCache.invalidateAll();
        return setting;
    }

    @Override
    // @Cacheable(value = "settings", key = "all.settings")
    public List<Setting> getAllSettings() throws ApplicationException {
        try {
            return new ArrayList<Setting>(settingCache.get("All").values());
        } catch (ExecutionException e) {
            throw new ApplicationException(e);
        }
    }

    @Override
    // @Cacheable(value = "settings", key = "setting.#setting.name")
    public Setting getSetting(String settingName) throws ApplicationException {
        Map<String, Setting> settingMap;
        try {
            settingMap = settingCache.get("All");
            logger.info("settingMap : {} ", settingMap);
            logger.info("Getting  settingName: {} ", settingName);
            return settingMap.get(settingName);
        } catch (ExecutionException e) {
            throw new ApplicationException(e);
        }

    }

    private boolean getBooleanResult(Setting.SettingNames settingName) throws ApplicationException {
        Setting setting = getSetting(settingName.getName());
        logger.info("Setting = {}", setting);
        if (setting != null) {
            if ("true".equalsIgnoreCase(setting.getValue())) {
                logger.info("{} - true", settingName.getName());
                return true;
            }
            if ("false".equalsIgnoreCase(setting.getValue())) {
                logger.info("{} - false", settingName.getName());
                return false;
            }
        }
        if ("true".equalsIgnoreCase(settingName.getDefaultValue())) {
            logger.info("Default {} - true", settingName.getName());
            return true;
        }
        logger.info("Default {} - false", settingName.getName());
        return false;
    }

    @Override
    public boolean isAllowComplaint() throws ApplicationException {
        return getBooleanResult(SettingNames.ALLOW_COMPLAINT);
    }

    @Override
    public boolean isFakeLocation() throws ApplicationException {
        return getBooleanResult(SettingNames.FAKE_DELHI_POINTS);
    }

    @Override
    public boolean isAllowPoliticalAdminSearch() throws ApplicationException {
        return getBooleanResult(SettingNames.ALLOW_POLITICAL_ADMIN_SEARCH);
    }

}
