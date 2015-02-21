package com.next.eswaraj.admin.jsf.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Document;
import com.eswaraj.domain.nodes.Document.DocumentType;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.jsf.convertor.LocationSearchResultConvertor;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class TimelineItemBean extends BaseBean {

    private boolean showList;
    private List<TimelineItem> timelineItems;
    private TimelineItem selectedTimelineItem;
    private List<PoliticalBodyAdminSearchResult> allAdmins;
    
    private List<PoliticalBodyAdminSearchResult> selectedAdmins;

    private List<Location> selectedLocations;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AwsUploadUtil awsUploadUtil;

    @Autowired
    private LocationSearchResultConvertor locationSearchResultConvertor;

    @PostConstruct
    public void init() {
        try {
            showList = true;
            timelineItems = adminService.getTimelineItems(0, 20);
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void createTimelineItem() {
        selectedTimelineItem = new TimelineItem();
        showList = false;
    }

    public void cancel() {
        selectedTimelineItem = new TimelineItem();
        showList = true;
    }

    public void saveTimelineItem() {
        try {
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
            timelineItems = adminService.getTimelineItems(0, 20);
            showList = true;
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to save Timeline Item", e);
        }

    }

    public List<PoliticalBodyAdminSearchResult> completeAdmin(String query) {
        try {
            logger.info("Searching for {}", query);
            allAdmins = adminService.searchPoliticalAdmin(query);
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to search Admins", e);
        }
        return allAdmins;
    }

    public void handleAdminSelect(SelectEvent event) {
        logger.info("handleAdminSelect : " + event.getObject());
        if (selectedLocations == null) {
            selectedLocations = new ArrayList<Location>();
        }
        selectedLocations.clear();
        for (PoliticalBodyAdminSearchResult oneSelectedAdmin : selectedAdmins) {
            logger.info("handleAdminSelect addning one Location " + oneSelectedAdmin.getLocation());
            selectedLocations.add(oneSelectedAdmin.getLocation());
        }

    }

    public void handleAdminChange(AjaxBehaviorEvent event) {
        logger.info("handleAdminChange : " + event.getSource());
        if (selectedLocations == null) {
            selectedLocations = new ArrayList<Location>();
        }
        selectedLocations.clear();
        for (PoliticalBodyAdminSearchResult oneSelectedAdmin : selectedAdmins) {
            logger.info("handleAdminChange addning one Location " + oneSelectedAdmin.getLocation());
            selectedLocations.add(oneSelectedAdmin.getLocation());
        }

    }

    public List<LocationSearchResult> completeLocation(String query) {
        List<LocationSearchResult> locations = null;
        try {
            logger.info("Searching for {}", query);
            locations = adminService.searchLocationByName(query);
            locationSearchResultConvertor.setLocations(locations);
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to search Admins", e);
        }
        return locations;
    }

    public void handleFileUpload1(FileUploadEvent event) {
        String imageType = ".jpg";
        String remoteFileName = selectedTimelineItem.getId() + "_1" + imageType;
        try {
            String httpFilePath = uploadFile(event, remoteFileName);
            selectedTimelineItem.setImage1(httpFilePath);
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void handleFileUpload2(FileUploadEvent event) {
        String imageType = ".jpg";
        String remoteFileName = selectedTimelineItem.getId() + "_2" + imageType;
        try {
            String httpFilePath = uploadFile(event, remoteFileName);
            selectedTimelineItem.setImage2(httpFilePath);
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void handleFileUpload3(FileUploadEvent event) {
        String imageType = ".jpg";
        String remoteFileName = selectedTimelineItem.getId() + "_3" + imageType;
        try {
            String httpFilePath = uploadFile(event, remoteFileName);
            selectedTimelineItem.setImage3(httpFilePath);
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void handleFileUpload4(FileUploadEvent event) {
        String imageType = ".jpg";
        String remoteFileName = selectedTimelineItem.getId() + "_4" + imageType;
        try {
            String httpFilePath = uploadFile(event, remoteFileName);
            selectedTimelineItem.setImage4(httpFilePath);
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public String uploadFile(FileUploadEvent event, String remoteFileName) throws FileNotFoundException, IOException {
        String httpFilePath = awsUploadUtil.uploadTimelineImageJpeg(remoteFileName, event.getFile().getInputstream());
        return httpFilePath;
    }

    public void handleDocFileUpload(FileUploadEvent event) {

        String imageType = ".pdf";
        String remoteFileName = selectedTimelineItem.getId() + imageType;
        try {
            String httpFilePath = awsUploadUtil.uploadTimelineDocument(remoteFileName, event.getFile().getInputstream(), "pdf");
            // selectedElectionManifesto.setImageUrl(httpFilePath);
            Document document = selectedTimelineItem.getDocument();
            if (document == null) {
                document = new Document();
            }
            document.setUrl(httpFilePath);
            document.setType(DocumentType.Pdf);
            selectedTimelineItem.setDocument(document);
            selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
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

    public TimelineItem getSelectedTimelineItem() {
        return selectedTimelineItem;
    }

    public void setSelectedTimelineItem(TimelineItem selectedTimelineItem) {
        this.selectedTimelineItem = new TimelineItem();
        BeanUtils.copyProperties(selectedTimelineItem, this.selectedTimelineItem);
        showList = false;
    }

    public List<TimelineItem> getTimelineItems() {
        return timelineItems;
    }

    public void setTimelineItems(List<TimelineItem> timelineItems) {
        this.timelineItems = timelineItems;
    }

    public List<PoliticalBodyAdminSearchResult> getAllAdmins() {
        return allAdmins;
    }

    public void setAllAdmins(List<PoliticalBodyAdminSearchResult> allAdmins) {
        this.allAdmins = allAdmins;
    }

    public List<PoliticalBodyAdminSearchResult> getSelectedAdmins() {
        return selectedAdmins;
    }

    public void setSelectedAdmins(List<PoliticalBodyAdminSearchResult> selectedAdmins) {
        this.selectedAdmins = selectedAdmins;
    }

    public List<Location> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<Location> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

}
