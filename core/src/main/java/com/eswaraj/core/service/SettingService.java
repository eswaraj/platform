package com.eswaraj.core.service;

import java.util.List;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Setting;

public interface SettingService {

    Setting saveSetting(Setting setting) throws ApplicationException;

    List<Setting> getAllSettings() throws ApplicationException;

    Setting getSetting(String settingName) throws ApplicationException;

    boolean isAllowComplaint() throws ApplicationException;

    boolean isFakeLocation() throws ApplicationException;

    boolean isAllowPoliticalAdminSearch() throws ApplicationException;
}
