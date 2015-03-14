package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.ElectionType;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class ElectionTypeBean extends BaseBean {

    private boolean showList;
    private List<ElectionType> electionTypes;
    private ElectionType selectedElectionType;

    @Autowired
    private AdminService adminService;
    
    @PostConstruct
    public void init() {
        try {
            showList = true;
            electionTypes = adminService.getAllElectionTypes();
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createElectionType() {
        selectedElectionType = new ElectionType();
        showList = false;
    }

    public void cancel() {
        selectedElectionType = new ElectionType();
        showList = true;
    }

    public void saveElectionType() {
        try {
            selectedElectionType = adminService.saveElectionType(selectedElectionType);
            electionTypes = adminService.getAllElectionTypes();
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

    public ElectionType getSelectedElectionType() {
        return selectedElectionType;
    }

    public void setSelectedElectionType(ElectionType selectedElectionType) {
        this.selectedElectionType = new ElectionType();
        BeanUtils.copyProperties(selectedElectionType, this.selectedElectionType);
        showList = false;
    }

    public List<ElectionType> getElectionTypes() {
        return electionTypes;
    }

    public void setElectionTypes(List<ElectionType> electionTypes) {
        this.electionTypes = electionTypes;
    }

}
