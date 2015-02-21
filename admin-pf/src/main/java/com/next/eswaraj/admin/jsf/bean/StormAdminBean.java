package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.queue.service.QueueService;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class StormAdminBean extends BaseBean {

    @Autowired
    private QueueService queueService;

    @Autowired
    private AdminService adminService;

    

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {

    }


    public void reprocessAllComplaints() {
        try {
            queueService.sendReprocesAllComplaints();
            sendInfoMessage("Success", "Complaint reporcess started, it may take few minutes to complete");
        } catch (ApplicationException e) {
            e.printStackTrace();
            sendErrorMessage("Error", "Unable to reproces all Complaints");
        }
    }

    public void reprocessAllLocations() {
        try {
            queueService.sendReprocesAllLocations();
            sendInfoMessage("Success", "Location reporcess started, it may take few minutes to complete");
        } catch (ApplicationException e) {
            e.printStackTrace();
            sendErrorMessage("Error", "Unable to reproces all Locations");
        }
    }

    public void reprocessAllPersons() {
        try {
            queueService.sendReprocesAllPersons();
            sendInfoMessage("Success", "Person reporcess started, it may take few minutes to complete");
        } catch (ApplicationException e) {
            e.printStackTrace();
            sendErrorMessage("Error", "Unable to reproces all Persons");
        }
    }

    public void reprocessAllComments() {
        try {
            queueService.sendReprocesAllPersons();
            sendInfoMessage("Success", "Comment reporcess started, it may take few minutes to complete");
        } catch (ApplicationException e) {
            e.printStackTrace();
            sendErrorMessage("Error", "Unable to reproces all Comment");
        }
    }

    public void reprocessAllCategories() {
        try {
            queueService.sendCategoryUpdateMessage(0L);
            sendInfoMessage("Success", "Comment reporcess started, it may take few minutes to complete");
        } catch (ApplicationException e) {
            e.printStackTrace();
            sendErrorMessage("Error", "Unable to reproces all Comment");
        }
    }

    public void reprocessAllPoliticalAdmins() {
        try {
            List<PoliticalBodyAdminSearchResult> admins = adminService.getAllActivePoliticalAdmin();
            for (PoliticalBodyAdminSearchResult onePoliticalBodyAdminSearchResult : admins) {
                queueService.sendPoliticalBodyAdminUpdateMessage(onePoliticalBodyAdminSearchResult.getLocation().getId(), onePoliticalBodyAdminSearchResult.getPoliticalBodyAdmin().getId());
            }
            //
            sendInfoMessage("Success", "Admin reporcess started, it may take few minutes to complete");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorMessage("Error", "Unable to reproces all Comment");
        }
    }
}
