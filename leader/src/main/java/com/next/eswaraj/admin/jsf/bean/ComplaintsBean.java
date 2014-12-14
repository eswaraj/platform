package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class ComplaintsBean {

    @Autowired
    private AdminService adminService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<ComplaintSearchResult> complaints;

    private ComplaintSearchResult selectedComplaint;

    @PostConstruct
    public void init() {
        try {
            logger.info("Getting Location From DB");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<ComplaintSearchResult> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<ComplaintSearchResult> complaints) {
        this.complaints = complaints;
    }

    public ComplaintSearchResult getSelectedComplaint() {
        return selectedComplaint;
    }

    public void setSelectedComplaint(ComplaintSearchResult selectedComplaint) {
        this.selectedComplaint = selectedComplaint;
    }

}
