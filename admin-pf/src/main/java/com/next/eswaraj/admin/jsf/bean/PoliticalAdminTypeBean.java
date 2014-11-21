package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PoliticalAdminTypeBean extends BaseBean {

    private boolean showList;
    private List<PoliticalBodyType> politicalBodyTypes;
    private PoliticalBodyType selectedPoliticalBodyType;
    private List<LocationType> locationTypes;

    @Autowired
    private AdminService adminService;

    @PostConstruct
    public void init() {
        try {
            showList = true;
            locationTypes = adminService.getAllLocationTypes();
            politicalBodyTypes = adminService.getAllPoliticalBodyTypes();
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }
    }
    public void createPoliticalAdminType() {
        selectedPoliticalBodyType = new PoliticalBodyType();
        showList = false;
    }

    public void cancel() {
        selectedPoliticalBodyType = new PoliticalBodyType();
        showList = true;
    }

    public void savePoliticalBodyType() {
        try {
            System.out.println("LocationType = " + selectedPoliticalBodyType.getLocationType());
            selectedPoliticalBodyType = adminService.savePoliticalBodyType(selectedPoliticalBodyType);
            politicalBodyTypes = adminService.getAllPoliticalBodyTypes();
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

    public List<PoliticalBodyType> getPoliticalBodyTypes() {
        return politicalBodyTypes;
    }

    public void setPoliticalBodyTypes(List<PoliticalBodyType> politicalBodyTypes) {
        this.politicalBodyTypes = politicalBodyTypes;
    }

    public PoliticalBodyType getSelectedPoliticalBodyType() {
        return selectedPoliticalBodyType;
    }

    public void setSelectedPoliticalBodyType(PoliticalBodyType selectedPoliticalBodyType) {
        this.selectedPoliticalBodyType = new PoliticalBodyType();
        BeanUtils.copyProperties(selectedPoliticalBodyType, this.selectedPoliticalBodyType);
        showList = false;
    }

    public List<LocationType> getLocationTypes() {
        return locationTypes;
    }

    public void setLocationTypes(List<LocationType> locationTypes) {
        this.locationTypes = locationTypes;
    }

}
