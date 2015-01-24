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

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<PoliticalBodyAdminStaffSearchResult> staff;

    private PoliticalBodyAdminStaffSearchResult selectedStaff;

    private List<PoliticalBodyAdmin> userPoliticalBodyAdmins;
    
    private PoliticalBodyAdmin selectedPoliticalBodyAdmin;

    private Person selectedPerson;

    private List<Person> personSearchResults;

    private boolean showList = true;


    @PostConstruct
    public void init() {
        try {
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
        showList = true;
    }

    public void onSelectPoliticalBodyAdmin() {
        refreshStaffList();

    }

    private void refreshStaffList() {

    }

    public List<Person> searchPerson(String query) {
        try {
            personSearchResults = adminService.searchPersonByName(query);
            return personSearchResults;
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        return null;

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
        this.selectedStaff = selectedStaff;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> getPersonSearchResults() {
        return personSearchResults;
    }

    public void setPersonSearchResults(List<Person> personSearchResults) {
        this.personSearchResults = personSearchResults;
    }

}
