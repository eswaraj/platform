package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Party;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PoliticalPartyBean extends BaseBean {

    private boolean showList;
    private List<Party> parties;
    private Party selectedParty;

    @Autowired
    private AdminService adminService;

    @PostConstruct
    public void init() {
        try {
            showList = true;
            parties = adminService.getAllParties();
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createParty() {
        selectedParty = new Party();
        showList = false;
    }

    public void cancel() {
        selectedParty = new Party();
        showList = true;
    }

    public void saveParty() {
        try {
            selectedParty = adminService.saveParty(selectedParty);
            parties = adminService.getAllParties();
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

    public Party getSelectedParty() {
        return selectedParty;
    }

    public void setSelectedParty(Party selectedParty) {
        this.selectedParty = new Party();
        BeanUtils.copyProperties(selectedParty, this.selectedParty);
        showList = false;
    }

    public List<Party> getParties() {
        if (parties == null) {
            init();
        }
        return parties;
    }

    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

}
