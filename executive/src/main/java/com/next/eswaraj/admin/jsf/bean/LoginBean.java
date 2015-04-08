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

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Department;
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

    private List<Department> userDepartments;

    private Department selectedDepartment;

    private String userName;
    private String password;

    @Autowired
    private ApplicationContext applicationCtx;
    

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {

    }

    public void login() {
        System.out.println("Login with " + userName);
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            user = adminService.login(userName, password);
            if (user != null) {

                sessionUtil.setLoggedInUserinSession(httpServletRequest, user);
            }
            refreshLoginRoles();
            // Move to Redirect page
            String redirectUrl = httpServletRequest.getParameter("redirect_url");
            if (StringUtils.isEmpty(redirectUrl)) {
                redirectUrl = "/admin/complaints.xhtml";
            }
            redirect(redirectUrl);
        } catch (ApplicationException e) {
            sendErrorMessage("Error", e.getMessage());
        }

    }

    public void logout() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        httpServletRequest.getSession().invalidate();
        String redirectUrl = "/admin/login.xhtml";
        redirect(redirectUrl);
    }

    public void refreshLoginRoles() {
        try {
            userDepartments = adminService.getUserDepartments(user.getId());

            if (userDepartments.size() == 1) {
                selectedDepartment = userDepartments.get(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onSelectDepartment() {
        HttpServletRequest request = getHttpServletRequest();
        Enumeration<String> paramNames = request.getSession().getAttributeNames();
        while (paramNames.hasMoreElements()) {
            logger.info("param : " + paramNames.nextElement());
        }
        request.getSession().removeAttribute("scopedTarget.complaintsBean");
        refreshPage();
    }

    public List<Department> getUserDepartments() {
        return userDepartments;
    }

    public void setUserDepartments(List<Department> userDepartments) {
        this.userDepartments = userDepartments;
    }

    public Department getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Department selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
