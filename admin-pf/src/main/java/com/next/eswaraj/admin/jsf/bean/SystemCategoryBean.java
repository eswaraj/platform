package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.SystemCategory;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class SystemCategoryBean extends BaseBean {

    private boolean showList;
    private List<SystemCategory> systemCategories;
    private SystemCategory selectedSystemCategory;

    @Autowired
    private AdminService adminService;
    
    @PostConstruct
    public void init() {
        try {
            showList = true;
            systemCategories = adminService.getAllSystemCategories();
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createSystemCategory() {
        selectedSystemCategory = new SystemCategory();
        showList = false;
    }

    public void cancel() {
        selectedSystemCategory = new SystemCategory();
        showList = true;
    }

    public void saveSystemCategory() {
        try {
            selectedSystemCategory = adminService.saveSystemCategory(selectedSystemCategory);
            systemCategories = adminService.getAllSystemCategories();
            showList = true;
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }

    public boolean isShowList() {
        return showList;
    }

    public void setShowList(boolean showList) {
        this.showList = showList;
    }

    public SystemCategory getSelectedSystemCategory() {
        return selectedSystemCategory;
    }

    public void setSelectedSystemCategory(SystemCategory selectedSystemCategory) {
        this.selectedSystemCategory = new SystemCategory();
        BeanUtils.copyProperties(selectedSystemCategory, this.selectedSystemCategory);
        showList = false;
    }

    public List<SystemCategory> getSystemCategories() {
        return systemCategories;
    }

    public void setSystemCategories(List<SystemCategory> systemCategories) {
        this.systemCategories = systemCategories;
    }

}
