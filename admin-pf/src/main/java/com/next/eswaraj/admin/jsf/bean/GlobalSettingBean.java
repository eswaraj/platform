package com.next.eswaraj.admin.jsf.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.SettingService;
import com.eswaraj.domain.nodes.Setting;
import com.eswaraj.domain.nodes.Setting.SettingNames;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class GlobalSettingBean extends BaseBean {

    private List<Setting> settings;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private SettingService settingService;

    @PostConstruct
    public void init() {
        try {
            settings = settingService.getAllSettings();
            Map<String, Setting> settingMap = new HashMap<String, Setting>();
            for (Setting oneSetting : settings) {
                settingMap.put(oneSetting.getName(), oneSetting);
            }
            SettingNames[] settingNames = Setting.SettingNames.values();
            for (SettingNames oneSettingName : settingNames) {
                if (settingMap.get(oneSettingName.getName()) == null) {
                    Setting setting = new Setting();
                    setting.setName(oneSettingName.getName());
                    setting.setType("Global");
                    setting.setDescription(oneSettingName.getDescription());
                    settings.add(setting);
                }
            }
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void onRowEdit(RowEditEvent event) {
        Setting setting = (Setting) event.getObject();
        try {
            setting = settingService.saveSetting(setting);
            sendInfoMessage("Saved", "Setting Saved Succesfully");
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to save Setting", e);
        }

    }

    public void onRowCancel(RowEditEvent event) {
    }

    public void onCellEdit(CellEditEvent event) {
        Setting setting = settings.get(event.getRowIndex());
        try {
            setting = settingService.saveSetting(setting);
            sendInfoMessage("Saved", "Setting Saved Succesfully");
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to save Setting", e);
        }

    }

    public List<Setting> getSettings() {
        return settings;
    }

    public void setSettings(List<Setting> settings) {
        this.settings = settings;
    }
}
