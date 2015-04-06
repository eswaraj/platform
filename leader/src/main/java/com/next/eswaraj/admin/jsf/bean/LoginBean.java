package com.next.eswaraj.admin.jsf.bean;

import java.util.Enumeration;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.User;
import com.next.eswaraj.admin.service.AdminService;
import com.next.eswaraj.web.session.SessionUtil;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class LoginBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    private User user;

    @Autowired
    private SessionUtil sessionUtil;

    private List<PoliticalBodyAdmin> userPoliticalBodyAdmins;

    private PoliticalBodyAdmin selectedPoliticalBodyAdmin;


    @Autowired
    private ApplicationContext applicationCtx;
    
    private String userName;
    private String password;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
    }

    public void login() {
        System.out.println("Login with " + userName);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            user = adminService.login(userName, password);
            logger.info("Logged In User : {}", user);
            if (user != null) {

                sessionUtil.setLoggedInUserinSession(httpServletRequest, user);
                refreshLoginRoles();
            }
            // Move to Redirect page
            String redirectUrl = httpServletRequest.getParameter("redirect_url");
            if (StringUtils.isEmpty(redirectUrl)) {
                redirectUrl = "/admin/complaints.xhtml";
            }
            redirect(redirectUrl);
        } catch (Exception e) {
            sendErrorMessage("Error", e.getMessage(), e);
        } finally {
            password = null;
        }

    }

    public void refreshLoginRoles() {
        try {
            user = sessionUtil.getLoggedInUserFromSession();
            userPoliticalBodyAdmins = adminService.getUserPoliticalBodyAdmins(user.getId());

            if (userPoliticalBodyAdmins.size() == 1) {
                selectedPoliticalBodyAdmin = userPoliticalBodyAdmins.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onSelectPoliticalBodyAdmin() {
        HttpServletRequest request = getHttpServletRequest();
        Enumeration<String> paramNames = request.getSession().getAttributeNames();
        while (paramNames.hasMoreElements()) {
            logger.info("param : " + paramNames.nextElement());
        }
        request.getSession().removeAttribute("scopedTarget.staffBean");
        request.getSession().removeAttribute("scopedTarget.complaintsBean");
        refreshPage();
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
