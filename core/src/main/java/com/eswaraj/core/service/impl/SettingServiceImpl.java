package com.eswaraj.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.SettingService;
import com.eswaraj.domain.nodes.Setting;
import com.eswaraj.domain.repo.SettingRepository;

@Service
public class SettingServiceImpl extends BaseService implements SettingService {

    private static final long serialVersionUID = 1L;
    @Autowired
    private SettingRepository settingRepository;

    @Override
    @CachePut(value = "settings", key = "setting.#setting.name")
    public Setting saveSetting(Setting setting) throws ApplicationException {
        setting = settingRepository.save(setting);
        return setting;
    }

    @Override
    @Cacheable(value = "settings", key = "all.settings")
    public List<Setting> getAllSettings() throws ApplicationException {
        return convertToList(settingRepository.findAll());
    }

    @Override
    @Cacheable(value = "settings", key = "all.settings")
    public Setting getSetting(String settingName) throws ApplicationException {
        List<Setting> allSettings = getAllSettings();
        for (Setting oneSetting : allSettings) {
            if (oneSetting.getName().equalsIgnoreCase(settingName)) {
                return oneSetting;
            }
        }
        return null;
    }

}
