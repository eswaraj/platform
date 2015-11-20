package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Location;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ApplicationCacheBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AdminService adminService;

    private List<Location> allLocations;

    @PostConstruct
    public void init() {
    }

    public void refreshLocations() throws ApplicationException {
        allLocations = adminService.getAllLocations();
    }

    public List<Location> getAllLocations() throws ApplicationException {
        if (allLocations == null) {
            refreshLocations();
        }
        return allLocations;
    }

}
