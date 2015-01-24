package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyAdminStaff;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminStaffSearchResult;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.web.dto.UserDto;
import com.next.eswaraj.admin.service.AdminService;
import com.next.eswaraj.web.session.SessionUtil;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class StaffBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private QueueService queueService;

    @Autowired
    private PersonSearchBean personSearchBean;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<PoliticalBodyAdminStaffSearchResult> staff;

    private PoliticalBodyAdminStaffSearchResult selectedStaff;

    private List<PoliticalBodyAdmin> userPoliticalBodyAdmins;
    
    private PoliticalBodyAdmin selectedPoliticalBodyAdmin;

    private boolean showList = true;

    @PostConstruct
    public void init() {
        try {
            personSearchBean.setSelectedPerson(null);
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            UserDto userDto = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
            userPoliticalBodyAdmins = adminService.getUserPoliticalBodyAdmins(userDto.getId());

            if (userPoliticalBodyAdmins.size() == 1) {
                selectedPoliticalBodyAdmin = userPoliticalBodyAdmins.get(0);
                onSelectPoliticalBodyAdmin();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancel() {
        personSearchBean.setSelectedPerson(null);
    }

    public void saveAdminStaff() {
        Person selectedPerson = personSearchBean.getSelectedPerson();
        try {
            if (selectedPerson.getId().equals(selectedPoliticalBodyAdmin.getPerson().getId())) {
                sendErrorMessage("Error", "You can not add your self as Staff");
            }
            if (isValidInput()) {
                adminService.savePoliticalBodyAdminStaff(selectedPoliticalBodyAdmin, selectedPerson, "staff");
                refreshStaffList();
                personSearchBean.setSelectedPerson(null);
            }
        } catch (ApplicationException e) {
            sendErrorMessage("Error : Unable to save Staff", e.getMessage());
        }
    }

    public void removeAdminStaff() {
        removeAdminStaff(selectedStaff.getPoliticalBodyAdminStaff());
    }

    public void removeSelectePersonAdminStaff() {
        PoliticalBodyAdminStaffSearchResult staffTobeDeleted = getStaffForPerson(personSearchBean.getSelectedPerson());
        if (staffTobeDeleted != null) {
            removeAdminStaff(staffTobeDeleted.getPoliticalBodyAdminStaff());
        }
    }

    private void removeAdminStaff(PoliticalBodyAdminStaff politicalBodyAdminStaff) {
        try {
            logger.info("Removing politicalBodyAdminStaff : {}", politicalBodyAdminStaff);
            adminService.removePoliticalBodyAdminStaff(politicalBodyAdminStaff);
            refreshStaffList();
        } catch (ApplicationException e) {
            sendErrorMessage("Error : Unable to save Staff", e.getMessage());
        }
    }
    public void onSelectPoliticalBodyAdmin() {
        refreshStaffList();

    }

    private void refreshStaffList() {
        try {
            staff = adminService.getAdminStaffList(selectedPoliticalBodyAdmin.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public List<PoliticalBodyAdminStaffSearchResult> getStaff() {
        return staff;
    }

    public void setStaff(List<PoliticalBodyAdminStaffSearchResult> staff) {
        this.staff = staff;
    }

    public PoliticalBodyAdminStaffSearchResult getSelectedStaff() {
        return selectedStaff;
    }

    public void setSelectedStaff(PoliticalBodyAdminStaffSearchResult selectedStaff) {
        removeAdminStaff(selectedStaff.getPoliticalBodyAdminStaff());
        this.selectedStaff = null;
    }

    public boolean isShowPersonDetail() {
        return personSearchBean.getSelectedPerson() != null;
    }

    private PoliticalBodyAdminStaffSearchResult getStaffForPerson(Person person) {
        for (PoliticalBodyAdminStaffSearchResult oneStaff : staff) {
            if (oneStaff.getPerson().getId().equals(person.getId())) {
                return oneStaff;
            }
        }
        return null;
    }

    public boolean isShowCreateStaffButton() {
        if (personSearchBean.getSelectedPerson() == null || selectedPoliticalBodyAdmin == null) {
            return false;
        }
        if (getStaffForPerson(personSearchBean.getSelectedPerson()) == null) {
            return true;
        }
        return false;
    }

    public boolean isShowRemoveStaffButton() {
        return !isShowCreateStaffButton();
    }

}
