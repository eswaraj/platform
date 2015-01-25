package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

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
