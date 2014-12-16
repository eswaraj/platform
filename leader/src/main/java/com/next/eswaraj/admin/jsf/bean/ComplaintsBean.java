package com.next.eswaraj.admin.jsf.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.web.dto.UserDto;
import com.next.eswaraj.admin.jsf.dto.ComplaintSearchResultDto;
import com.next.eswaraj.admin.service.AdminService;
import com.next.eswaraj.web.session.SessionUtil;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class ComplaintsBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionUtil sessionUtil;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<ComplaintSearchResultDto> complaints;

    private ComplaintSearchResult selectedComplaint;

    private List<PoliticalBodyAdmin> userPoliticalBodyAdmins;
    
    private PoliticalBodyAdmin selectedPoliticalBodyAdmin;

    private boolean showList = true;

    private MapModel mapModel;

    private List<Photo> complaintPhotos;

    private List<String> images;

    private List<Person> complaintCreators;

    private Map<Long, Category> categoryMap = new HashMap<Long, Category>();

    @PostConstruct
    public void init() {
        try {
            logger.info("Getting Political Admin Records From DB");
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            UserDto userDto = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
            userPoliticalBodyAdmins = adminService.getUserPoliticalBodyAdmins(userDto.getId());
            logger.info("Records : " + userPoliticalBodyAdmins);

            List<Category> allCategories = adminService.getAllcategories();
            logger.info("allCategories : " + allCategories);
            for (Category oneCategory : allCategories) {
                logger.info("oneCategory : " + oneCategory);
                categoryMap.put(oneCategory.getId(), oneCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancel() {
        showList = true;
    }

    public List<ComplaintSearchResultDto> getComplaints() {
        if (complaints == null || complaints.isEmpty()) {
            init();
        }
        return complaints;
    }

    public void onSelectPoliticalBodyAdmin() {

        if (selectedPoliticalBodyAdmin == null) {

        } else {
            try {
                List<ComplaintSearchResult> complaints = adminService.getPoliticalAdminComplaintsAll(selectedPoliticalBodyAdmin.getId());
                this.complaints = new ArrayList<ComplaintSearchResultDto>();
                for (ComplaintSearchResult oneComplaintSearchResult : complaints) {
                    Category rootCategory = null;
                    Category subCategory = null;
                    for (Category oneCategory : oneComplaintSearchResult.getComplaint().getCategories()) {
                        Category loadedCategory = categoryMap.get(oneCategory.getId());
                        if (loadedCategory != null) {
                            if(loadedCategory.isRoot()){
                                rootCategory = loadedCategory;
                            }else{
                                subCategory = loadedCategory;
                            }
                        }
                    }
                    ComplaintSearchResultDto oneComplaintSearchResultDto = new ComplaintSearchResultDto(oneComplaintSearchResult, rootCategory, subCategory);
                    this.complaints.add(oneComplaintSearchResultDto);
                    System.out.println("Complaint : " + oneComplaintSearchResult.getComplaint().getId());
                    try {
                        System.out.println("   oneComplaintPoliticalAdmin : " + oneComplaintSearchResult.getComplaintPoliticalAdmin().getId());
                    } catch (Exception ex) {
                        System.out.println("   oneComplaintPoliticalAdmin(Ex) : " + oneComplaintSearchResult.getComplaintPoliticalAdmin());
                    }

                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
        }
    }

    public void setComplaints(List<ComplaintSearchResultDto> complaints) {
        this.complaints = complaints;
    }

    public ComplaintSearchResult getSelectedComplaint() {
        return selectedComplaint;
    }

    public void setSelectedComplaint(ComplaintSearchResult selectedComplaint) {
        this.selectedComplaint = selectedComplaint;
        showList = false;
        mapModel = new DefaultMapModel();
        LatLng coord1 = new LatLng(selectedComplaint.getComplaint().getLattitude(), selectedComplaint.getComplaint().getLongitude());

        // Basic marker
        mapModel.addOverlay(new Marker(coord1, selectedComplaint.getComplaint().getTitle()));

        try {
            images = new ArrayList<String>();
            complaintPhotos = adminService.getComplaintPhotos(selectedComplaint.getComplaint().getId());
            complaintCreators = adminService.getComplaintCreators(selectedComplaint.getComplaint().getId());
            for (Photo onePhoto : complaintPhotos) {
                images.add(onePhoto.getOrgUrl());
            }
        } catch (ApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isShowList() {
        return showList;
    }

    public void setShowList(boolean showList) {
        this.showList = showList;
    }

    public List<PoliticalBodyAdmin> getUserPoliticalBodyAdmins() {
        return userPoliticalBodyAdmins;
    }

    public void setUserPoliticalBodyAdmins(List<PoliticalBodyAdmin> userPoliticalBodyAdmins) {
        this.userPoliticalBodyAdmins = userPoliticalBodyAdmins;
    }

    public PoliticalBodyAdmin getSelectedPoliticalBodyAdmin() {
        return selectedPoliticalBodyAdmin;
    }

    public void setSelectedPoliticalBodyAdmin(PoliticalBodyAdmin selectedPoliticalBodyAdmin) {
        this.selectedPoliticalBodyAdmin = selectedPoliticalBodyAdmin;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public List<Photo> getComplaintPhotos() {
        return complaintPhotos;
    }

    public void setComplaintPhotos(List<Photo> complaintPhotos) {
        this.complaintPhotos = complaintPhotos;
    }

    public List<Person> getComplaintCreators() {
        return complaintCreators;
    }

    public void setComplaintCreators(List<Person> complaintCreators) {
        this.complaintCreators = complaintCreators;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
