package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.Photo;
import com.eswaraj.domain.nodes.extended.ComplaintSearchResult;
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

    private boolean showList = true;

    @PostConstruct
    public void init() {
        try {
            logger.info("Getting Complaints From DB");
            // HttpServletRequest httpServletRequest = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            // UserDto userDto = sessionUtil.getLoggedInUserFromSession(httpServletRequest);
            complaints = adminService.getPoliticalAdminComplaintsAll(84069L);
            for (ComplaintSearchResult oneComplaintSearchResult : complaints) {
                System.out.println("Complaint : " + oneComplaintSearchResult.getComplaint().getId());
                for (Location oneLocation : oneComplaintSearchResult.getLocation()) {
                    System.out.println("   Location : " + oneLocation.getId());
                }
                for (Person oneComplaintLoggedByPerson : oneComplaintSearchResult.getComplaintLoggedByPerson()) {
                    System.out.println("   ComplaintLoggedByPerson : " + oneComplaintLoggedByPerson.getId());
                }
                for (Photo onePhoto : oneComplaintSearchResult.getComplaintPhoto()) {
                    System.out.println("   onePhoto : " + onePhoto.getId());
                }
                System.out.println("   oneComplaintPoliticalAdmin : " + oneComplaintSearchResult.getComplaintPoliticalAdmin().getId());

            }
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

}
