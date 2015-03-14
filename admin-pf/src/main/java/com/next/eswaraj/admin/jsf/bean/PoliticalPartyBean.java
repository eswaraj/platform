package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class PoliticalPartyBean extends BaseBean {

    private boolean showList;
    private List<Party> parties;
    private Party selectedParty;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AwsUploadUtil awsImageUploadUtil;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

    public void handleFileUpload(FileUploadEvent event) {
        String imageType = ".jpg";
        String remoteFileName = selectedParty.getId() + imageType;
        try {
            String httpFilePath = awsImageUploadUtil.uploadProfileImageJpeg(remoteFileName, event.getFile().getInputstream());
            selectedParty.setImageUrl(httpFilePath);
            selectedParty = adminService.saveParty(selectedParty);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
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
