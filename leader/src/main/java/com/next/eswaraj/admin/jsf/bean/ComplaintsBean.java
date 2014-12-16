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
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
import com.eswaraj.web.dto.UserDto;
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

    private List<ComplaintSearchResult> complaints;

    private ComplaintSearchResult selectedComplaint;

    private List<PoliticalBodyAdmin> userPoliticalBodyAdmins;
    
    private PoliticalBodyAdmin selectedPoliticalBodyAdmin;

    private boolean showList = true;

    @PostConstruct
    public void init() {
        try {
            logger.info("Getting Complaints From DB");
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            UserDto userDto = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
            userPoliticalBodyAdmins = adminService.getUserPoliticalBodyAdmins(userDto.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cancel() {
        showList = true;
    }

    public List<ComplaintSearchResult> getComplaints() {
        if (complaints == null || complaints.isEmpty()) {
            init();
        }
        return complaints;
    }

    public void onSelectPoliticalBodyAdmin() {

        if (selectedPoliticalBodyAdmin == null) {

        } else {
            try {
                complaints = adminService.getPoliticalAdminComplaintsAll(selectedPoliticalBodyAdmin.getId());
                for (ComplaintSearchResult oneComplaintSearchResult : complaints) {
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

    public void setComplaints(List<ComplaintSearchResult> complaints) {
        this.complaints = complaints;
    }

    public ComplaintSearchResult getSelectedComplaint() {
        return selectedComplaint;
    }

    public void setSelectedComplaint(ComplaintSearchResult selectedComplaint) {
        this.selectedComplaint = selectedComplaint;
        showList = false;
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

}
