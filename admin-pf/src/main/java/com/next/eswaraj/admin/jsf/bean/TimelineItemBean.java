package com.next.eswaraj.admin.jsf.bean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Document;
import com.eswaraj.domain.nodes.Document.DocumentType;
import com.eswaraj.domain.nodes.ElectionManifestoPromise;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.TimelineItem;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminSearchResult;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.jsf.convertor.ElectionPromiseConvertor;
import com.next.eswaraj.admin.jsf.convertor.LocationSearchResultConvertor;
import com.next.eswaraj.admin.jsf.convertor.PoliticalBodyAdminSearchResultConvertor;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class TimelineItemBean extends BaseBean {

    private boolean showList;
    private List<TimelineItem> timelineItems;
    private TimelineItem selectedTimelineItem;
    private List<PoliticalBodyAdminSearchResult> allAdmins;
    
    private List<PoliticalBodyAdminSearchResult> selectedAdmins;

    private List<LocationSearchResult> selectedLocations;

    private List<ElectionManifestoPromise> promises;

    private List<ElectionManifestoPromise> selectedPromises;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AwsUploadUtil awsUploadUtil;

    @Autowired
    private LocationSearchResultConvertor locationSearchResultConvertor;

    @Autowired
    private ElectionPromiseConvertor electionPromiseConvertor;

    @Autowired
    private PoliticalBodyAdminSearchResultConvertor politicalBodyAdminSearchResultConvertor;

    @PostConstruct
    public void init() {
        try {
            showList = true;
            timelineItems = adminService.getTimelineItems(0, 20);
            promises = adminService.getAllPromises();
            electionPromiseConvertor.setPromises(promises);
            System.out.println("All Timelines : " + timelineItems);
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
        try {
            timelineItems = adminService.getTimelineItems(0, 20);
            System.out.println("All Timelines : " + timelineItems);
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }

    public void clearAlllData() {
        selectedAdmins.clear();
        selectedLocations.clear();
        selectedPromises.clear();
        locationSearchResultConvertor.clear();
        politicalBodyAdminSearchResultConvertor.clear();
    }
    public void saveTimelineItem() {
        try {
            System.out.println("********************");
            if (selectedLocations == null) {
                selectedLocations = new ArrayList<LocationSearchResult>();
            }
            Set<Location> locations = new HashSet<Location>();
            for (PoliticalBodyAdminSearchResult oneAdmin : selectedAdmins) {
                System.out.println("oneAdmin : = " + oneAdmin.getPerson().getName() + ", " + oneAdmin.getPoliticalBodyType().getShortName() + ", " + oneAdmin.getLocation().getName());
                locations.add(oneAdmin.getLocation());
            }
            for (LocationSearchResult oneLocation : selectedLocations) {
                locations.add(oneLocation.getLocation());
            }
            for (Location oneLocation : locations) {
                System.out.println("oneLocation : " + oneLocation.getName());
            }
            for (ElectionManifestoPromise onePromise : selectedPromises) {
                System.out.println("onePromise : " + onePromise.getTitle());
            }
            System.out.println("********************");
            adminService.saveTimelineItem(selectedTimelineItem, selectedAdmins, locations, selectedPromises);
            clearAlllData();
            // selectedTimelineItem = adminService.saveTimelineItem(selectedTimelineItem);
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

    public List<LocationSearchResult> completeLocation(String query) {
        List<LocationSearchResult> locations = null;
        try {
            logger.info("Searching for {}", query);
            locations = adminService.searchLocationByName(query);
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
        System.out.println("selectedTimelineItem : " + selectedTimelineItem);
        this.selectedTimelineItem = new TimelineItem();
        BeanUtils.copyProperties(selectedTimelineItem, this.selectedTimelineItem);
        System.out.println("this.selectedTimelineItem : " + this.selectedTimelineItem);
        showList = false;
        // Load all Relations from DB
        try {
            selectedLocations = adminService.getTimelineLocations(selectedTimelineItem);
            selectedPromises = adminService.getTimelinePromises(selectedTimelineItem);
            selectedAdmins = adminService.getTimelineAdmins(selectedTimelineItem);
        } catch (Exception e) {
            sendErrorMessage("Error", "Internal Server Error", e);
        }
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

    public List<LocationSearchResult> getSelectedLocations() {
        return selectedLocations;
    }

    public void setSelectedLocations(List<LocationSearchResult> selectedLocations) {
        this.selectedLocations = selectedLocations;
    }

    public List<ElectionManifestoPromise> getPromises() {
        return promises;
    }

    public void setPromises(List<ElectionManifestoPromise> promises) {
        this.promises = promises;
    }

    public List<ElectionManifestoPromise> getSelectedPromises() {
        return selectedPromises;
    }

    public void setSelectedPromises(List<ElectionManifestoPromise> selectedPromises) {
        this.selectedPromises = selectedPromises;
    }

}
