package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Election;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class ElectionBean extends BaseBean {

    private boolean showList;
    private List<Election> elections;
    private Election selectedElection;

    @Autowired
    private AdminService adminService;
    
    @PostConstruct
    public void init() {
        try {
            showList = true;
            elections = adminService.getAllElections();
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createElection() {
        selectedElection = new Election();
        showList = false;
    }

    public void cancel() {
        selectedElection = new Election();
        showList = true;
    }

    public void saveElection() {
        try {
            selectedElection = adminService.saveElection(selectedElection);
            elections = adminService.getAllElections();
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

    public Election getSelectedElection() {
        return selectedElection;
    }

    public void setSelectedElection(Election selectedElection) {
        this.selectedElection = new Election();
        BeanUtils.copyProperties(selectedElection, this.selectedElection);
        showList = false;
    }

    public List<Election> getElections() {
        return elections;
    }

    public void setElections(List<Election> elections) {
        this.elections = elections;
    }

}
